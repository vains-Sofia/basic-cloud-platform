package com.basic.example.data.jpa;

import com.basic.example.data.jpa.domain.entity.OAuth2Application;
import com.basic.framework.data.jpa.lambda.LambdaUtils;

/**
 * LambdaUtils测试类
 *
 * @author vains
 */
public class LambdaUtilsTest {

    public static void main(String[] args) {
        String property = LambdaUtils.extractMethodToProperty(OAuth2Application::getClientId);
        System.out.println(property);
    }

}
