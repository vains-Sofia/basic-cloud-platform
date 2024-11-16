package com.basic.example.data.jpa.domain.entity;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

/**
 * oauth2客户端 entity
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "oauth2_application")
@EqualsAndHashCode(callSuper = true)
public class OAuth2Application extends BasicAuditorEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "client_id_issued_at", nullable = false)
    private LocalDateTime clientIdIssuedAt;

    @Size(max = 255)
    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @Size(max = 255)
    @NotNull
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Size(max = 255)
    @Column(name = "client_logo")
    private String clientLogo;

    @Size(max = 1000)
    @NotNull
    @Column(name = "client_authentication_methods", nullable = false, length = 1000)
    private String clientAuthenticationMethods;

    @Size(max = 1000)
    @NotNull
    @Column(name = "authorization_grant_types", nullable = false, length = 1000)
    private String authorizationGrantTypes;

    @Size(max = 1000)
    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    @Size(max = 1000)
    @Column(name = "post_logout_redirect_uris", length = 1000)
    private String postLogoutRedirectUris;

    @Size(max = 1000)
    @NotNull
    @Column(name = "scopes", nullable = false, length = 1000)
    private String scopes;

    @Size(max = 2000)
    @NotNull
    @Column(name = "client_settings", nullable = false, length = 2000)
    private String clientSettings;

    @Size(max = 2000)
    @NotNull
    @Column(name = "token_settings", nullable = false, length = 2000)
    private String tokenSettings;

}