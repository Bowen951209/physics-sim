package net.bowen.physics;

import java.awt.*;

public class UserData {
    public static final UserData DEFAULT_DATA = create(Color.WHITE);

    public Color color = Color.WHITE;
    public TrackTracer trackTracer;

    public static UserData create(TrackTracer trackTracer) {
        UserData userData = new UserData();
        userData.trackTracer = trackTracer;
        return userData;
    }

    public static UserData create(Color color) {
        UserData userData = new UserData();
        userData.color = color;
        return userData;
    }
}
