package utils;

import messages.User;

public class LoginHandler {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        LoginHandler.user = user;
    }
}
