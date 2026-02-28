package com.ddd.interfaces.controller.user;


import com.ddd.application.user.UserApplicationService;
import com.ddd.sdk.entity.ResponseBase;
import com.ddd.domain.user.entity.command.UserCreateCommand;
import com.ddd.domain.user.entity.command.UserUpdateCommand;
import com.ddd.domain.user.entity.dto.UserCreateDTO;
import com.ddd.domain.user.entity.dto.UserDTO;
import com.ddd.domain.user.entity.command.SubscribeSMSNotifyCommand;
import com.ddd.domain.user.entity.query.UserPageQuery;
import com.ddd.domain.user.entity.query.UserQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import javax.annotation.Resource;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户服务API")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserApplicationService userApplicationService;

    @ApiOperation(value = "用户创建")
    @PostMapping("/create")
    public ResponseBase<UserCreateDTO> create(@RequestBody @Validated UserCreateCommand userCreateCommand) {
        log.info("Start 用户创建请求 Interfaces");
        return ResponseBase.success(userApplicationService.create(userCreateCommand));
    }

    @ApiOperation(value = "用户修改")
    @PostMapping("/update")
    public ResponseBase<Void> update(@RequestBody @Validated UserUpdateCommand userUpdateCommand) {
        userApplicationService.update(userUpdateCommand);
        return ResponseBase.success();
    }

    @ApiOperation(value = "订阅短信通知")
    @PostMapping("/subscribeSMSNotify")
    public ResponseBase<Void> subscribeSMSNotify(@RequestBody @Validated SubscribeSMSNotifyCommand subscribeSMSNotifyCommand) {
        userApplicationService.subscribeSMSNotify(subscribeSMSNotifyCommand);
        return ResponseBase.success();
    }

    @ApiOperation(value = "分页查询用户信息")
    @PostMapping("/queryUserPage")
    public ResponseBase<List<UserDTO>> queryUserPage(@RequestBody @Validated UserPageQuery userPageQuery) {
        return ResponseBase.success(userApplicationService.queryUserPage(userPageQuery));
    }

    @ApiOperation(value = "查询单用户信息")
    @PostMapping("/queryUser")
    public ResponseBase<UserDTO> queryUser(@RequestBody @Validated UserQuery userQuery) {
        return ResponseBase.success(userApplicationService.queryUser(userQuery));
    }

    @ApiOperation(value = "根据用户ID查询用户信息")
    @GetMapping("/queryUserByUserId")
    public ResponseBase<UserDTO> queryUserByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") Integer userId) {
        return ResponseBase.success(userApplicationService.queryUserByUserId(userId));
    }
}
