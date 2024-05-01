package com.basic.cloud.oauth2.authorization.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * 抽象的登录认证 provider
 * 里边逻辑参考 {@link DaoAuthenticationProvider }
 *
 * @author vains
 * @see DaoAuthenticationProvider
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractLoginAuthenticationProvider implements AuthenticationProvider {

    @Getter
    private final UserDetailsService userDetailsService;

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private final UserDetailsChecker preAuthenticationChecks = new AccountStatusUserDetailsChecker();

    /**
     * 子类重写该方法以校验密码(验证码)
     * 参考{@link DaoAuthenticationProvider}  }
     *
     * @param authentication filter中生成的用户登录信息令牌
     * @throws AuthenticationException 当密码校验失败后抛出
     */
    protected abstract void additionalAuthenticationChecks(AbstractAuthenticationToken authentication) throws AuthenticationException;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(AbstractAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));
        String principal = determinePrincipal(authentication);
        UserDetails user;
        try {
            // 子类都是验证码一类的校验，所以这里先校验验证码，之后再获取用户信息
            additionalAuthenticationChecks((AbstractAuthenticationToken) authentication);
            user = retrieveUser(principal, (AbstractAuthenticationToken) authentication);
        } catch (UsernameNotFoundException ex) {
            log.debug("Failed to find user '{}'", principal);
            // 隐藏真实异常，统一抛出账号或密码错误异常
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        this.preAuthenticationChecks.check(user);

        Object principalToReturn = user;

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    /**
     * 获取用户认证凭证(邮箱/手机号/...)
     *
     * @param authentication filter中生成的用户登录信息令牌
     * @return 用户认证凭证
     */
    private String determinePrincipal(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    }

    /**
     * 检索用户信息, 子类可以重写该方法自定义获取用户信息逻辑
     * 参考{@link DaoAuthenticationProvider}  }
     *
     * @param principal      用户认证凭证
     * @param authentication filter中生成的用户登录信息令牌
     * @return 检索到的用户信息
     * @throws AuthenticationException 如果检索不到用户信息则抛出异常
     */
    protected UserDetails retrieveUser(String principal, AbstractAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(principal);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            log.debug("Failed to retrieve user '{}'", principal, ex);
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }


    /**
     * 子类需要重写该方法以完善登录成功逻辑
     *
     * @param principal      当前用户认证凭证
     * @param authentication filter中生成的用户登录信息令牌
     * @param user           用户信息
     * @return 认证成功的 Authentication 实例
     */
    protected abstract Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                                  UserDetails user);

}
