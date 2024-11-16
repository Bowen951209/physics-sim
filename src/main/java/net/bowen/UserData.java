package net.bowen;

import java.awt.*;

public class UserData {
    public static final UserData DEFAULT_DATA = create(Color.WHITE);

    public Color color = Color.WHITE;

    public static UserData create(Color color) {
        UserData userData = new UserData();
        userData.color = color;
        return userData;
    }
}
