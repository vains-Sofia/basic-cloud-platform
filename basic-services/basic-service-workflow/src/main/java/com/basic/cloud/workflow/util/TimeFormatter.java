package com.basic.cloud.workflow.util;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 时间格式化工具类 - 用于将毫秒数转换为易读的时间字符串
 *
 * @author vains
 */
public class TimeFormatter {
    
    /**
     * 时间单位枚举
     */
    public enum TimeUnitLevel {
        AUTO,      // 自动选择合适的最大单位
        DAYS,      // 显示到天
        HOURS,     // 显示到小时
        MINUTES,   // 显示到分钟
        SECONDS    // 显示到秒
    }
    
    /**
     * 格式化样式
     */
    public enum FormatStyle {
        COMPACT,      // 紧凑格式：1天2小时3分钟
        FULL,         // 完整格式：1天2小时3分钟4秒
        CHINESE,      // 中文带单位：1天2小时3分钟
        COLON,        // 冒号格式：01:02:03
        HUMAN_READABLE // 人性化：1天2小时前
    }
    
    /**
     * 核心方法：将毫秒转换为时间分量
     */
    private static Map<String, Long> toTimeComponents(long milliseconds) {
        if (milliseconds < 0) {
            milliseconds = 0;
        }
        
        Map<String, Long> components = new LinkedHashMap<>();
        
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) 
                   - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) 
                     - TimeUnit.DAYS.toMinutes(days)
                     - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                     - TimeUnit.DAYS.toSeconds(days)
                     - TimeUnit.HOURS.toSeconds(hours)
                     - TimeUnit.MINUTES.toSeconds(minutes);
        long ms = milliseconds 
                - TimeUnit.DAYS.toMillis(days)
                - TimeUnit.HOURS.toMillis(hours)
                - TimeUnit.MINUTES.toMillis(minutes)
                - TimeUnit.SECONDS.toMillis(seconds);
        
        components.put("days", days);
        components.put("hours", hours);
        components.put("minutes", minutes);
        components.put("seconds", seconds);
        components.put("milliseconds", ms);
        
        return components;
    }
    
    /**
     * 方法1：智能格式化
     * 自动选择最合适的显示单位
     */
    public static String formatSmart(long milliseconds) {
        if (milliseconds <= 0) {
            return "0秒";
        }
        
        Map<String, Long> components = toTimeComponents(milliseconds);
        long days = components.get("days");
        long hours = components.get("hours");
        long minutes = components.get("minutes");
        long seconds = components.get("seconds");
        
        StringBuilder result = new StringBuilder();
        
        if (days > 0) {
            result.append(days).append("天");
            if (hours > 0) {
                result.append(hours).append("小时");
                if (minutes > 0) {
                    result.append(minutes).append("分钟");
                }
            }
        } else if (hours > 0) {
            result.append(hours).append("小时");
            if (minutes > 0) {
                result.append(minutes).append("分钟");
            }
        } else if (minutes > 0) {
            result.append(minutes).append("分钟");
            if (seconds > 0) {
                result.append(seconds).append("秒");
            }
        } else {
            result.append(seconds).append("秒");
        }
        
        return result.toString();
    }
    
    /**
     * 方法2：完整格式化（显示所有非零单位）
     */
    public static String formatFull(long milliseconds) {
        if (milliseconds <= 0) {
            return "0秒";
        }
        
        Map<String, Long> components = toTimeComponents(milliseconds);
        long days = components.get("days");
        long hours = components.get("hours");
        long minutes = components.get("minutes");
        long seconds = components.get("seconds");
        
        List<String> parts = new ArrayList<>();
        
        if (days > 0) parts.add(days + "天");
        if (hours > 0) parts.add(hours + "小时");
        if (minutes > 0) parts.add(minutes + "分钟");
        if (seconds > 0 || parts.isEmpty()) parts.add(seconds + "秒");
        
        return String.join("", parts);
    }
    
    /**
     * 方法3：定制化格式化
     * @param maxUnit 最大显示单位
     * @param minUnit 最小显示单位
     * @param style 显示样式
     */
    public static String formatCustom(long milliseconds, 
                                     TimeUnitLevel maxUnit,
                                     TimeUnitLevel minUnit,
                                     FormatStyle style) {
        if (milliseconds <= 0) {
            return switch (style) {
                case COLON -> "00:00:00";
                case HUMAN_READABLE -> "刚刚";
                default -> "0秒";
            };
        }
        
        Map<String, Long> components = toTimeComponents(milliseconds);
        long days = components.get("days");
        long hours = components.get("hours");
        long minutes = components.get("minutes");
        long seconds = components.get("seconds");
        
        // 根据最大单位调整
        if (maxUnit == TimeUnitLevel.HOURS) days = 0;
        if (maxUnit == TimeUnitLevel.MINUTES) { days = 0; hours = 0; }
        if (maxUnit == TimeUnitLevel.SECONDS) { days = 0; hours = 0; minutes = 0; }
        
        return switch (style) {
            case COMPACT -> buildCompactString(days, hours, minutes, seconds);
            case FULL -> buildFullString(days, hours, minutes, seconds);
            case CHINESE -> buildChineseString(days, hours, minutes, seconds);
            case COLON -> buildColonString(days, hours, minutes, seconds);
            case HUMAN_READABLE -> buildHumanReadable(milliseconds);
        };
    }
    
    /**
     * 方法4：流程专用的格式化（适合你的使用场景）
     */
    public static String formatForProcess(long durationInMillis) {
        if (durationInMillis <= 0) {
            return "0秒";
        }
        
        Map<String, Long> components = toTimeComponents(durationInMillis);
        long days = components.get("days");
        long hours = components.get("hours");
        long minutes = components.get("minutes");
        long seconds = components.get("seconds");
        
        // 业务流程常用格式：超过1天显示天，否则显示小时和分钟
        if (days > 0) {
            if (hours > 0) {
                return String.format("%d天%d小时", days, hours);
            }
            return String.format("%d天", days);
        }
        
        if (hours > 0) {
            if (minutes > 0) {
                return String.format("%d小时%d分钟", hours, minutes);
            }
            return String.format("%d小时", hours);
        }
        
        if (minutes > 0) {
            return String.format("%d分钟", minutes);
        }
        
        return String.format("%d秒", seconds);
    }
    
    /**
     * 方法5：使用Java 8 Duration API（推荐）
     */
    public static String formatWithDuration(long milliseconds) {
        Duration duration = Duration.ofMillis(milliseconds);
        
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        
        if (days > 0) {
            return String.format("%d天%02d小时%02d分钟", days, hours, minutes);
        }
        
        if (hours > 0) {
            return String.format("%d小时%02d分钟", hours, minutes);
        }
        
        if (minutes > 0) {
            return String.format("%d分钟%02d秒", minutes, seconds);
        }
        
        return String.format("%d秒", seconds);
    }
    
    // ========== 私有构建方法 ==========
    
    private static String buildCompactString(long d, long h, long m, long s) {
        StringBuilder sb = new StringBuilder();
        if (d > 0) sb.append(d).append("d");
        if (h > 0) sb.append(h).append("h");
        if (m > 0) sb.append(m).append("m");
        if (s > 0 || sb.isEmpty()) sb.append(s).append("s");
        return sb.toString();
    }
    
    private static String buildFullString(long d, long h, long m, long s) {
        List<String> parts = new ArrayList<>();
        if (d > 0) parts.add(d + "天");
        if (h > 0) parts.add(h + "小时");
        if (m > 0) parts.add(m + "分钟");
        if (s > 0 || parts.isEmpty()) parts.add(s + "秒");
        return String.join("", parts);
    }
    
    private static String buildChineseString(long d, long h, long m, long s) {
        if (d > 0) return d + "天" + h + "小时" + m + "分钟";
        if (h > 0) return h + "小时" + m + "分钟";
        if (m > 0) return m + "分钟" + s + "秒";
        return s + "秒";
    }
    
    private static String buildColonString(long d, long h, long m, long s) {
        if (d > 0) {
            h += d * 24;
        }
        return String.format("%02d:%02d:%02d", h, m, s);
    }
    
    private static String buildHumanReadable(long milliseconds) {
        if (milliseconds < 60_000) { // 小于1分钟
            return "刚刚";
        } else if (milliseconds < 3_600_000) { // 小于1小时
            long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
            return minutes + "分钟前";
        } else if (milliseconds < 86_400_000) { // 小于1天
            long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
            return hours + "小时前";
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
            return days + "天前";
        }
    }
    
    /**
     * 向后兼容：保持与原方法相同的签名
     */
    public static String getDate(long ms) {
        return formatForProcess(ms);
    }
}