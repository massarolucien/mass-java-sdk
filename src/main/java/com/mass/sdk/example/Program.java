package com.mass.sdk.example;

import com.mass.sdk.MassClient;
import com.mass.sdk.desktop.models.DesktopNetGame;
import com.mass.sdk.desktop.models.DesktopNetGameCharacter;
import com.mass.sdk.desktop.models.DesktopSession;
import com.mass.sdk.models.ProxyInstance;

public final class Program {
    private Program() {
    }

    public static void main(String[] args) {
        try {
            var massClient = MassClient.find();

            var token = Server.getToken("YOUR_USERNAME");

            massClient.massLogin(token);
            System.out.println("Mass login succeeded.");

            DesktopSession session = massClient.desktop().login4399ComRandom();
            System.out.println("Account login succeeded: userId=" + session.getUserId()
                    + ", nickname=" + session.getNickname()
                    + ", platform=" + session.getInfo().getPlatform()
                    + ", type=" + session.getInfo().getType()
                    + ", account=" + session.getInfo().getAccount()
                    + ", password=" + session.getInfo().getPassword());

            DesktopNetGame heypixelGame = session.getDesktopNetGames().stream()
                    .filter(game -> game.getName().contains("布吉岛"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Target game was not found."));
            System.out.println(heypixelGame.getName() + " " + heypixelGame.getId());

            session.addDesktopNetGameCharacter(heypixelGame.getId(), RandomHelper.getString(10));
            System.out.println("Random role added.");

            DesktopNetGameCharacter character = session.getDesktopNetGameCharacters(heypixelGame.getId()).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Role was not found."));
            System.out.println("Current role: " + character.getName());

            ProxyInstance instance = session.startDesktopNetGameProxy(heypixelGame.getId(), character.getName());
            System.out.println("Proxy started at 127.0.0.1:" + instance.getPort());

            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
