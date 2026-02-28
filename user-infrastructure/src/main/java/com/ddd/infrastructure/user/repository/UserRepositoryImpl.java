package com.ddd.infrastructure.user.repository;

/*import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;*/
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddd.common.util.BeanUtil;
import com.ddd.domain.user.aggregate.UserAggregate;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.model.UserModel;
import com.ddd.domain.user.entity.query.UserPageQuery;
import com.ddd.domain.user.entity.query.UserQuery;
import com.ddd.domain.user.repository.UserRepository;
import com.ddd.infrastructure.user.entity.UserDO;
import com.ddd.infrastructure.user.mapper.UserMapper;
import com.ddd.sdk.entity.PageResult;
import com.ddd.sdk.exception.BusinessException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

  @Resource
  private UserMapper userMapper;

  @Override
  public Integer create(UserAggregate userAggregate) {
    log.info("持久化 UserAggregate.userModel");
    UserDO userDO = BeanUtil.copy(userAggregate.getUserModel(), UserDO.class);
    boolean result = userMapper.insert(userDO) > 0;

    if (!result) {
      throw new BusinessException("create user failed.");
    }
    return userDO.getUserId();
  }


  @Override
  public UserModel selectByUserName(String userName) {
    UserDO userDO = userMapper.selectOne(new LambdaQueryWrapper<UserDO>()
            .eq(UserDO::getUserName, userName));
    return BeanUtil.copy(userDO, UserModel.class);
  }

  @Override
  public Integer update(UserAggregate userAggregate) {
    UserDO userDO = BeanUtil.copy(userAggregate.getUserModel(), UserDO.class);
    return userMapper.updateById(userDO);
  }

  @Override
  public PageResult<UserDTO> selectUserPage(UserPageQuery query) {
    Page<UserDO> page = new Page<>(query.getCurrentPage(), query.getPageSize());
    LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
            .like(StringUtils.isNotBlank(query.getNickName()), UserDO::getNickName, query.getNickName())
            .like(StringUtils.isNotBlank(query.getUserName()), UserDO::getUserName, query.getUserName())
            .like(StringUtils.isNotBlank(query.getTel()), UserDO::getTel, query.getTel())
            .like(query.getUserId() != null, UserDO::getUserId, query.getUserId())
            .orderByDesc(UserDO::getUserId);
    IPage<UserDO> result = userMapper.selectPage(page, wrapper);

    return PageResult.<UserDTO>builder()
            .totalCount(result.getTotal())
            .totalPage(result.getPages())
            .data(BeanUtil.copyList(result.getRecords(), UserDTO.class))
            .build();
  }

  @Override
  public UserDTO selectUser(UserQuery userQuery) {
    UserDO userDO = userMapper.selectOne(new LambdaQueryWrapper<UserDO>()
            .eq(userQuery.getUserId() != null, UserDO::getUserId, userQuery.getUserId())
            .eq(StringUtils.isNotBlank(userQuery.getUserName()), UserDO::getUserName, userQuery.getUserName())
            .eq(StringUtils.isNotBlank(userQuery.getTel()), UserDO::getTel, userQuery.getTel())
            .last("limit 1"));
    return BeanUtil.copy(userDO, UserDTO.class);
  }

  @Override
  public UserDTO queryUserByUserId(Integer userId) {
    UserDO userDO = userMapper.selectById(userId);
    return BeanUtil.copy(userDO, UserDTO.class);
  }
}
