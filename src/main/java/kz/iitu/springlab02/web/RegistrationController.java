package kz.iitu.springlab02.web;

import jakarta.validation.Valid;
import kz.iitu.springlab02.catalog.AppUser;
import kz.iitu.springlab02.catalog.AppUserRepository;
import kz.iitu.springlab02.web.dto.RegistrationForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userForm") RegistrationForm form,
                               BindingResult result, Model model) {

        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            result.rejectValue("username", "error.userForm", "Username is already taken");
        }

        if (result.hasErrors()) {
            return "register";
        }


        AppUser user = new AppUser();
        user.setUsername(form.getUsername());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.getRoles().add("USER");
        userRepository.save(user);

        return "redirect:/login";
    }
}