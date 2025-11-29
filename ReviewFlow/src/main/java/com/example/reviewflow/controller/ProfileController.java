package com.example.reviewflow.controller;

import com.example.reviewflow.dto.ProfileUpdateRequest;
import com.example.reviewflow.model.User;
import com.example.reviewflow.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping({"/", "/profile"})
    public String profile(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = profileService.getCurrentUser(authentication);
        ProfileUpdateRequest dto = new ProfileUpdateRequest();
        dto.setFullName(user.getFullName());
        model.addAttribute("user", user);
        model.addAttribute("profileUpdateRequest", dto);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("profileUpdateRequest") @Valid ProfileUpdateRequest request,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        User user = profileService.getCurrentUser(authentication);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "profile";
        }
        user = profileService.updateProfile(authentication, request.getFullName());
        model.addAttribute("user", user);
        model.addAttribute("profileUpdateRequest", request);
        model.addAttribute("message", "Profile updated.");
        return "profile";
    }
}
