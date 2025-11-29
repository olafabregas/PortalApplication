package com.example.reviewflow.controller;

import com.example.reviewflow.dto.LoginRequest;
import com.example.reviewflow.dto.RegisterRequest;
import com.example.reviewflow.model.User;
import com.example.reviewflow.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("loginRequest") @Valid LoginRequest loginRequest,
                          BindingResult bindingResult,
                          HttpServletResponse response,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            String jwt = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .path("/")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("loginError", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("registerRequest") @Valid RegisterRequest request,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        // 1) Confirm password match
        if (password == null || !password.equals(confirmPassword)) {
            model.addAttribute("registerError", "Passwords do not match.");
            return "register";
        }

        // 2) Strength rules: ≥ 8 chars, 1 uppercase, 1 digit, 1 symbol
        String pattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        if (password == null || !password.matches(pattern)) {
            model.addAttribute("registerError",
                    "Password must be at least 8 characters and include an uppercase letter, a number, and a symbol.");
            return "register";
        }

        try {
            User user = authService.register(
                    request.getFullName(),
                    request.getEmail(),
                    password,
                    request.toRoleEnum()
            );
            model.addAttribute("message",
                    "Account created. Please check console for verification token, then open /verify-email?token=...");
            return "login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("registerError", ex.getMessage());
            return "register";
        }
    }


    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        boolean success = authService.verifyEmail(token);
        model.addAttribute("message",
                success ? "Email verified. You can now log in." : "Invalid or expired verification link.");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            authService.createPasswordResetToken(email);
            model.addAttribute("message",
                    "If an account exists, a reset link has been logged to the console.");
        } catch (Exception e) {
            model.addAttribute("message",
                    "If an account exists, a reset link has been logged to the console.");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {

        // 1) Confirm password match
        if (password == null || !password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Passwords do not match.");
            return "reset-password";
        }

        // 2) Strength rules: ≥8 chars, 1 uppercase, 1 digit, 1 symbol
        String pattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        if (password == null || !password.matches(pattern)) {
            model.addAttribute("token", token);
            model.addAttribute("error",
                    "Password must be at least 8 characters and include an uppercase letter, a number, and a symbol.");
            return "reset-password";
        }

        boolean success = authService.resetPassword(token, password);
        model.addAttribute("message",
                success ? "Password updated. Please log in." : "Invalid or expired reset link.");
        return "login";
    }

}
