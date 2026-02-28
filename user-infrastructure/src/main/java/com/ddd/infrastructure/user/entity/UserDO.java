package com.ddd.infrastructure.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户DO-- 数据库映射
 */
@Data
@TableName("t_user")
public class UserDO {
  /**
   * 用户ID
   */
  @TableId(type = IdType.AUTO)
  private Integer userId;

  /**
   * 用户名
   */
  private String userName;

  /**
   * 密码
   */
  private String password;

  /**
   * 昵称
   */
  private String nickName;


  /**
   * 手机号
   */
  private String tel;

  /**
   * 年龄
   */
  private Integer age;
}
