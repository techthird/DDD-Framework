package com.ddd.domain.user.service;

import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.entity.command.SubscribeSMSNotifyCommand;

public interface UserDomainService {

    /**
     * 业务校验
     *
     * @param userAggregate
     */
    void businessValidation(UserAggregate userAggregate);

    void logicProcess(UserAggregate userAggregate);

    void subscribeSMSNotify(SubscribeSMSNotifyCommand subscribeSMSNotifyCommand);
}
