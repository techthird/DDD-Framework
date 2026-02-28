package com.ddd.infrastructure.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.ddd.infrastructure.user.entity.UserDO;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}


