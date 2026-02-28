# ddd-mysql-crud 技能使用说明

本技能用于根据 **MySQL 表结构** 按本 DDD 项目规范生成**创建、修改、分页查询、根据 ID 查询**四类能力对应的各层代码（领域层、基础设施层、应用层、接口层）。

## 何时会用到

- 你提供了一张表的 `CREATE TABLE` 或表结构
- 你希望“按表生成 DDD 各层代码”或“按表生成增删改查”
- 你需要为某个新表补齐 Controller、ApplicationService、Repository、DO、Command、DTO 等

Agent 会根据技能描述自动在以上场景下参考本技能生成代码。

---

## 如何使用

1. **确保技能在项目内**：本技能位于 `.cursor/skills/ddd-mysql-crud/`，与项目一起使用即可。
2. **在对话中直接说明需求**：提供表结构或表名，并说明要生成“四类能力”或“按 DDD 分层生成代码”。
3. **使用下面的提示词**：复制任一条到 Cursor 对话中发送，Agent 会按 SKILL.md 与 reference.md 的约定生成代码。

---

## 提示词示例

### 按表结构生成全部四类能力

```
请根据 ddd-mysql-crud 技能，按下面表结构生成 DDD 各层代码（创建、修改、分页查询、根据 ID 查询）：

create table t_xxx (
    id bigint not null auto_increment comment '主键',
    ...
    primary key (id)
) engine = InnoDB default charset = utf8mb4 comment = 'xxx表';
```

### 只提供表名，让 Agent 按项目规范生成

```
使用 @.cursor/skills/ddd-mysql-crud 技能，为 t_order 表生成创建、修改、分页查询、根据ID查询的完整 DDD 各层代码，表结构按项目惯例推断或由我补充。
```

### 已有表结构，只要补全某几层

```
参考 ddd-mysql-crud 技能和项目里的 user 模块，根据下面的 t_product 表结构，只生成领域层和基础设施层（Command、Model、DTO、Query、Aggregate、Repository 接口、DO、RepositoryImpl）。
```

### 指定主键/实体名

```
按 ddd-mysql-crud 技能，用表 t_payment_order（主键 id bigint）生成四类能力的全部分层代码，实体名用 PaymentOrder，包名用 payment。
```

### 仅生成接口层 + 应用层

```
根据 SKILL ddd-mysql-crud，为已有的 User 领域（表 t_user）只生成或补充：UserController 的创建、修改、分页查询、根据 userId 查询四个接口，以及 UserApplicationService 的对应方法声明与实现片段。
```

---

## 技能文件说明

| 文件 | 说明 |
|------|------|
| **SKILL.md** | 技能主文档：表与命名约定、各层生成清单、示例表 t_user、生成顺序 |
| **reference.md** | 方法级参考：创建/修改/分页/按ID查询在各层的真实代码片段（来自项目 user 模块） |
| **README.md** | 本文件：使用方式与提示词示例 |

---

## 生成后的四类能力对应关系

| 能力 | 接口示例（user 模块） | 说明 |
|------|------------------------|------|
| 创建 | `POST /user/create`，入参 CreateCommand，返回 CreateDTO | 聚合根 → 领域校验 → 持久化 → 事件 |
| 修改 | `POST /user/update`，入参 UpdateCommand（含主键） | 同上流程，无返回体 |
| 分页查询 | `POST /user/queryUserPage`，入参 PageQuery，返回 `PageResult<DTO>` | 应用层直接调仓储分页 |
| 单条查询 | `POST /user/queryUser`，入参 Query，返回 DTO | 应用层直接调仓储查询 |
| 根据 ID 查询 | `GET /user/queryUserByUserId?userId=`，返回 DTO | 应用层直接调仓储 getById |

其他表生成时，路径与方法名按实体替换（如 `queryOrderPage`、`queryOrderByOrderId`）。
