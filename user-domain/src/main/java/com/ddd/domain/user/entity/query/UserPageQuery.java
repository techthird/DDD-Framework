package com.ddd.domain.user.entity.query;

import com.ddd.sdk.entity.PageBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPageQuery extends PageBase {

  @ApiModelProperty(value = "用户ID")
  private Integer userId;

  @ApiModelProperty(value = "用户名")
  private String userName;

  @ApiModelProperty(value = "昵称")
  private String nickName;

  @ApiModelProperty(value = "手机号")
  private String tel;

}
