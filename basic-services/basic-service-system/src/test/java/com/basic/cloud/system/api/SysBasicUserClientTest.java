package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
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

    @Test
    public void getByEmail() {
        Result<BasicUserResponse> responseResult = basicUserClient.getByEmail("17683906991@163.com");
        System.out.println(responseResult);
    }

    @Test
    public void findByPage() {
        FindBasicUserPageRequest request = new FindBasicUserPageRequest();
        request.setSize(10L);
        request.setCurrent(1L);
        Result<PageResult<FindBasicUserResponse>> basicUserByPage = basicUserClient.findByPage(request);
        System.out.println(basicUserByPage);
    }

    @Test
    public void userDetails() {
        Result<BasicUserResponse> userResponseResult = basicUserClient.userDetails(1L);
        System.out.println(userResponseResult);
    }
}