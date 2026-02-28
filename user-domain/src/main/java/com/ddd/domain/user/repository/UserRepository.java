package com.ddd.domain.user.repository;

import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.model.UserModel;
import com.ddd.domain.user.entity.query.UserPageQuery;
import com.ddd.domain.user.entity.query.UserQuery;
import com.ddd.sdk.entity.PageResult;


public interface UserRepository {

  Integer create(UserAggregate userAggregate);


  UserModel selectByUserName(String userName);

  Integer update(UserAggregate userAggregate);

  PageResult<UserDTO> selectUserPage(UserPageQuery userPageQuery);

  UserDTO selectUser(UserQuery userQuery);

  UserDTO queryUserByUserId(Integer userId);
}
