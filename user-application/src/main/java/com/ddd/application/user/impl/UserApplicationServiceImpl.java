package com.ddd.application.user.impl;

import com.ddd.application.user.UserApplicationService;
import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.cache.UserCacheService;
import com.ddd.domain.user.event.UserCreatedEvent;
import com.ddd.domain.user.event.UserUpdatedEvent;
import com.ddd.domain.user.repository.UserRepository;
import com.ddd.domain.user.entity.command.UserCreateCommand;
import com.ddd.domain.user.entity.command.UserUpdateCommand;
import com.ddd.domain.user.entity.dto.UserCreateDTO;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.command.SubscribeSMSNotifyCommand;
import com.ddd.domain.user.entity.query.UserPageQuery;
import com.ddd.domain.user.entity.query.UserQuery;

import java.util.List;
import javax.annotation.Resource;

import com.ddd.domain.user.service.UserDomainService;
import com.ddd.sdk.entity.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserApplicationServiceImpl implements UserApplicationService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserDomainService userDomainService;
    @Resource
    private UserCacheService userCacheService;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCreateDTO create(UserCreateCommand userCreateCommand) {
        log.info("用户创建请求 Application");
        // 1️⃣ 创建聚合根
        UserAggregate userAggregate = UserAggregate.create(userCreateCommand);

        // 2️⃣ 业务校验
        userDomainService.businessValidation(userAggregate);

        // 3️⃣ 业务逻辑处理
        userDomainService.logicProcess(userAggregate);

        // 4️⃣ 持久化
        Integer userId = userRepository.create(userAggregate);

        // 5️⃣ 缓存
        userCacheService.saveCache(userAggregate);

        // 6️⃣ 发送领域事件
        eventPublisher.publishEvent(new UserCreatedEvent(this, userId));

        log.info("End 用户创建成功 userId: {}", userId);
        return UserCreateDTO.builder()
                .userId(userId)
                .nickName(userAggregate.getUserModel().getNickName())
                .userName(userAggregate.getUserModel().getUserName())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateCommand userUpdateCommand) {

        // 流程类似 create

        // 1️⃣ 创建聚合根
        UserAggregate userAggregate = UserAggregate.create(userUpdateCommand);

        // 2️⃣ 业务校验
        userDomainService.businessValidation(userAggregate);

        // 3️⃣ 业务逻辑处理
        userDomainService.logicProcess(userAggregate);

        // 4️⃣ 持久化
        Integer userId = userRepository.update(userAggregate);

        // 5️⃣ 缓存
        userCacheService.saveCache(userAggregate);

        // 6️⃣ 发送领域事件
        eventPublisher.publishEvent(new UserUpdatedEvent(this, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscribeSMSNotify(SubscribeSMSNotifyCommand subscribeSMSNotifyCommand) {
        userDomainService.subscribeSMSNotify(subscribeSMSNotifyCommand);
    }

    @Override
    public PageResult<UserDTO> queryUserPage(UserPageQuery userPageQuery) {
        return userRepository.selectUserPage(userPageQuery);
    }

    @Override
    public UserDTO queryUser(UserQuery userQuery) {
        return userRepository.selectUser(userQuery);
    }

    @Override
    public UserDTO queryUserByUserId(Integer userId) {
        return userRepository.queryUserByUserId(userId);
    }

}
