package com.fin4bol.fin4bolbackend.configuration.security.jwt;

import com.fin4bol.fin4bolbackend.configuration.security.jwt.json.ApplicationUserJson;
import com.fin4bol.fin4bolbackend.service.ApplicationUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class RegistrationController {

    private final ApplicationUserService applicationUserService;

    public RegistrationController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    /**
     * Endpoint to register a new user.
     *
     * @param newUser new user
     * @return ResponseEntity
     */
    @PostMapping("sign-up")
    public ResponseEntity<Void> registerNewUser(@Valid @RequestBody ApplicationUserJson newUser) {
        applicationUserService.saveApplicationUser(newUser);
        return ResponseEntity.ok().build();
    }
}