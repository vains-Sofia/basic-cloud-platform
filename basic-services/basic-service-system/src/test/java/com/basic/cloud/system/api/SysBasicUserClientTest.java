package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.framework.core.domain.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 基础用户信息feignClient测试类
 *
 * @author vains
 */
@SpringBootTest
class SysBasicUserClientTest {

    @Autowired
    private SysBasicUserClient basicUserClient;

    @BeforeEach
    public void before() {
        System.setProperty("basic.cloud.api.system.path", "/system");
    }

    @Test
    public void getByEmail() {
        Result<BasicUserResponse> responseResult = basicUserClient.getByEmail("17683906991@163.com");
        System.out.println(responseResult);
    }
}