package com.example.reviewflow.service;

import com.example.reviewflow.model.PasswordResetToken;
import com.example.reviewflow.model.User;
import com.example.reviewflow.model.VerificationToken;
import com.example.reviewflow.repository.PasswordResetTokenRepository;
import com.example.reviewflow.repository.UserRepository;
import com.example.reviewflow.repository.VerificationTokenRepository;
import com.example.reviewflow.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       VerificationTokenRepository verificationTokenRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public User register(String fullName, String email, String rawPassword, User.Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnabled(false);
        user = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken vt = new VerificationToken(
                token, user, Instant.now().plus(1, ChronoUnit.DAYS));
        verificationTokenRepository.save(vt);

        // In real app send email. For dev we just log to console.
        System.out.println("Verify email for " + email + " using token: " + token);

        return user;
    }

    @Transactional
    public boolean verifyEmail(String token) {
        Optional<VerificationToken> opt = verificationTokenRepository.findByToken(token);
        if (!opt.isPresent()) return false;
        VerificationToken vt = opt.get();
        if (vt.getExpiresAt().isBefore(Instant.now())) {
            return false;
        }
        User user = vt.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(vt);
        return true;
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!user.isEnabled()) {
            throw new IllegalStateException("Email not verified");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwtService.generateToken(user);
    }

    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user with that email"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = new PasswordResetToken(
                token, user, Instant.now().plus(1, ChronoUnit.HOURS));
        passwordResetTokenRepository.save(prt);
        System.out.println("Password reset link for " + email + " token: " + token);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> opt = passwordResetTokenRepository.findByToken(token);
        if (!opt.isPresent()) return false;
        PasswordResetToken prt = opt.get();
        if (prt.getExpiresAt().isBefore(Instant.now())) {
            return false;
        }
        User user = prt.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(prt);
        return true;
    }
}
