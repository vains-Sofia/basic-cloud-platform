package com.basic.framework.oauth2.storage.jpa.domain;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "oauth2_authorization_consent")
public class JpaOAuth2AuthorizationConsent extends BasicAuditorEntity {


    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "registered_client_id", nullable = false)
    private String registeredClientId;

    @NotNull
    @Size(max = 255)
    @Column(name = "principal_name", nullable = false)
    private String principalName;

    @NotNull
    @Size(max = 1000)
    @Column(name = "authorities", nullable = false, length = 1000)
    private String authorities;

}