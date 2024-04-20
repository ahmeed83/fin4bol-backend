package com.fin4bol.fin4bolbackend.configuration.security.oauth;

import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService clientService;

    public OAuth2Controller(UserRepository userRepository, final OAuth2AuthorizedClientService clientService) {
        this.userRepository = userRepository;
        this.clientService = clientService;
    }

    @GetMapping("/login/oauth2/code/{provider}")
    public RedirectView loginSuccess(@PathVariable String provider, OAuth2AuthenticationToken authenticationToken) {

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );

        String userName = client.getPrincipalName();
        String provider2 = authenticationToken.getAuthorizedClientRegistrationId();


        ApplicationUser existingUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("UserName %s not found", userName)));

        if (existingUser == null) {
            ApplicationUser user = new ApplicationUser();
            user.setUserName(userName);
            user.setProvider(provider2);
            userRepository.save(user);
        } else {
            return new RedirectView("/login-error");
        }

        return new RedirectView("/login-success");
    }
}