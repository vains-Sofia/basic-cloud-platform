package com.basic.example.data.jpa.repository;

import com.basic.example.data.jpa.domain.entity.OAuth2Application;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * oauth2客户端repository
 *
 * @author vains
 */
public interface OAuth2ApplicationRepository extends CrudRepository<OAuth2Application, Long>, JpaSpecificationExecutor<OAuth2Application> {

}
