package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.SysDictTypeRequest;
import com.basic.cloud.system.api.enums.StatusEnum;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 字典测试
 *
 * @author vains
 */
@SpringBootTest
public class SystemDictTests {

    @Autowired
    private SysDictTypeClient sysDictTypeClient;

    /**
     * 测试前置方法
     */
    @BeforeEach
    public void before() {
        // 初始化认证信息
        DefaultAuthenticatedUser currentUser = new DefaultAuthenticatedUser();
        currentUser.setId(1L);
        currentUser.setName("云逸");
    }

    @Test
    public void testListAll() {
        // 测试查询所有字典类型
        sysDictTypeClient.listAll().getData().forEach(System.out::println);
    }

    @Test
    public void testCreate() {
        int count = 30;
        for (int i = 0; i < count; i++) {
            SysDictTypeRequest request = new SysDictTypeRequest();
            request.setName("测试字典类型" + (i + 1));
            request.setTypeCode("TEST" + (i + 1));
            request.setDescription("这是一个测试字典类型" + (i + 1));
            request.setStatus(StatusEnum.ENABLE);
            // 调用创建字典类型接口
            sysDictTypeClient.create(request);
        }
    }

}
