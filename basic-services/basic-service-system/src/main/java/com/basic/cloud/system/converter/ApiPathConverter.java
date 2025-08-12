package com.basic.cloud.system.converter;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * API路径转换器，用于将API路径转换为通配路径和权限码。
 * 主要功能包括：
 * 1. 将路径中的占位符（如 {tenantId}）替换为通配符（*），生成通配路径。
 * 2. 从路径中提取有意义的段落，生成权限码，格式为 module:resource:action。
 *
 * @author vains
 */
@UtilityClass
public class ApiPathConverter {

    /**
     * 将路径中所有 {xxx} 替换为 *，用于生成通配路径。
     *
     * @param path 原始路径，如 /tenant/{tenantId}/user/{id}/reset
     * @return 通配路径
     */
    public static String toWildcardPath(String path) {
        if (path == null) return "";
        return path.replaceAll("\\{[^/]+?}", "*");
    }

    /**
     * 从路径生成权限码，格式为 module:resource:action（提取非占位的三个关键段）
     *
     * @param path 原始路径
     * @return 权限码，如 tenant:user:reset
     */
    public static String toPermissionCode(String path) {
        if (path == null || path.isEmpty()) return "";

        String[] segments = path.split("/");
        List<String> meaningful = new ArrayList<>();

        for (String segment : segments) {
            if (!segment.isEmpty() && !segment.startsWith("{")) {
                meaningful.add(segment);
            }
        }

        if (meaningful.isEmpty()) {
            return "";
        }

        if (meaningful.size() == 1) {
            return meaningful.getFirst();
        } else if (meaningful.size() == 2) {
            return meaningful.get(0) + ":" + meaningful.get(1);
        } else {
            return meaningful.get(0) + ":" + meaningful.get(1) + ":" + meaningful.getLast();
        }
    }

}
