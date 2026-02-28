package com.ddd.sdk.user.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRequest {

  @ApiModelProperty(value = "用户ID")
  private Integer userId;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "手机号")
  private String tel;

}
