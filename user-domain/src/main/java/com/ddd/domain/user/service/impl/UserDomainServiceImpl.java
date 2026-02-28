package com.ddd.domain.user.service.impl;

import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.entity.command.SubscribeSMSNotifyCommand;
import com.ddd.domain.user.entity.model.UserModel;
import com.ddd.domain.user.repository.UserRepository;
import com.ddd.domain.user.service.UserDomainService;
import com.ddd.sdk.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserDomainServiceImpl  implements UserDomainService {

    @Resource
    private UserRepository userRepository;

    @Override
    public void businessValidation(UserAggregate userAggregate) {
        log.info("业务校验 UserAggregate");
        UserModel userModel = userRepository.selectByUserName(userAggregate.getUserModel().getUserName());
        if (userModel != null) {
            throw new BusinessException("用户名已存在");
        }
        // 其他业务校验
    }

    @Override
    public void logicProcess(UserAggregate userAggregate) {
        log.info("详细业务逻辑处理 UserAggregate");
        // 积分逻辑
        // 等级逻辑----可增加策略设计模式实现不同平台之间的差异化需求

    }

    @Override
    public void subscribeSMSNotify(SubscribeSMSNotifyCommand subscribeSMSNotifyCommand) {
        // 业务逻辑
    }
}
