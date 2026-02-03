# mass-sdk-java
## 项目简介
Mass 官方 Java SDK，基于 Java 原生实现，提供统一的 API 请求封装、参数自动序列化、鉴权登录、连通性检测以及桌面端接口的支持，便于在 Java 项目中快速集成 Mass 服务。

## 布吉岛 SDK 使用示例
仅需 MassHeypixel.java 单文件，一行代码即可快速启动布吉岛代理
```java
var address = MassHeypixel.StartProxy(23333, "Token");
```

## 使用示例

```java
import com.mass.sdk.MassHeypixel;

// 寻找 Mass 服务
MassClient massClient = MassClient.findAsync(23333, 10);

// 登录 Mass
massClient.massLogin("Token");
            
// 随机小号登录
DesktopSession session = massClient.desktop.randomLogin();

// 获取网络服务器列表
List<DesktopNetGame> netGames = session.getNetGames();

// 添加角色
session.addNetGameCharacter(game.getId(), "角色名");

// 获取角色列表
List<DesktopNetGameCharacter> characters = session.getNetGameCharacters(game.getId());

// 启动代理服务
int port = session.startNetGameProxy(game.getId(), "角色名");
```
