package io.donghun.blog.user.controller;

import io.donghun.blog.common.exception.BusinessException;
import io.donghun.blog.user.dto.UserSignUpRequest;
import io.donghun.blog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        model.addAttribute("signUpRequest", new UserSignUpRequest("", "", ""));
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(
            @Valid @ModelAttribute("signUpRequest") UserSignUpRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userService.signUp(request);
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
