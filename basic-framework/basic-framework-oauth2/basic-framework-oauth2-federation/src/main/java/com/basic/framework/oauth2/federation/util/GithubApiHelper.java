package com.basic.framework.oauth2.federation.util;

import com.basic.framework.core.constants.PlatformConstants;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.federation.domain.GithubEmail;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@UtilityClass
public class GithubApiHelper {

    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * GitHub 用户邮箱 API
     */
    private static final String EMAIL_API = "https://api.github.com/user/emails";

    /**
     * 获取 GitHub 用户邮箱列表（需要 user:email scope）
     *
     * @param accessToken OAuth access token
     * @return 邮箱列表（可能为空）
     */
    public static List<GithubEmail> fetchEmails(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.set("Accept", "application/vnd.github+json");
            headers.set("User-Agent", PlatformConstants.PLATFORM_NAME);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    EMAIL_API,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("请求 GitHub Email 接口失败: {}", response.getStatusCode());
                return Collections.emptyList();
            }

            return JsonUtils.toObject(response.getBody(), List.class, GithubEmail.class);
        } catch (RestClientException e) {
            log.warn("调用 GitHub /user/emails 接口失败，可能没有 email scope", e);
        } catch (Exception e) {
            log.error("解析 GitHub 邮箱响应失败", e);
        }
        return Collections.emptyList();
    }

    /**
     * 获取主邮箱（primary 且 verified）
     *
     * @param accessToken access_token
     * @return 主邮箱 或 null
     */
    public static String fetchPrimaryEmail(String accessToken) {
        List<GithubEmail> emails = fetchEmails(accessToken);
        return emails.stream()
                .filter(e -> Boolean.TRUE.equals(e.getPrimary()) && Boolean.TRUE.equals(e.getVerified()))
                .map(GithubEmail::getEmail)
                .findFirst()
                .orElse(null);
    }
}
