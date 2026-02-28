package com.ddd.domain.user.entity.model;

import com.ddd.sdk.exception.BusinessException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户模型
 */
@Data
@Slf4j
public class UserModel {

  /**
   * 用户ID
   */
  private Integer userId;

  /**
   * 用户名
   */
  // private UserName userName; // 🉑选：考虑封装为ValueObject，做基础校验
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

  @Getter
  @EqualsAndHashCode // 值对象：根据值比较，而不是引用
  public class UserName {

    private final String value;

    public UserName(String value) {
      log.info("基础校验：用户名格式");
      if (value.length() < 4 || value.length() > 20) {
        throw new BusinessException("用户名长度必须在4-20位之间");
      }
      if (!value.matches("^[a-zA-Z0-9_]+$")) {
        throw new BusinessException("用户名只能包含字母、数字和下划线");
      }
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }
}
