package com.ddd.interfaces.controller.open;


import com.ddd.application.user.UserApplicationService;
import com.ddd.common.util.BeanUtil;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.query.UserQuery;
import com.ddd.sdk.entity.ResponseBase;
import com.ddd.sdk.user.api.UserOpenApi;
import com.ddd.sdk.user.entity.request.UserRequest;
import com.ddd.sdk.user.entity.response.UserResponse;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserOpenController implements UserOpenApi {

    @Resource
    private UserApplicationService userApplicationService;

    @Override
    public ResponseBase<UserResponse> queryUser(UserRequest userRequest) {
        UserQuery userQuery = BeanUtil.copy(userRequest, UserQuery.class);
        UserDTO userDTO = userApplicationService.queryUser(userQuery);
        return ResponseBase.success(BeanUtil.copy(userDTO, UserResponse.class));
    }
}
