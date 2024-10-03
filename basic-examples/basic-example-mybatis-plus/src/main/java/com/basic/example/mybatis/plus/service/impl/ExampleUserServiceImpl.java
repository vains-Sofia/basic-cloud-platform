package com.basic.example.mybatis.plus.service.impl;

import com.basic.example.mybatis.plus.entity.ExampleUser;
import com.basic.example.mybatis.plus.mapper.ExampleUserMapper;
import com.basic.example.mybatis.plus.service.IExampleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vains
 * @since 2024-05-15
 */
@Service
public class ExampleUserServiceImpl extends ServiceImpl<ExampleUserMapper, ExampleUser> implements IExampleUserService {

}
