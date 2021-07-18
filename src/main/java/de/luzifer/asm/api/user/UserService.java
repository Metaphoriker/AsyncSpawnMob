package de.luzifer.asm.api.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> users = new ArrayList<>();

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
