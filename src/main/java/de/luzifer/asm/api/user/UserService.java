package de.luzifer.asm.api.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {

    public static final List<User> users = new ArrayList<>();

    public static User getOrCreateUser(UUID uuid) {

        for(User u : users) {
            if(u.uuid.equals(uuid)) return u;
        }

        User user = new User(uuid);
        users.add(user);
        return user;
    }

    public static void removeUser(UUID uuid) {
        users.remove(getOrCreateUser(uuid));
    }

}
