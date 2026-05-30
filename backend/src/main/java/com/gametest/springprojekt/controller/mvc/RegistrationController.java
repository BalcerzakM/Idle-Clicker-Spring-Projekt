    package com.gametest.springprojekt.controller.mvc;

    import com.gametest.springprojekt.dto.RegistrationDto;
    import com.gametest.springprojekt.exception.EmailAlreadyExistsException;
    import com.gametest.springprojekt.exception.UsernameAlreadyExistsException;
    import com.gametest.springprojekt.service.UserService;
    import jakarta.validation.Valid;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    @RequestMapping("/mvc/register")
    public class RegistrationController {

        private final UserService userService;

        public RegistrationController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping
        public String registration(
                Model model
        ) {
            model.addAttribute("registrationDto", new RegistrationDto());
            return "registration";
        }

        @PostMapping
        public String register(
                @Valid @ModelAttribute("registrationDto") RegistrationDto request,
                BindingResult bindingResult
                ){
            if (bindingResult.hasErrors()) {
                return "registration";
            }
            try{
                userService.registerUser(request);
            }catch (UsernameAlreadyExistsException e) {
                bindingResult.rejectValue("username", "error.username", e.getMessage());
                return "registration";
            }catch (EmailAlreadyExistsException e) {
                bindingResult.rejectValue("email", "error.email", e.getMessage());
                return "registration";
            }


            return "redirect:/mvc/login";
        }

    }
