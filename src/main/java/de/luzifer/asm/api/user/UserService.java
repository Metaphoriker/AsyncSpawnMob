package de.luzifer.asm.api.user;

import java.util.*;

public interface UserService {

    Set<User> users = new HashSet<>();

    static User getOrCreateUser(UUID uuid) {

        for(User u : users)
            if(u.uuid.equals(uuid)) return u;

        User user = new User(uuid);
        users.add(user);

        return user;
    }

    static void removeUser(UUID uuid) {
        users.remove(getOrCreateUser(uuid));
    }

}
