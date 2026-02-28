package com.ddd.sdk.user.entity.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserResponse {

  @ApiModelProperty(value = "用户ID")
  private Integer userId;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "昵称")
  private String nickName;

  @ApiModelProperty(value = "手机号")
  private String tel;

  @ApiModelProperty(value = "年龄")
  private Integer age;
}
