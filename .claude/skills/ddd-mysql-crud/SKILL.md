---
name: ddd-mysql-crud
description: 根据 MySQL 表结构按本 DDD 项目分层规范生成完整代码，包含创建、修改、分页查询、根据 ID 查询。在用户提供 CREATE TABLE 或表结构、或要求“按表生成 DDD 各层代码”时使用。
---

# 根据 MySQL 表结构生成 DDD 各层代码

本技能约定：根据 **MySQL 表结构**，按项目既有 DDD 分层（接口层 → 应用层 → 领域层 ← 基础设施层），生成 **创建、修改、分页查询、根据 ID 查询** 四类能力对应的全部层级代码。表名与包名/类名转换、各层职责与依赖关系以当前项目（user-* 模块）为唯一参考。

## 一、前置约定

### 1.1 表与领域命名

- **表名**：如 `t_user`，去掉 `t_` 前缀得到 **snake_case** 名 `user`。
- **领域名**：将 snake_case 转为 **PascalCase** 作为“实体名”，如 `User`；包/模块可用短名，如 `user`（与表名一致）。
- **主键**：表内 `user_id int AUTO_INCREMENT` 对应 Java **Integer**（本项目以 user 为参考，主键为 Integer）；若表为 `id bigint` 则用 Long。其它字段类型映射见下。

### 1.2 MySQL → Java 类型映射

| MySQL 类型 | Java 类型 |
|------------|-----------|
| bigint | Long（或 Integer） |
| int / tinyint / smallint | Integer |
| decimal(m,n) | BigDecimal |
| varchar / char | String |
| datetime / timestamp | java.util.Date 或 LocalDateTime（与项目一致） |
| text / longtext | String |

### 1.3 项目分层与模块对应

- **user-base**：PageBase、ResponseBase、ErrorCodeEnum、BusinessException（不按表生成，直接复用）。
- **user-common**：BeanUtil、全局配置（不按表生成）。
- **领域层**：`user-domain` → 新表无需新建模块，在当前模块中添加，包名 `com.ddd.domain.payment`。
- **基础设施层**：`user-infrastructure` → 包名 `com.ddd.infrastructure.payment`。
- **应用层**：`user-application` → 包名 `com.ddd.application.payment`。
- **接口层**：`user-interface` → 包名 `com.ddd.interfaces.controller.payment`。

若新表归属已有限界上下文（如仍放在 user 下），则包名保持 `com.ddd.domain.user` 等，仅类名改为新实体名（如 PaymentOrder）。

---

## 二、必须生成的四类能力

1. **创建**：接口入参 → CreateCommand → 应用层 → 聚合根 → 领域校验/逻辑 → 仓储 create → 事件（可选）→ 返回 CreateDTO。
2. **修改**：接口入参 → UpdateCommand（含主键 id）→ 应用层 → 聚合根 → 领域校验/逻辑 → 仓储 update。
3. **分页查询**：接口入参 → PageQuery（继承 PageBase）→ 应用层 → 仓储分页查询 → 返回 List&lt;EntityDTO&gt;。
4. **根据 ID 查询**：接口入参为主键 id → 应用层 → 仓储 getById → 返回 EntityDTO。

---

## 三、各层生成清单与规范

### 3.1 领域层 (domain)

**路径与包**：`{module}-domain`，包 `com.ddd.domain.{context}`（如 `com.ddd.domain.payment`）。

| 类型 | 路径/类名 | 说明 |
|------|------------|------|
| 命令基类 | entity/command/{Entity}Command.java | 创建/修改共有字段（不含 id）；带 @NotBlank 等校验 |
| 创建命令 | entity/command/{Entity}CreateCommand.java | extends {Entity}Command，无 id |
| 修改命令 | entity/command/{Entity}UpdateCommand.java | extends {Entity}Command，增加 id 字段（主键） |
| 领域模型 | entity/model/{Entity}Model.java | 与 DO 字段对齐，供聚合根使用 |
| DTO | entity/dto/{Entity}DTO.java | 查询出参，字段与表业务字段一致 |
| 创建出参 | entity/dto/{Entity}CreateDTO.java | 创建后返回，至少含 id 及关键业务字段 |
| 分页查询 | entity/query/{Entity}PageQuery.java | extends PageBase，加表内可筛字段 |
| 条件查询 | entity/query/{Entity}Query.java | 单条查询条件（含 id 等） |
| 聚合根 | aggregate/{Entity}Aggregate.java | 持有一个 {Entity}Model；静态方法 create(Command) 用 BeanUtil.copy 转 Model |
| 仓储接口 | repository/{Entity}Repository.java | create、update、queryXxxPage、queryXxx、queryXxxByXxxId 等 |
| 领域服务接口 | service/{Entity}DomainService.java | businessValidation(aggregate)、logicProcess(aggregate) |
| 领域服务实现 | service/impl/{Entity}DomainServiceImpl.java | 实现校验与逻辑，可注入 Repository 查重等 |
| 领域事件 | event/{Entity}CreatedEvent.java、event/{Entity}UpdatedEvent.java | 继承 ApplicationEvent，构造含 source 与 id |

**规范要点**：

- Command/DTO/Model/Query 使用 Lombok `@Data`；CreateDTO 可用 `@Builder`。
- PageQuery 继承 `com.ddd.sdk.entity.PageBase`（currentPage、pageSize）。
- 聚合根内只做轻量校验（如格式），重复性、存在性等放 DomainService。
- *Controller类，@Validated注解，包导入 `import org.springframework.validation.annotation.Validated;`

### 3.2 基础设施层 (infrastructure)

**路径与包**：`{module}-infrastructure`，包 `com.ddd.infrastructure.{context}`。

| 类型 | 路径/类名 | 说明 |
|------|------------|------|
| 数据对象 | entity/{Entity}DO.java | 与表结构 1:1，主键 id；可用 MyBatis-Plus @TableName、@TableId |
| Mapper | mapper/{Entity}Mapper.java | 继承 BaseMapper&lt;{Entity}DO&gt;，@Mapper |
| 仓储实现 | repository/{Entity}RepositoryImpl.java | @Repository；注入 Mapper；create/update 用 BeanUtil 将 Model 转 DO；分页用 MyBatis-Plus Page；queryById 用 Mapper.selectById；查询出参用 BeanUtil.copy(DO, DTO) 或 copyList |

**规范要点**：

- DO 字段与表一致（驼峰）；分页可用 `new Page<>(query.getCurrentPage(), query.getPageSize())` 与 QueryWrapper 条件。
- 只依赖 domain 与 mybatis-plus；不暴露 DO 到应用层。

### 3.3 应用层 (application)

**路径与包**：`{module}-application`，包 `com.ddd.application.{context}`。

| 类型 | 路径/类名 | 说明 |
|------|------------|------|
| 应用服务接口 | {Entity}ApplicationService.java | create( CreateCommand )、update( UpdateCommand )、queryXxxPage( PageQuery )、queryXxx( Query )、queryXxxByXxxId( id ) |
| 应用服务实现 | impl/{Entity}ApplicationServiceImpl.java | @Service；注入 Repository、DomainService、ApplicationEventPublisher；create/update 内：聚合根.create → businessValidation → logicProcess → repository.create/update → 发 Created/Updated 事件；queryXxxPage/queryXxx/queryXxxByXxxId 直接调 repository |

**规范要点**：

- create/update 方法加 `@Transactional(rollbackFor = Exception.class)`。
- 查询方法命名与 Controller 保持一致，如 `queryUserPage`、`queryUser`、`queryUserByUserId`。
- 查询方法只做编排，不写业务规则。

### 3.4 接口层 (interface)

**路径与包**：`{module}-interface`，包 `com.ddd.interfaces.controller.{context}`。

| 类型 | 路径/类名 | 说明 |
|------|------------|------|
| Controller | controller/{context}/{Entity}Controller.java | @RestController、@RequestMapping、@Api、@Slf4j；注入 ApplicationService；创建 POST /create、修改 POST /update、分页 POST /queryXxxPage、单条查询 POST /queryXxx、根据 ID 查询 GET /queryXxxByXxxId?xxxId=；返回 ResponseBase.success(data) 或 ResponseBase.success()。项目示例：`/user/queryUserPage`、`/user/queryUser`、`/user/queryUserByUserId?userId=`（见 UserController） |

**规范要点**：

- 使用 `@Api(tags = "xxx服务API")`、`@Slf4j` 注解。
- 注入使用 `@Resource` 而非 `@Autowired`。
- 入参使用 Command/PageQuery，加 `@RequestBody @Validated`；getById 用 `@RequestParam("id")` 并加 `@ApiParam` 说明。
- 创建方法记录日志：`log.info("Start xxx请求 Interfaces")`。
- 分页查询返回 `ResponseBase<PageResult<DTO>>`，非 `List<DTO>`。
- 统一返回 `ResponseBase<T>`，与 user-interface 一致。

---

## 四、示例：t_user 表（项目参考表结构）

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

**命名**：实体名 `User`；包/模块名 `user`；主键 `user_id` → Java `Integer userId`。

**领域层**（对应项目 `user-domain`）：

- **UserCommand**：userName、password、nickName、tel、age（创建/修改共用；无 userId）；见 `com.ddd.domain.user.entity.command.UserCommand`。
- **UserCreateCommand**：继承 UserCommand；见 `UserCreateCommand.java`。
- **UserUpdateCommand**：继承 UserCommand，增加 `private Integer userId;`；见 `UserUpdateCommand.java`。
- **UserModel**：userId、userName、password、nickName、tel、age；见 `entity/model/UserModel.java`。
- **UserDTO**：同上（查询出参）；见 `entity/dto/UserDTO.java`。
- **UserCreateDTO**：userId、userName、nickName 等；见 `entity/dto/UserCreateDTO.java`。
- **UserPageQuery**：extends PageBase；userId、userName、nickName、tel；见 `entity/query/UserPageQuery.java`。
- **UserQuery**：userId、userName、tel 等（单条条件）；见 `entity/query/UserQuery.java`。
- **UserAggregate**：持有 UserModel；`create(UserCreateCommand)` / `create(UserUpdateCommand)`；见 `aggregate/UserAggregate.java`。
- **UserRepository**：create、update、queryUserPage、queryUser、queryUserByUserId；见 `repository/UserRepository.java`。
- **UserDomainService**：businessValidation、logicProcess；见 `service/UserDomainService.java`。
- **UserCreatedEvent** / **UserUpdatedEvent**：构造 (source, userId)；见 `event/`。

**基础设施层**（对应项目 `user-infrastructure`）：

- **UserDO**：userId、userName、password、nickName、tel、age；与表字段 1:1；见 `entity/UserDO.java`。
- **UserMapper**：继承 BaseMapper&lt;UserDO&gt;；见 `mapper/UserMapper.java`。
- **UserRepositoryImpl**：实现 UserRepository；create/update 用 BeanUtil 转 DO；queryUserPage、queryUser、queryUserByUserId 见 `repository/UserRepositoryImpl.java`。

**应用层**（对应项目 `user-application`）：

- **UserApplicationService**：create、update、queryUserPage、queryUser、queryUserByUserId；见 `UserApplicationService.java`。
- **UserApplicationServiceImpl**：create/update 走聚合根→领域服务→仓储→缓存→事件；查询直接调仓储；见 `impl/UserApplicationServiceImpl.java`。

**接口层**（对应项目 `user-interface`）：

- **UserController**：POST /user/create、/user/update、/user/queryUserPage、/user/queryUser；GET /user/queryUserByUserId?userId=；返回 ResponseBase；见 `controller/user/UserController.java`。

---

## 五、生成顺序建议

1. 解析表结构，确定实体名、主键类型、字段与 Java 类型。
2. 领域层：Command → Model → DTO → Query/PageQuery → Aggregate → Repository 接口 → DomainService 接口/实现 → Event。
3. 基础设施层：DO → Mapper → RepositoryImpl。
4. 应用层：ApplicationService 接口 → ApplicationServiceImpl。
5. 接口层：Controller。

按此顺序可避免循环依赖，并保持与现有 user-* 代码风格一致。

## 六、方法级代码参考

创建、修改、分页查询、根据 ID 查询在各层的方法签名与实现要点见 [reference.md](reference.md)。
