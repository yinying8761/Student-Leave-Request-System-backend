# 学生请销假系统 · 后端

基于 Spring Boot + MyBatis + MySQL 的学生请销假系统服务端，提供登录鉴权、请假申请、辅导员审批、销假、用户管理与数据统计等 REST 接口。

- 仓库：`git@github.com:yinying8761/Student-Leave-Request-System-backend.git`
- 前端仓库：`Student-Leave-Request-System-frontend`（Vue 3 + Vite，默认 `http://localhost:5173`）
- 默认端口：`8080`

---

## 一、技术栈

| 分类 | 选型 |
| --- | --- |
| 语言 / 构建 | Java 21、Maven 3.9+ |
| 框架 | Spring Boot 2.7.18（web / security / validation） |
| 持久层 | MyBatis Spring Boot Starter 2.3.2 + XML 映射 |
| 数据库 | MySQL 8.x（mysql-connector-java 8.0.33） |
| 鉴权 | Spring Security（无状态）+ JWT（jjwt 0.11.5） |
| 其他 | Lombok、BCrypt 密码加密、`@Valid` 参数校验 |

---

## 二、目录结构

```
backend/
├── pom.xml
├── database/
│   ├── schema.sql                     # 建表脚本 + 默认管理员数据
│   └── migrate_add_leave_fields.sql   # 旧库补字段脚本（离校/目的地/紧急联系人）
└── src/main/
    ├── java/com/leave/
    │   ├── LeaveApplication.java          # 启动类
    │   ├── common/                        # Result、PageResult、BusinessException、
    │   │                                  # GlobalExceptionHandler、JwtUtils
    │   ├── config/                        # SecurityConfig、JwtAuthFilter
    │   ├── controller/                    # Auth / User / LeaveApplication /
    │   │                                  # Approval / Cancellation / Statistics
    │   ├── dto/                           # 请求体与 UserVO
    │   ├── entity/                        # User、LeaveApplication、ApprovalRecord、
    │   │                                  # LeaveCancellation、Notification
    │   ├── mapper/                        # MyBatis Mapper 接口
    │   └── service/ + service/impl/        # 业务层
    └── resources/
        ├── application.yml.example        # 配置模板（真实配置已被 .gitignore 忽略）
        └── mapper/*.xml                   # SQL 映射文件
```

---

## 三、环境要求

- JDK 21：[adoptium.net/download](https://adoptium.net/download/)
- Maven 3.9+：[maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
- MySQL 8.x：[dev.mysql.com/downloads/mysql](https://dev.mysql.com/downloads/mysql/)

---

## 四、快速开始

### 1. 初始化数据库

```sql
CREATE DATABASE leave_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

```bash
mysql -u root -p leave_system < database/schema.sql
```

> 若是**升级已有库**（`leave_application` 表已存在但缺少离校/目的地/紧急联系人字段），先执行：
> ```bash
> mysql -u root -p leave_system < database/migrate_add_leave_fields.sql
> ```

### 2. 配置应用

复制模板并按本机情况修改数据库账号密码：

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/leave_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: <你的密码>
jwt:
  secret: <至少 32 字节的密钥>
  expiration: 86400000   # token 有效期，单位毫秒（默认 24 小时）
```

> `application.yml` 已在 `.gitignore` 中，**请勿提交真实数据库密码和 JWT 密钥**。

### 3. 启动

```bash
mvn spring-boot:run
```

或打包后运行：

```bash
mvn clean package -DskipTests
java -jar target/leave-system-1.0.0.jar
```

启动后接口地址为 `http://localhost:8080`。

### 4. 默认账号

| 用户名 | 密码 | 角色 |
| --- | --- | --- |
| `admin` | `admin123` | ADMIN 系统管理员 |

学生与辅导员账号需由管理员在「用户管理」中创建（或直接写库，密码须为 BCrypt 密文）。

---

## 五、数据库设计

| 表 | 说明 |
| --- | --- |
| `user` | 用户表。`role` ∈ `STUDENT` / `COUNSELOR` / `ADVISOR` / `ADMIN`；学生通过 `counselor_id`（及预留的 `advisor_id`）绑定审批人 |
| `leave_application` | 请假申请。含请假类型、起止时间、天数、原因、是否离校、目的地省市區与详细地址、本人电话、紧急联系人、`status` |
| `approval_record` | 审批记录。`step`（1=辅导员）、`action`（APPROVE/REJECT）、`comment` |
| `leave_cancellation` | 销假记录。实际返校时间、状态（PENDING/APPROVED）、销假说明与审批人 |
| `leave_quota` | 请假额度表（预留，当前业务未使用） |
| `notification` | 通知表（预留，当前业务未使用） |

**请假状态流转**

```
PENDING ──辅导员审批通过──> APPROVED ──到达 end_time（自动）──> CANCELLING ──销假审批通过──> CANCELLED
   │                            │
   │                            └──学生/辅导员发起销假──> CANCELLING ──> CANCELLED
   ├──辅导员驳回──> REJECTED
   └──学生撤销（仅 PENDING）──> REJECTED
```

- 学生撤销的 SQL 是把状态置为 `REJECTED`（`LeaveApplicationMapper.xml` → `cancel`），仅对本人 `PENDING` 的申请生效。
- 列表查询前会执行 `updateExpiredToCancelling()`：已通过的请假一旦超过 `end_time` 自动进入 `CANCELLING`（待销假）。
- 请假天数由后端按 `(end_time - start_time)` 向上取整到 0.1 天计算，不信任前端传值。

---

## 六、接口一览

统一响应体：

```json
{ "code": 200, "message": "success", "data": {} }
```

分页数据 `data` 形如：`{ "records": [], "total": 0, "current": 1, "size": 10 }`。
非 200 的 `code` 由 `GlobalExceptionHandler` / `Result.fail` 返回，前端 `utils/request.js` 会统一弹出 `message`。

### 认证 `/api/auth`

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | 公开 | 登录，返回 `{ token, user }` |
| GET | `/api/auth/me` | 登录 | 获取当前用户信息（含辅导员姓名） |

### 用户 `/api/users`

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| GET | `/api/users` | ADMIN | 用户列表 |
| GET | `/api/users/{id}` | ADMIN | 用户详情 |
| POST | `/api/users` | ADMIN | 新建用户（密码必填，BCrypt 存储） |
| PUT | `/api/users/{id}` | ADMIN | 修改用户（密码留空则不修改） |
| DELETE | `/api/users/{id}` | ADMIN | 删除用户 |
| PUT | `/api/users/profile` | 登录 | 修改本人资料（姓名/手机/邮箱/院系/班级/所属辅导员） |
| PUT | `/api/users/password` | 登录 | 修改密码（校验原密码） |
| GET | `/api/users/counselors` | 登录 | 辅导员下拉列表 |

### 请假申请 `/api/applications`

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| POST | `/api/applications` | 仅 STUDENT | 提交请假申请 |
| GET | `/api/applications?current=1&size=10` | 登录 | 分页列表：学生=本人；辅导员=名下学生；管理员=全部 |
| GET | `/api/applications/{id}` | 登录 | 详情，返回 `{ application, records }` |
| PUT | `/api/applications/{id}/cancel` | 登录 | 撤销本人 `PENDING` 申请 |

### 审批 `/api/approvals`

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| GET | `/api/approvals/pending` | COUNSELOR | 待审批列表（按 `user.counselor_id` 匹配名下学生） |
| POST | `/api/approvals` | COUNSELOR | 审批：`{ applicationId, action: APPROVE|REJECT, comment }` |

当前为**辅导员单级审批**（`step = 1`），旧的导师（ADVISOR）审批环节已移除。

### 销假 `/api/cancellations`

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| POST | `/api/cancellations` | 仅 STUDENT | 学生提交返校销假（申请须为 `APPROVED`/`CANCELLING`，且不能有未处理的销假） |
| GET | `/api/cancellations/pending` | 登录 | 待处理销假列表 |
| PUT | `/api/cancellations/{id}/approve` | 登录 | 通过销假，申请状态置为 `CANCELLED` |
| POST | `/api/cancellations/counselor` | 仅 COUNSELOR | 辅导员代为销假（对 `APPROVED`/`CANCELLING` 申请直接确认返校） |

### 统计 `/api/statistics`

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| GET | `/api/statistics/dashboard` | 登录 | 工作台计数：辅导员=名下待审批数；学生=本人申请总数 |
| GET | `/api/statistics/class` | 登录 | 按当前用户院系/班级汇总每名学生请假次数与累计天数 |
| GET | `/api/statistics/student/{id}` | 登录 | 学生个人统计（**当前实现返回空列表，待补充**） |

---

## 七、鉴权与安全

- 登录成功签发 JWT（HS256），载荷含 `sub=userId`、`username`、`role`，有效期由 `jwt.expiration` 控制。
- `JwtAuthFilter` 从 `Authorization: Bearer <token>` 解析，校验通过后按 `user.enabled` 加载用户并注入 `ROLE_{role}` 权限。
- `SecurityConfig`：无状态 Session、关闭 CSRF、开启 CORS（允许所有来源与方法，开发期前端经 Vite 代理访问）。

**接口权限规则（`SecurityConfig` 匹配顺序）**

1. `/api/auth/**` → 直接放行
2. `/api/users/profile`、`/api/users/password`、`/api/users/counselors` → 需登录
3. `/api/users/**` → 需 `ADMIN`
4. `/api/applications/**`、`/api/cancellations/**`、`/api/statistics/**` → 需登录
5. `/api/approvals/**` → 需 `COUNSELOR`

控制器与服务层还会再校验一次角色（仅学生可提交申请、仅辅导员可审批或代销假）。

**错误码约定**

| code | 含义 |
| --- | --- |
| 200 | 成功 |
| 400 | 业务异常或参数校验失败（`BusinessException` / `@Valid`） |
| 403 | 控制器层角色不足（如非学生提交申请） |
| 401 | token 缺失或失效（前端拦截 401 后退出登录并跳回登录页） |
| 500 | 服务器内部错误 |

**开发注意事项**

- 新增业务字段时请同步修改 `database/schema.sql`、迁移脚本、`entity`、`mapper/*.xml` 与 `dto`。
- 只提交 `application.yml.example`，真实配置（数据库密码、JWT 密钥）保持本地。
- 时间统一按 `serverTimezone=Asia/Shanghai` 处理，`DATETIME` ↔ `java.sql.Timestamp`。

---

## 八、项目现状与已知待办

**已完成**：JWT 登录鉴权、请假申请（含是否离校、目的地、紧急联系人）、辅导员单级审批、销假（学生发起 + 辅导员代销假）、超期自动转为待销假、用户管理、个人资料与密码修改、工作台与班级统计。

**待完善**：

- 暂未编写单元测试 / 集成测试，打包时使用 `-DskipTests`。
- `/api/statistics/student/{id}` 未实现，当前返回空列表。
- `notification` 表与 `NotificationMapper` 已创建但未接入业务，审批/销假暂无站内通知。
- `leave_quota` 额度表未接入，提交申请时未校验单次/学期上限。
- 前端 `api/approval.js` 引用了 `GET /api/approvals/history`，后端尚未提供该接口。
- 请假实时状态（在校 / 离校）与审批记录的进一步展示。
- `ADVISOR` 角色在表结构与个别校验中仍有保留，但多级审批流程已下线。

---

## 九、常见问题

**Q：提示找不到 `mvn` 命令？**
A：确认已安装 Maven 并配置 `PATH`，或在 `backend` 目录下添加 Maven Wrapper（`mvnw`）。

**Q：数据库连接失败？**
A：检查 MySQL 是否启动、账号密码是否正确、`leave_system` 库是否已创建，以及 `application.yml` 是否已从模板复制。

**Q：启动时报 JWT 密钥过短（WeakKeyException）？**
A：HS256 要求 `jwt.secret` 至少 32 字节（UTF-8），换用更长的密钥即可。

**Q：8080 端口被占用？**
A：修改 `application.yml` 的 `server.port`，并同步修改前端 `vite.config.js` 的代理目标。

**Q：管理员密码忘了？**
A：用 `schema.sql` 中的 BCrypt 密文覆盖 `user` 表中 `admin` 的记录（明文为 `admin123`），或用 `BCryptPasswordEncoder` 生成新密文后更新。