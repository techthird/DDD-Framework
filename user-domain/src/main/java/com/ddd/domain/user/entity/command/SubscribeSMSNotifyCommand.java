package com.ddd.domain.user.entity.command;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubscribeSMSNotifyCommand {

  @ApiModelProperty(value = "用户ID")
  private Integer userId;

  @ApiModelProperty(value = "订阅时间段")
  @NotBlank(message = "订阅时间段不能为空")
  private String time;
}
