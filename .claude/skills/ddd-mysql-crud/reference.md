# 四类方法各层代码参考（项目真实代码）

表结构参考：**t_user**（主键 `user_id` → Integer）。以下引用均来自本项目真实实现。

**表结构**：

```sql
create table t_user (
    user_id INT not null auto_increment COMMENT '用户ID',
    user_name VARCHAR(50) not null COMMENT '用户名',
    password VARCHAR(255) not null COMMENT '密码',
    nick_name VARCHAR(50) COMMENT '昵称',
    tel VARCHAR(20) COMMENT '手机号',
    age INT COMMENT '年龄',
    primary key (user_id),
    unique key uk_user_name (user_name),
    unique key uk_tel (tel)
) engine = InnoDB default CHARSET = utf8mb4 COMMENT = '用户表';
```

---

## 1. 创建 (create)

**Controller** — `user-interface/.../UserController.java`

```java
import org.springframework.validation.annotation.Validated;

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
}
```

**ApplicationService 接口** — `user-application/.../UserApplicationService.java`

```java
UserCreateDTO create(UserCreateCommand userCreateCommand);
```

**ApplicationService 实现** — `user-application/.../impl/UserApplicationServiceImpl.java`

```java
@Override
@Transactional(rollbackFor = Exception.class)
public UserCreateDTO create(UserCreateCommand userCreateCommand) {
    // 1️⃣ 创建聚合根
    UserAggregate userAggregate = UserAggregate.create(userCreateCommand);
    // 2️⃣ 业务校验
    userDomainService.businessValidation(userAggregate);
    // 3️⃣ 业务逻辑处理
    userDomainService.logicProcess(userAggregate);
    // 4️⃣ 持久化
    Integer userId = userRepository.create(userAggregate);
    // 5️⃣ 缓存
    userCacheService.saveCache(userAggregate);
    // 6️⃣ 发送领域事件
    eventPublisher.publishEvent(new UserCreatedEvent(this, userId));
    return UserCreateDTO.builder()
            .userId(userId)
            .nickName(userAggregate.getUserModel().getNickName())
            .userName(userAggregate.getUserModel().getUserName())
            .build();
}
```

**Repository 接口** — `user-domain/.../UserRepository.java`

```java
Integer create(UserAggregate userAggregate);
```

**Repository 实现** — `user-infrastructure/.../UserRepositoryImpl.java`

```java
@Override
public Integer create(UserAggregate userAggregate) {
    UserDO userDO = BeanUtil.copy(userAggregate.getUserModel(), UserDO.class);
    // userMapper.insert(userDO); 实际项目用 Mapper 插入
    // boolean result = userMapper.insert(userDO) > 0;
    if (!result) {
        throw new BusinessException("create user failed.");
    }
    return userDO.getUserId();
}
```

---

## 2. 修改 (update)

**Controller** — `UserController.java`

```java
@ApiOperation(value = "用户修改")
@PostMapping("/update")
public ResponseBase<Void> update(@RequestBody @Validated UserUpdateCommand userUpdateCommand) {
    userApplicationService.update(userUpdateCommand);
    return ResponseBase.success();
}
```

**ApplicationService**

```java
void update(UserUpdateCommand userUpdateCommand);
// 实现：聚合根.create → businessValidation → logicProcess → repository.update → 缓存 → publishEvent(UserUpdatedEvent)
```

**ApplicationServiceImpl 片段** — `UserApplicationServiceImpl.java`

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void update(UserUpdateCommand userUpdateCommand) {
    UserAggregate userAggregate = UserAggregate.create(userUpdateCommand);
    userDomainService.businessValidation(userAggregate);
    userDomainService.logicProcess(userAggregate);
    Integer userId = userRepository.update(userAggregate);
    userCacheService.saveCache(userAggregate);
    eventPublisher.publishEvent(new UserUpdatedEvent(this, userId));
}
```

**Repository**

```java
Integer update(UserAggregate userAggregate);
// 实现：BeanUtil.copy(aggregate.getUserModel(), UserDO.class) → mapper.updateById(do) → return do.getUserId()
```

---

## 3. 分页查询 (queryUserPage)

**Controller** — `UserController.java`

```java
@ApiOperation(value = "分页查询用户信息")
@PostMapping("/queryUserPage")
public ResponseBase<PageResult<UserDTO>> queryUserPage(@RequestBody @Validated UserPageQuery userPageQuery) {
    return ResponseBase.success(userApplicationService.queryUserPage(userPageQuery));
}
```

**ApplicationService** — `UserApplicationService.java`

```java
PageResult<UserDTO> queryUserPage(UserPageQuery userPageQuery);
```

**ApplicationServiceImpl** — `UserApplicationServiceImpl.java`

```java
@Override
public PageResult<UserDTO> queryUserPage(UserPageQuery userPageQuery) {
    return userRepository.queryUserPage(userPageQuery);
}
```

**Repository 接口** — `UserRepository.java`

```java
PageResult<UserDTO> queryUserPage(UserPageQuery userPageQuery);
```

**Repository 实现** — `UserRepositoryImpl.java`

```java
@Override
public PageResult<UserDTO> queryUserPage(UserPageQuery userPageQuery) {
    // Page<UserDO> page = new Page<>(userPageQuery.getCurrentPage(), userPageQuery.getPageSize());
    // QueryWrapper 拼条件 → userMapper.selectPage(page, wrapper)
    //IPage<UserDO> result = userMapper.selectPage(page, wrapper);
    return PageResult.<PaymentOrderDTO>builder() // 当前项目为占位
            .totalCount(result.getTotal())
            .totalPage(result.getPages())
            .data(BeanUtil.copyList(result.getRecords(), UserDO.class))
            .build();
}
```

---

## 4. 单条查询 (queryUser)

**Controller** — `UserController.java`

```java
@ApiOperation(value = "查询单用户信息")
@PostMapping("/queryUser")
public ResponseBase<UserDTO> queryUser(@RequestBody @Validated UserQuery userQuery) {
    return ResponseBase.success(userApplicationService.queryUser(userQuery));
}
```

**ApplicationService** — `UserApplicationService.java`

```java
UserDTO queryUser(UserQuery userQuery);
```

**ApplicationServiceImpl** — `UserApplicationServiceImpl.java`

```java
@Override
public UserDTO queryUser(UserQuery userQuery) {
    return userRepository.queryUser(userQuery);
}
```

**Repository 接口** — `UserRepository.java`

```java
UserDTO queryUser(UserQuery userQuery);
```

**Repository 实现** — `UserRepositoryImpl.java`

```java
@Override
public UserDTO queryUser(UserQuery userQuery) {
    // QueryWrapper 拼条件 → userMapper.selectOne(wrapper)
    // return BeanUtil.copy(userDO, UserDTO.class);
    return new UserDTO(); // 当前项目为占位
}
```

---

## 5. 根据 ID 查询 (queryUserByUserId)

**Controller** — `UserController.java`

```java
@ApiOperation(value = "根据用户ID查询用户信息")
@GetMapping("/queryUserByUserId")
public ResponseBase<UserDTO> queryUserByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") Integer userId) {
    return ResponseBase.success(userApplicationService.queryUserByUserId(userId));
}
```

**ApplicationService** — `UserApplicationService.java`

```java
UserDTO queryUserByUserId(Integer userId);
```

**ApplicationServiceImpl** — `UserApplicationServiceImpl.java`

```java
@Override
public UserDTO queryUserByUserId(Integer userId) {
    return userRepository.queryUserByUserId(userId);
}
```

**Repository 接口** — `UserRepository.java`

```java
UserDTO queryUserByUserId(Integer userId);
```

**Repository 实现** — `UserRepositoryImpl.java`

```java
@Override
public UserDTO queryUserByUserId(Integer userId) {
    UserDO userDO = new UserDO(); // 实际：userMapper.selectById(userId);
    return BeanUtil.copy(userDO, UserDTO.class);
}
```

---

## 类型与注解速查（与项目一致）

- **Controller**：使用 `@Api`、`@Slf4j`、`@Resource`；创建方法记录 `log.info("Start xxx请求 Interfaces")`。见 `interfaces/.../UserController.java`。
- **Command**：`UserCommand` 使用 @Data；字段 @NotBlank；Create 无 userId，Update 有 userId。见 `domain/.../entity/command/UserCommand.java`、`UserUpdateCommand.java`。
- **PageQuery**：`UserPageQuery` extends `PageBase`（currentPage、pageSize）；业务字段 userId、userName、nickName、tel。见 `domain/.../entity/query/UserPageQuery.java`。
- **Query**：`UserQuery` 单条查询条件，含 userId、userName、tel 等。见 `domain/.../entity/query/UserQuery.java`。
- **DO**：`UserDO` 与表 t_user 字段 1:1（userId、userName、password、nickName、tel、age）。见 `infrastructure/.../entity/UserDO.java`。
- **Mapper**：`UserMapper` 继承 `BaseMapper<UserDO>`。见 `infrastructure/.../mapper/UserMapper.java`。
- **聚合根**：`UserAggregate.create(UserCommand)` 内使用 `BeanUtil.copy(command, UserModel.class)`。见 `domain/.../aggregate/UserAggregate.java`。
- **方法命名**：查询方法统一使用 `queryXxxPage`、`queryXxx`、`queryXxxByXxxId` 格式。见 `UserApplicationService.java`、`UserRepository.java`。
