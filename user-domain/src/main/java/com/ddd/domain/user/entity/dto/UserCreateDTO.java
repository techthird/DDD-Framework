package com.ddd.domain.user.entity.dto;

import com.ddd.domain.user.entity.model.UserModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "用户名")
    private String userName;
}
