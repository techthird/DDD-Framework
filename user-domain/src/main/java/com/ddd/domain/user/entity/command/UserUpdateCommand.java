package com.ddd.domain.user.entity.command;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateCommand extends UserCommand {

  @ApiModelProperty(value = "用户ID")
  private Integer userId;

}
