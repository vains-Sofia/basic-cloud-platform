package com.basic.framework.oauth2.federation.util;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.federation.domain.GiteeEmail;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@UtilityClass
public class GiteeApiHelper {

    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * GitHub 用户邮箱 API
     */
    private static final String EMAIL_API = "https://gitee.com/api/v5/emails?access_token={accessToken}";

    /**
     * 获取 GitHub 用户邮箱列表（需要 user:email scope）
     *
     * @param accessToken OAuth access token
     * @return 邮箱列表（可能为空）
     */
    public static List<GiteeEmail> fetchEmails(String accessToken) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    EMAIL_API,
                    String.class,
                    accessToken
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("请求 Gitee Email 接口失败: {}", response.getStatusCode());
                return Collections.emptyList();
            }

            return JsonUtils.toObject(response.getBody(), List.class, GiteeEmail.class);
        } catch (RestClientException e) {
            log.warn("调用 Gitee /api/v5/emails 接口失败，可能没有 email scope", e);
        } catch (Exception e) {
            log.error("解析 Gitee 邮箱响应失败", e);
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
        List<GiteeEmail> emails = fetchEmails(accessToken);
        return emails.stream()
                .filter(e -> Objects.equals("confirmed", e.getState()) && e.getScope().contains("primary"))
                .map(GiteeEmail::getEmail)
                .findFirst()
                .orElse(null);
    }
}
