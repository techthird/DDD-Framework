package com.ddd.application.user;

import com.ddd.domain.user.entity.command.UserCreateCommand;
import com.ddd.domain.user.entity.command.UserUpdateCommand;
import com.ddd.domain.user.entity.dto.UserCreateDTO;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.command.SubscribeSMSNotifyCommand;
import com.ddd.domain.user.entity.query.UserPageQuery;
import com.ddd.domain.user.entity.query.UserQuery;
import java.util.List;


public interface UserApplicationService {

  UserCreateDTO create(UserCreateCommand userCreateCommand);

  void update(UserUpdateCommand userUpdateCommand);

  void subscribeSMSNotify(SubscribeSMSNotifyCommand subscribeSMSNotifyCommand);

  List<UserDTO> queryUserPage(UserPageQuery userPageQuery);

  UserDTO queryUser(UserQuery userQuery);

  UserDTO queryUserByUserId(Integer userId);


}
