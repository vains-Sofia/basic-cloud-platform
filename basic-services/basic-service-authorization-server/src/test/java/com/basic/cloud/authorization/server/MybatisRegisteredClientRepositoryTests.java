package com.basic.cloud.authorization.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author vains
 */
@SpringBootTest
class MybatisRegisteredClientRepositoryTests {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @BeforeEach
    void setUp() {
        UserDetails user = userDetailsService.loadUserByUsername("user");
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void initClients() {
        Clients.defaultClients().forEach(this.registeredClientRepository::save);
    }

    @Test
    public void findById() {
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId("opaque-client");
        System.out.println(registeredClient);
    }

    @Test
    public void findByClientId() {
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId("device-messaging-client");
        System.out.println(registeredClient);
    }

}