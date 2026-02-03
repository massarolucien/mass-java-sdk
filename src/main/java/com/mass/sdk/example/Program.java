package com.mass.sdk.example;

import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopNetGame;
import com.mass.sdk.desktop.models.DesktopNetGameCharacter;
import com.mass.sdk.desktop.models.DesktopSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Program {
    private static final Logger logger = LoggerFactory.getLogger(Program.class);

    static void main(String[] args) {
        try {
            // 查找本地Mass服务
            MassClient massClient = MassClient.findAsync(23333, 10);

            // 登录 Mass
            massClient.massLogin("bUSNBWWVT+G5OC/jKU3IWd2X8xec5e20WBv8RnUjxWzV3030/6EyH3f5YvIL+mHaFo6+CSoo46u5uIMs9d/pJS4cXaks6c1UYSPjanbqSQuIaAkYLnLRp28DhCoHDEVOO9ja0zRlWzkZcHW0HiYG8RCeIJ8VUltzDLR60df0hLzeDsR95u6uouqiga1HJ7lHcBWwvj48R5rclnzrGX8lUGvVQ5hWkWd0KsuGtpE38VYEEKg6DU8axnY2uVxSZ+xWprEorbVSurBawh+Hn8iHi/eqo568O4q0UvZa3SL4tdsBmPv0KRvAVnl90FGhsYY770plJARarZN1/ma8RTrR9w==");
            logger.info("成功登录Mass");

            // 随机小号登录
            DesktopSession session = massClient.desktop.randomLogin();
            logger.info("成功登录 {}", session.getUserId());

            // 获取网络服务器列表
            List<DesktopNetGame> netGames = session.getNetGames();
            DesktopNetGame heypixelGame = null;
            for (DesktopNetGame game : netGames) {
                if (game.getName().contains("布吉岛")) {
                    heypixelGame = game;
                    break;
                }
            }

            // 添加随机角色
            session.addNetGameCharacter(heypixelGame.getId(), RandomHelper.getString(10));

            // 获取角色列表
            List<DesktopNetGameCharacter> characters = session.getNetGameCharacters(heypixelGame.getId());
            DesktopNetGameCharacter character = characters.get(0);

            // 启动代理服务
            int port = session.startNetGameProxy(heypixelGame.getId(), character.getName());
            logger.info("{}", "127.0.0.1:" + port);
        } catch (Exception e) {
            logger.error("程序执行失败", e);
        }
    }
}