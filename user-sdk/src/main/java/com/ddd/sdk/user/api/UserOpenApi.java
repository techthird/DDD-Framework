package com.ddd.sdk.user.api;

import com.ddd.sdk.entity.ResponseBase;
import com.ddd.sdk.user.entity.request.UserRequest;
import com.ddd.sdk.user.entity.response.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "用户服务开放API")
// 启用Feign客户端，对外提供接口，需POM引入依赖
// <dependency>
//    <groupId>org.springframework.cloud</groupId>
//    <artifactId>spring-cloud-starter-openfeign</artifactId>
//    <version>2.1.1.RELEASE</version>
// </dependency>
//@FeignClient(value = "ddd-service", contextId = "UserApi")
@RequestMapping("/open/user")
public interface UserOpenApi {

    @ApiOperation(value = "查询单用户信息")
    @PostMapping("/queryUser")
    ResponseBase<UserResponse> queryUser(@RequestBody UserRequest userRequest);

}
