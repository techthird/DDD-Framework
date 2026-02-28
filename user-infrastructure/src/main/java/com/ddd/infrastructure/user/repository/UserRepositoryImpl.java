package com.ddd.infrastructure.user.repository;

/*import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;*/
import com.ddd.common.util.BeanUtil;
import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.model.UserModel;
import com.ddd.domain.user.entity.query.UserPageQuery;
import com.ddd.domain.user.entity.query.UserQuery;
import com.ddd.domain.user.repository.UserRepository;
import com.ddd.infrastructure.user.entity.UserDO;
import com.ddd.sdk.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

 /* @Resource
  private UserMapper userMapper;*/

  @Override
  public Integer create(UserAggregate userAggregate) {
    log.info("持久化 UserAggregate.userModel");
    UserDO userDO = BeanUtil.copy(userAggregate.getUserModel(), UserDO.class);
    // boolean result = userMapper.insert(userDO) > 0
    // mock
    boolean result = true;
    userDO.setUserId(1);

    if (!result) {
      throw new BusinessException("create user failed.");
    }
    return userDO.getUserId();
  }


  @Override
  public UserModel selectByUserName(String userName) {
    /*UserDO userDO = userMapper.selectOne(new LambdaQueryWrapper<UserDO>()
            .eq(UserDO::getUserName, userName));
    return BeanUtil.copy(userDO, UserModel.class);*/
    return null;
  }

  @Override
  public Integer update(UserAggregate userAggregate) {
    // userMapper.update
    return 0;
  }

  @Override
  public List<UserDTO> selectUserPage(UserPageQuery userPageQuery) {
    // userMapper.select
    return Collections.emptyList();
  }

  @Override
  public UserDTO selectUser(UserQuery userQuery) {
    // userMapper.select
    UserDO userDO = new UserDO();  // userMapper.selectOne(new LambdaQueryWrapper<UserDO>().eq(UserDO::getUserName, userQuery.getUserName()));
    return BeanUtil.copy(userDO, UserDTO.class);
  }

  @Override
  public UserDTO queryUserByUserId(Integer userId) {
    UserDO userDO = new UserDO(); // userMapper.selectById(userId);
    return BeanUtil.copy(userDO, UserDTO.class);
  }
}
