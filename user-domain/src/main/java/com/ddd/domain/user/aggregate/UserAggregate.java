package com.ddd.domain.user.aggregate;

import com.ddd.common.util.BeanUtil;
import com.ddd.domain.user.entity.command.UserCommand;
import com.ddd.domain.user.entity.model.UserModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户聚合根
 */
@Data
@Slf4j
public class UserAggregate {

    /**
     * 聚合根模型
     */
    private UserModel userModel;

    public static UserAggregate create(UserCommand userCommand) {
        validateUsernameFormat(userCommand.getUserName());

        UserAggregate userAggregate = new UserAggregate();
        userAggregate.userModel = BeanUtil.copy(userCommand, UserModel.class);
        log.info("创建聚合根 userAggregate");
        return userAggregate;
    }

    // 校验用户名格式（比如长度、特殊字符）---如有必要
    private static void validateUsernameFormat(String username) {
        log.info("基础校验：聚合根内完成用户名格式，可采用ValueObject简化");
        if (username.length() < 4 || username.length() > 20) {
            throw new IllegalArgumentException("用户名长度必须在4-20位之间");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("用户名只能包含字母、数字和下划线");
        }
    }

}
