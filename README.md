# mass-sdk-java 使用教程

## 1. 这份文档适合谁？

`mass-sdk-java` 是一个面向 Java 17 的 SDK，用来连接本地运行的 `Mass` 服务，并以编程方式完成：

- 发现本地 `Mass` 服务
- 使用外部签发的 token 登录 `Mass`
- 登录桌面版或手机版账号
- 查询网络服、租赁服、皮肤、角色
- 启动代理实例
- 启动游戏实例
- 监听实例启动进度
- 查询和关闭已启动实例

如果你准备在自己的工具、脚本、桌面程序或服务端程序里集成 `Mass`，这份文档就是给你的。

---

## 2. 先理解整体工作流

在开始之前，建议先把 `mass-sdk-java` 的调用链理解清楚：

1. 先启动本地 `Mass` 服务
2. 在你的 Java 程序里创建 `MassClient`
3. 用 `MassClient.find()` 自动发现本地服务，或手动指定地址
4. 你自己从业务服务端获取一个 `Mass token`
5. 调用 `massClient.massLogin(token)` 完成本地 `Mass` 服务登录
6. 通过 `desktop()` 或 `mobile()` 子客户端登录具体账号
7. 拿到 `DesktopSession` 或 `MobileSession` 后，再继续查游戏、角色、皮肤，或启动代理、游戏

这意味着：

- `mass-sdk-java` 不负责“签发 token”
- token 的签发流程应该放在调用方自己的服务端
- SDK 负责“拿着 token 去登录本地 Mass 服务”
- 账号登录成功后，后续操作都围绕 `Session` 对象展开

---

## 3. 项目结构速览

`mass-sdk-java` 当前主要由下面几部分组成：

- `MassClient`
  - SDK 总入口，封装 REST 请求、SignalR 进度连接、服务探测、Mass 登录
- `DesktopClient`
  - 桌面版账号登录入口
- `MobileClient`
  - 手机版账号登录入口
- `InstanceClient`
  - 实例管理入口，可通过 `massClient.instance()` 直接使用
- `DesktopSession`
  - 桌面版登录成功后的会话对象，可继续查服、查角色、换皮肤、启动代理、启动 Java 游戏
- `MobileSession`
  - 手机版登录成功后的会话对象，可继续查服、启动 Cpp 游戏
- `models`
  - 各类响应模型、分页模型、进度模型、账号模型、实例模型

---

## 4. 运行前提

### 4.1 开发环境

- JDK 17 或更高版本
- Maven 3.9 或更高版本
- 本地可运行的 `Mass.LocalServer`
- 你自己的 token 获取方式
- 调用方自己的服务端授权接口

### 4.2 为什么必须先启动本地服务

`mass-sdk-java` 不是直接连接远程业务系统，它主要调用的是本地 `Mass` 服务暴露出来的 HTTP / SignalR 接口。

从源码可以看出：

- `MassClient.find()` 默认从 `127.0.0.1:23333` 开始扫描
- 默认尝试 10 次，也就是扫描 `23333` 到 `23342`

所以最常见的使用方式就是：

1. 启动 `Mass.LocalServer`
2. 再运行你的 Java 程序

---

## 5. 安装与引用

### 5.1 在当前仓库里直接构建

如果你正在这个仓库里开发，可以直接在 `mass-sdk-java` 目录执行：

```powershell
mvn clean package
```

### 5.2 安装到本地 Maven 仓库

如果你想在其他本地项目中引用，可以先执行：

```powershell
mvn clean install
```

然后在你的项目中添加依赖：

```xml
<dependency>
  <groupId>com.mass</groupId>
  <artifactId>mass-sdk-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 5.3 运行仓库内的示例

当前 `pom.xml` 已配置好 `exec-maven-plugin`，可以直接运行示例入口：

```powershell
mvn exec:java
```

如果你要显式指定主类：

```powershell
mvn -Dexec.mainClass=com.mass.sdk.example.Program exec:java
```

`com.mass.sdk.example.Server` 的作用不是建议你把开放平台凭据直接写进客户端，而是演示“如何向开放平台申请 token”的最小示例。

在真实项目里，更推荐这样做：

1. 你的客户端把“当前软件用户名”发给你自己的服务端
2. 你的服务端校验这个用户是否有权使用你的软件
3. 你的服务端再去申请 `Mass token`
4. 你的服务端把 token 返回给客户端
5. 客户端再调用 `massClient.massLogin(token)`

这样做的好处是：

- 不会把开放平台的敏感凭据暴露给客户端
- 授权逻辑由调用方自己掌控
- 你可以在自己的服务端做用户校验、封禁、额度控制、审计日志等业务处理

---

## 6. 第一个可运行示例

下面是一条最标准的主线：

```java
import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopNetGame;
import com.mass.sdk.desktop.models.DesktopNetGameCharacter;
import com.mass.sdk.desktop.models.DesktopSession;
import com.mass.sdk.example.RandomHelper;
import com.mass.sdk.models.ProxyInstance;

public final class Demo {
    public static void main(String[] args) throws Exception {
        MassClient client = MassClient.find();

        String token = getMassTokenFromYourServer("your-software-username");
        client.massLogin(token);

        DesktopSession session = client.desktop().login4399ComRandom();

        DesktopNetGame game = session.getDesktopNetGames().get(0);
        session.addDesktopNetGameCharacter(game.getId(), RandomHelper.getString(10));

        DesktopNetGameCharacter character = session.getDesktopNetGameCharacters(game.getId()).get(0);
        ProxyInstance proxy = session.startDesktopNetGameProxy(game.getId(), character.getName());

        System.out.println("代理端口: " + proxy.getPort());
    }

    private static String getMassTokenFromYourServer(String username) {
        throw new UnsupportedOperationException("这里应该调用你自己的服务端接口");
    }
}
```

这段代码做了几件事：

- 自动发现本地 `Mass` 服务
- 从你自己的服务端获取 token 并登录 `Mass`
- 使用一个随机 4399 账号登录桌面版
- 获取网络服列表
- 给目标游戏添加一个随机角色
- 获取角色列表
- 启动代理实例

---

## 7. 启动本地 `Mass` 服务

如果你在这个仓库里开发，可以直接运行：

```powershell
dotnet run --project .\Mass.LocalServer\Mass.LocalServer.csproj
```

通常情况下，服务会监听本地端口，SDK 默认从 `23333` 开始探测。

如果你没有使用默认端口，也可以手动创建客户端：

```java
MassClient client = new MassClient("http://127.0.0.1:25000");
```

---

## 8. 创建 `MassClient`

### 8.1 自动发现服务

```java
MassClient client = MassClient.find();
```

默认行为：

- 起始端口：`23333`
- 尝试次数：`10`
- 实际扫描范围：`23333` 到 `23342`

也可以自定义：

```java
MassClient client = MassClient.find(24000, 20);
```

### 8.2 手动指定地址

```java
MassClient client = new MassClient("http://127.0.0.1:23333");
```

适合这些场景：

- 你明确知道服务地址
- 你不想走扫描逻辑
- 本地服务运行在非默认端口

### 8.3 服务健康检查

```java
boolean ok = client.ping();
System.out.println(ok ? "服务可用" : "服务不可用");
```

---

## 9. `massLogin` 是什么？

### 9.1 它不是账号登录

很多人第一次看 SDK 会把这一步和“网易账号登录”混在一起。实际上：

- `massClient.massLogin(token)` 登录的是本地 `Mass` 服务
- `desktop().loginXXX(...)` / `mobile().loginXXX(...)` 登录的才是具体账号

### 9.2 正确顺序

```java
MassClient client = MassClient.find();

String token = getMassTokenFromYourServer("your-software-username");
client.massLogin(token);

DesktopSession session = client.desktop().login4399ComRandom();
```

### 9.3 token 从哪里来

SDK 里没有“获取 token”的公共接口。示例项目中的 `Server.getToken(username)` 展示的是“向开放平台获取 token”的过程，然后再把拿到的 token 传给：

```java
client.massLogin(token);
```

但这个过程在真实项目里应该放在你自己的服务端，而不是直接放在最终客户端里。

原因很简单：

- 请求开放平台时通常会带上敏感凭据
- 这些敏感凭据不应该出现在客户端程序中
- 授权判断应该由调用方自己的服务端完成

推荐架构不是：

```text
客户端 -> Mass 开放平台
```

而是：

```text
客户端 -> 你的服务端 -> Mass 开放平台
```

另外要特别注意，`Server.getToken(username)` 里的 `username` 指的是“调用方自己软件里的用户名”，不是网易账号，也不是 4399 账号。

---

## 10. `DesktopClient` 用法

`massClient.desktop()` 提供桌面版相关登录入口。

### 10.1 Cookies 登录

```java
DesktopSession session = client.desktop().loginCookies(cookies);
```

### 10.2 163 邮箱登录

```java
DesktopSession session = client.desktop().login163(email, password);
```

### 10.3 手机号密码登录

```java
DesktopSession session = client.desktop().loginMobile(mobile, password);
```

### 10.4 发送短信验证码

```java
client.desktop().sendSms(mobile);
```

### 10.5 手机验证码登录

```java
DesktopSession session = client.desktop().loginSms(mobile, code);
```

### 10.6 4399 PC 登录

```java
DesktopSession session = client.desktop().login4399Pc(username, password);
```

### 10.7 4399 网页登录

```java
DesktopSession session = client.desktop().login4399Com(username, password);
```

### 10.8 随机 4399 网页账号登录

```java
DesktopSession session = client.desktop().login4399ComRandom();
```

这个方法非常适合：

- 本地调试
- 快速验证流程是否可通
- 不关心固定账号，只想先跑通功能

---

## 11. `MobileClient` 用法

`massClient.mobile()` 提供手机版相关登录入口。

它和桌面版的大部分登录方式类似，支持：

- `loginCookies`
- `login163`
- `loginMobile`
- `sendSms`
- `loginSms`
- `login4399Com`
- `login4399ComRandom`

示例：

```java
var session = client.mobile().login4399ComRandom();
```

---

## 12. `Session` 对象是什么？

账号登录成功后，你拿到的是 `DesktopSession` 或 `MobileSession`。

这两个对象会保存当前登录上下文，常见字段包括：

- `userId`
- `cookies`
- `nickname`
- `info`

示例：

```java
System.out.println(session.getUserId());
System.out.println(session.getNickname());
System.out.println(session.getInfo().getPlatform());
System.out.println(session.getInfo().getType());
System.out.println(session.getInfo().getAccount());
```

其中 `info` 里通常包含：

- 登录平台
- 登录方式
- 账号
- 密码

如果你的程序有日志系统，请谨慎打印 `session.getInfo().getPassword()`，避免泄露敏感信息。

---

## 13. 查询桌面版网络服

```java
var games = session.getDesktopNetGames();

for (var game : games) {
    System.out.println(game.getName() + " | " + game.getId() + " | 在线 " + game.getPlayerCount());
}
```

`DesktopNetGame` 常用字段：

- `id`
- `name`
- `playerCount`
- `likeCount`
- `imageUrl`
- `summary`
- `downloadCount`
- `gameVersionId`

例如按名称筛选：

```java
var target = session.getDesktopNetGames().stream()
        .filter(x -> x.getName().contains("布吉岛"))
        .findFirst()
        .orElse(null);
```

---

## 14. 查询桌面版租赁服

```java
var rentalGames = session.getDesktopRentalGames();

for (var game : rentalGames) {
    System.out.println(game.getServerName() + " | " + game.getId() + " | " + game.getStatus());
}
```

`DesktopRentalGame` 常用字段：

- `id`
- `name`
- `serverName`
- `playerCount`
- `likeCount`
- `imageUrl`
- `visibility`
- `hasPassword`
- `serverType`
- `status`
- `capacity`
- `mcVersion`
- `ownerId`
- `worldId`
- `minLevel`
- `isPvpEnabled`
- `iconIndex`
- `offset`

如果你的业务需要判断是否能直接进入某个租赁服，通常会重点关注：

- `hasPassword`
- `status`
- `visibility`

---

## 15. 查询皮肤列表

### 15.1 查询全部皮肤

```java
var page = session.getDesktopSkins(1);

System.out.println("总页数: " + page.getTotalPage());
for (var skin : page.getItems()) {
    System.out.println(skin.getName() + " | " + skin.getId());
}
```

### 15.2 查询已拥有皮肤

```java
var page = session.getDesktopOwnedSkins(1);
```

`Page<T>` 结构很简单：

- `items`
- `totalPage`

`DesktopSkin` 常用字段：

- `id`
- `name`
- `briefSummary`
- `imageUrl`

---

## 16. 设置皮肤

```java
session.setDesktopSkin(itemId);
```

通常你会先获取皮肤列表，再把目标皮肤的 `id` 传给 `setDesktopSkin`：

```java
var page = session.getDesktopOwnedSkins(1);
var targetSkin = page.getItems().get(0);

session.setDesktopSkin(targetSkin.getId());
```

---

## 17. 查询角色列表

### 17.1 网络服角色

```java
var characters = session.getDesktopNetGameCharacters(gameId);

for (var character : characters) {
    System.out.println(character.getName() + " | " + character.getCreateTime());
}
```

### 17.2 租赁服角色

```java
var characters = session.getDesktopRentalGameCharacters(gameId);
```

角色模型共有这些核心字段：

- `gameId`
- `name`
- `createTime`

注意：

- Java 版已经把时间戳转换成了 `Instant`
- 时间转换由 `UnixTimestampInstantAdapter` 负责

---

## 18. 添加角色

### 18.1 给网络服添加角色

```java
session.addDesktopNetGameCharacter(gameId, "MyRoleName");
```

### 18.2 给租赁服添加角色

```java
session.addDesktopRentalGameCharacter(gameId, "MyRoleName");
```

### 18.3 生成随机角色名

仓库示例里带了一个 `RandomHelper`：

```java
import com.mass.sdk.example.RandomHelper;

String name = RandomHelper.getString(10);
session.addDesktopNetGameCharacter(gameId, name);
```

也可以自定义字符集：

```java
String name = RandomHelper.getString(8, "abcdefghijklmnopqrstuvwxyz0123456789");
```

如果你不希望业务代码依赖 `example` 包，也可以自己实现一个随机字符串工具。

---

## 19. 启动网络服代理

```java
var proxy = session.startDesktopNetGameProxy(gameId, roleName);

System.out.println(proxy.getId());
System.out.println(proxy.getPort());
System.out.println(proxy.getType());
System.out.println(proxy.getLaunchTime());
```

返回值是 `ProxyInstance`，它继承自 `MassInstance`，额外包含：

- `port`

最常见的用法是：

```java
var proxy = session.startDesktopNetGameProxy(gameId, roleName);
String address = "127.0.0.1:" + proxy.getPort();

System.out.println("请连接到 " + address);
```

---

## 20. 启动租赁服代理

```java
var proxy = session.startDesktopRentalGameProxy(gameId, roleName, password);
```

如果目标租赁服没有密码，也可以不传：

```java
var proxy = session.startDesktopRentalGameProxy(gameId, roleName);
```

建议在调用前先检查：

- `game.getHasPassword()`
- 你是否真的拿到了正确密码

---

## 21. 启动桌面版 Java 游戏

### 21.1 启动网络服 Java 游戏

```java
var game = session.startDesktopNetJavaGame(gameId, roleName);
```

### 21.2 启动租赁服 Java 游戏

```java
var game = session.startDesktopRentalJavaGame(gameId, roleName, password);
```

返回值是 `GameInstance`，继承自 `MassInstance`，并增加：

- `processId`

通常意味着：

- 游戏进程已经被启动
- 你可以拿到实例 ID
- 对于启动型实例，还能拿到进程 ID

---

## 22. 监听启动进度

这是 `mass-sdk-java` 很重要的一部分。

游戏启动不是一个瞬时动作，所以 SDK 使用 SignalR 订阅进度事件。你可以把一个 `Consumer<Progress>` 传给启动方法。

示例：

```java
var instance = session.startDesktopNetJavaGame(gameId, roleName, progress ->
        System.out.println("[" + progress.getStep() + "/" + progress.getTotal() + "] "
                + progress.getPercentage() + "% - " + progress.getMessage()));
```

`Progress` 模型包含：

- `step`
- `total`
- `percentage`
- `message`

这很适合：

- 控制台实时输出
- 桌面 UI 进度条
- WebSocket 中转给前端
- 任务状态监控

---

## 23. `MobileSession` 的特殊能力

`MobileSession` 继承自 `DesktopSession`，但有两个关键点需要注意：

### 23.1 获取手机版网络服列表

手机端使用的是：

```java
var games = mobileSession.getMobileNetGames();
```

返回类型是 `List<MobileNetGame>`。

### 23.2 启动手机版 Cpp 游戏

```java
var instance = mobileSession.startMobileNetCppGame(gameId, progress ->
        System.out.println(progress.getPercentage() + "% - " + progress.getMessage()));
```

---

## 24. 实例管理 `InstanceClient`

当前版本里，`InstanceClient` 已经挂在 `MassClient` 上，可以直接通过 `client.instance()` 使用。

### 24.1 查询实例列表

```java
var instances = client.instance().getList();

for (var instance : instances) {
    System.out.println(instance.getId() + " | " + instance.getType() + " | " + instance.getLaunchTime());
}
```

### 24.2 关闭所有实例

```java
client.instance().closeAll();
```

### 24.3 按游戏 ID 和角色名关闭实例

```java
client.instance().close(gameId, roleName);
```

### 24.4 按实例 ID 关闭实例

```java
client.instance().close(instanceId);
```

---

## 25. `MassInstance`、`GameInstance`、`ProxyInstance` 的区别

### 25.1 `MassInstance`

所有实例的基础模型，公共字段：

- `userId`
- `type`
- `id`
- `launchTime`

### 25.2 `GameInstance`

表示真正启动了一个游戏实例，额外字段：

- `processId`

### 25.3 `ProxyInstance`

表示启动了一个代理服务实例，额外字段：

- `port`

### 25.4 SDK 如何自动识别实例类型

SDK 内部注册了 `MassInstanceAdapter`，会根据返回 JSON 里的 `type` 字段自动反序列化：

- `java` 或 `cpp` -> `GameInstance`
- `java_proxy` -> `ProxyInstance`
- 其他 -> `MassInstance`

这意味着你拿到 `List<MassInstance>` 时，里面实际可能混有不同子类。

例如：

```java
for (var instance : client.instance().getList()) {
    if (instance instanceof ProxyInstance proxy) {
        System.out.println("代理端口: " + proxy.getPort());
    } else if (instance instanceof GameInstance game) {
        System.out.println("游戏进程: " + game.getProcessId());
    }
}
```

---

## 26. 错误处理

### 26.1 SDK 的异常行为

`MassClient.request(...)` 在这些情况下会抛出异常：

- HTTP 请求失败
- 响应无法反序列化
- 服务端返回的 `code` 不等于 `200`

Java 版主要抛出：

- `IOException`
- `IllegalStateException`

所以你应该在业务层捕获异常：

```java
try {
    var session = client.desktop().login4399ComRandom();
} catch (IOException ex) {
    System.out.println("请求失败: " + ex.getMessage());
} catch (Exception ex) {
    System.out.println("未知错误: " + ex);
}
```

### 26.2 `find()` 失败

如果扫描不到本地服务，会抛出：

```java
IllegalStateException
```

常见原因：

- `Mass.LocalServer` 没启动
- 服务端口不在默认扫描范围
- 本地防火墙或环境限制导致无法访问

建议写成：

```java
MassClient client;

try {
    client = MassClient.find();
} catch (IllegalStateException ex) {
    client = new MassClient("http://127.0.0.1:25000");
}
```

---

## 27. 关于资源释放

当前 `MassClient` 基于 JDK 自带 `HttpClient` 实现，本身没有提供 `close()` 或 `AutoCloseable`。

这意味着你通常不需要像 C# 版那样显式 `dispose` 客户端。更推荐的做法是：

- 在一个完整业务流程里复用同一个 `MassClient`
- 不要在 `massLogin` 之后频繁重新创建客户端

---

## 28. 推荐调用顺序

下面是一条比较稳妥的生产化顺序：

1. 启动并确认本地 `Mass.LocalServer` 可用
2. `MassClient client = MassClient.find();`
3. `client.massLogin(token);`
4. 调用 `desktop()` 或 `mobile()` 完成账号登录
5. 检查 `session.getUserId()`、`getNickname()`、`getInfo()`
6. 查询游戏列表并筛选目标游戏
7. 查询角色列表
8. 如果没有角色，先添加角色
9. 再启动代理或游戏
10. 用 `client.instance()` 做收尾清理

---

## 29. 一个更完整的控制台示例

```java
import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopNetGame;
import com.mass.sdk.desktop.models.DesktopNetGameCharacter;
import com.mass.sdk.desktop.models.DesktopSession;

public final class Demo {
    public static void main(String[] args) throws Exception {
        MassClient client = MassClient.find();

        String token = getMassTokenFromYourServer("demo-user");
        client.massLogin(token);

        DesktopSession session = client.desktop().login4399ComRandom();
        System.out.println("登录成功: " + session.getNickname() + " (" + session.getUserId() + ")");

        DesktopNetGame targetGame = session.getDesktopNetGames().stream()
                .filter(g -> g.getName().contains("布吉岛"))
                .findFirst()
                .orElseGet(() -> sessionSafeFirst(session));

        System.out.println("目标游戏: " + targetGame.getName() + " | " + targetGame.getId());

        var characters = session.getDesktopNetGameCharacters(targetGame.getId());
        DesktopNetGameCharacter role = characters.stream().findFirst().orElse(null);

        if (role == null) {
            String newName = randomString(10);
            session.addDesktopNetGameCharacter(targetGame.getId(), newName);
            role = session.getDesktopNetGameCharacters(targetGame.getId()).get(0);
        }

        System.out.println("使用角色: " + role.getName());

        var proxy = session.startDesktopNetGameProxy(targetGame.getId(), role.getName());
        System.out.println("代理已启动: 127.0.0.1:" + proxy.getPort());

        for (var instance : client.instance().getList()) {
            System.out.println("实例: " + instance.getId() + " | " + instance.getType() + " | " + instance.getLaunchTime());
        }

        // 如果要启动 Java 游戏，可以改用：
        // var gameInstance = session.startDesktopNetJavaGame(targetGame.getId(), role.getName(), p ->
        //         System.out.println("[" + p.getStep() + "/" + p.getTotal() + "] " + p.getPercentage() + "% " + p.getMessage()));
    }

    private static String getMassTokenFromYourServer(String username) {
        throw new UnsupportedOperationException("这里应该调用你自己的服务端接口。username 是你自己软件里的用户名，不是网易账号。");
    }

    private static DesktopNetGame sessionSafeFirst(DesktopSession session) {
        try {
            return session.getDesktopNetGames().get(0);
        } catch (Exception ex) {
            throw new IllegalStateException("没有可用游戏", ex);
        }
    }

    private static String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder(length);
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < length; i++) {
            builder.append(chars.charAt(random.nextInt(chars.length())));
        }

        return builder.toString();
    }
}
```

---

## 30. 常见问题

### 30.1 为什么 `massLogin` 之后还要 `desktop().loginXXX(...)`？

因为它们是两层登录：

- 第一层：登录本地 `Mass` 服务
- 第二层：登录目标账号

### 30.2 为什么 `find()` 找不到服务？

优先检查：

- `Mass.LocalServer` 是否已经启动
- 端口是不是默认的 `23333`
- 你是否需要手动指定地址

### 30.3 为什么启动游戏时没有立即返回？

因为游戏启动是异步流程，SDK 会通过 SignalR 等待后端把进度和最终实例推回来。

### 30.4 为什么建议复用同一个 `MassClient`？

因为 `massLogin` 完成后，后续请求依赖同一个客户端上下文。最稳妥的做法是：

- 一个业务流程使用一个 `MassClient`
- 不要在 `massLogin` 之后频繁新建客户端再继续调用

### 30.5 `InstanceClient` 应该怎么用？

当前版本已经可以直接通过：

```java
var instances = client.instance().getList();
```

如果你只是做实例查询、关闭、清理，一般不需要再手动创建 `InstanceClient`。

---

## 31. 最佳实践

- 先 `ping()` 或 `find()`，再进入主流程
- 把 token 获取逻辑和 SDK 调用逻辑分开
- token 申请放在你自己的服务端，不要把开放平台凭据下发到客户端
- 不要在日志里打印账号密码
- 启动游戏时尽量传入 `Consumer<Progress>`，便于排错
- 启动前先检查是否已有角色，避免不必要的重复创建
- 业务结束后用 `client.instance()` 清理无用实例
- 一个业务流程尽量复用一个 `MassClient`

---

## 32. API 速查

### 32.1 `MassClient`

| 方法/成员 | 说明 |
| --- | --- |
| `new MassClient(baseUrl)` | 手动指定服务地址 |
| `MassClient.find(startPort, tryTimes)` | 自动探测本地服务 |
| `ping()` | 检查服务可用性 |
| `massLogin(token)` | 登录本地 Mass 服务 |
| `desktop()` | 桌面版子客户端 |
| `mobile()` | 手机版子客户端 |
| `instance()` | 实例管理子客户端 |

### 32.2 `DesktopClient`

| 方法 | 说明 |
| --- | --- |
| `loginCookies(cookies)` | Cookies 登录 |
| `login163(email, password)` | 163 邮箱登录 |
| `loginMobile(mobile, password)` | 手机号密码登录 |
| `sendSms(mobile)` | 发送短信验证码 |
| `loginSms(mobile, code)` | 验证码登录 |
| `login4399Pc(username, password)` | 4399 PC 登录 |
| `login4399Com(username, password)` | 4399 网页登录 |
| `login4399ComRandom()` | 随机 4399 网页账号登录 |

### 32.3 `DesktopSession`

| 方法 | 说明 |
| --- | --- |
| `getDesktopNetGames()` | 获取网络服列表 |
| `getDesktopRentalGames()` | 获取租赁服列表 |
| `getDesktopSkins(page)` | 获取皮肤列表 |
| `getDesktopOwnedSkins(page)` | 获取已拥有皮肤 |
| `getDesktopNetGameCharacters(gameId)` | 获取网络服角色 |
| `getDesktopRentalGameCharacters(gameId)` | 获取租赁服角色 |
| `addDesktopNetGameCharacter(gameId, name)` | 添加网络服角色 |
| `addDesktopRentalGameCharacter(gameId, name)` | 添加租赁服角色 |
| `setDesktopSkin(itemId)` | 设置皮肤 |
| `startDesktopNetGameProxy(gameId, name)` | 启动网络服代理 |
| `startDesktopRentalGameProxy(gameId, name, password)` | 启动租赁服代理 |
| `startDesktopNetJavaGame(gameId, name, progressConsumer)` | 启动网络服 Java 游戏 |
| `startDesktopRentalJavaGame(gameId, name, password, progressConsumer)` | 启动租赁服 Java 游戏 |

### 32.4 `MobileSession`

| 方法 | 说明 |
| --- | --- |
| `getMobileNetGames()` | 获取手机版网络服列表 |
| `startMobileNetCppGame(gameId, progressConsumer)` | 启动手机版 Cpp 游戏 |

### 32.5 `InstanceClient`

| 方法 | 说明 |
| --- | --- |
| `getList()` | 获取实例列表 |
| `closeAll()` | 关闭所有实例 |
| `close(gameId, roleName)` | 按游戏和角色关闭实例 |
| `close(instanceId)` | 按实例 ID 关闭实例 |

---

## 33. 总结

如果只记住一条主线，请记住下面这四步：

1. 启动本地 `Mass.LocalServer`
2. `MassClient.find()` 找到服务
3. `massLogin(token)` 登录 `Mass`
4. 用 `DesktopSession` 或 `MobileSession` 完成后续业务动作
