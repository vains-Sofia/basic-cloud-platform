package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FilePreSignedRequest;
import com.basic.cloud.system.api.domain.response.FilePreSignedResponse;
import com.basic.cloud.system.property.StorageProperty;
import com.basic.cloud.system.service.FileService;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.oauth2.core.util.ServletUtils;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 文件相关操作minio实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioClientServiceImpl implements FileService {

    private final MinioClient minioClient;

    private final StorageProperty storageProperty;

    @Override
    public FilePreSignedResponse filePreSigned(FilePreSignedRequest request) {
        try {
            HttpServletRequest httpRequest = ServletUtils.getRequest();
            if (httpRequest == null) {
                log.error("获取HttpServletRequest失败");
                throw new CloudServiceException("获取HttpServletRequest失败.");
            }

            String bucket;
            if (ObjectUtils.isEmpty(request.getBucket())) {
                bucket = storageProperty.getBucket();
            } else {
                bucket = request.getBucket();
            }
            // 检测bucket是否存在
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                // 创建桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            // 根据当前请求的HttpMethod来判断是上传还是下载还是删除
            HttpMethod httpMethod = HttpMethod.valueOf(httpRequest.getMethod());
            Method method;
            if (httpMethod == HttpMethod.PUT) {
                // 上传文件
                method = Method.PUT;
            } else if (httpMethod == HttpMethod.DELETE) {
                // 删除文件
                method = Method.DELETE;
            } else if (httpMethod == HttpMethod.GET) {
                // 下载文件
                method = Method.GET;
            } else {
                log.error("不支持的请求方法: {}", httpMethod);
                throw new CloudServiceException("不支持的请求方法: " + httpMethod);
            }

            // 根据传入的桶和文件名组装参数
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(method)
                    .object(request.getName())
                    // 默认7天有效期(最大7天)
                    .expiry(request.getExpireTimes() == null ? 60 * 60 * 24 * 7 : request.getExpireTimes())
                    .bucket(ObjectUtils.isEmpty(bucket) ? storageProperty.getBucket() : bucket)
                    .build();
            // 生成预签名地址
            String preSignedUrl = minioClient.getPresignedObjectUrl(args);

            // 组装返回参数
            FilePreSignedResponse filePreSignedResponse = new FilePreSignedResponse();
            filePreSignedResponse.setName(request.getName());
            filePreSignedResponse.setUrl(preSignedUrl);
            filePreSignedResponse.setBucket(bucket);
            return filePreSignedResponse;
        } catch (Exception e) {
            log.error("获取预签名失败，原因：{}", e.getMessage(), e);
            throw new CloudServiceException(e.getMessage());
        }
    }
}
