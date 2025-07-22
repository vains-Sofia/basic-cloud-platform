package com.basic.framework.oauth2.authorization.server.mixin;

import com.basic.framework.oauth2.authorization.server.login.qrcode.QrCodeLoginAuthenticationToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;

/**
 * 邮件认证token反序列器
 *
 * @author vains
 */
public class QrCodeLoginAuthenticationTokenDeserializer extends JsonDeserializer<QrCodeLoginAuthenticationToken> {

    private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST = new TypeReference<>() {
    };

    private static final TypeReference<Object> OBJECT = new TypeReference<>() {
    };

    @Override
    public QrCodeLoginAuthenticationToken deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);
        // 获取是否认证属性
        boolean authenticated = readJsonNode(jsonNode, "authenticated").asBoolean();
        // 获取具体的用户信息
        JsonNode principalNode = readJsonNode(jsonNode, "principal");
        Object principal = getPrincipal(mapper, principalNode);
        // 获取权限信息
        List<GrantedAuthority> authorities = mapper.readValue(readJsonNode(jsonNode, "authorities").traverse(mapper),
                GRANTED_AUTHORITY_LIST);
        // 实例化token
        QrCodeLoginAuthenticationToken token = (!authenticated)
                ? QrCodeLoginAuthenticationToken.unauthenticated(principal)
                : QrCodeLoginAuthenticationToken.authenticated(principal, authorities);
        // 获取详情信息
        JsonNode detailsNode = readJsonNode(jsonNode, "details");
        if (detailsNode.isNull() || detailsNode.isMissingNode()) {
            token.setDetails(null);
        } else {
            Object details = mapper.readValue(detailsNode.toString(), OBJECT);
            token.setDetails(details);
        }
        return token;
    }

    private Object getPrincipal(ObjectMapper mapper, JsonNode principalNode)
            throws IOException {
        if (principalNode.isObject()) {
            return mapper.readValue(principalNode.traverse(mapper), Object.class);
        }
        return principalNode.asText();
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
