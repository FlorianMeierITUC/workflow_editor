package de.ketobi.vaadinspringdemo.services;

import com.vaadin.flow.server.VaadinSession;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByName(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public static User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute("user");
    }

    public static User getSystemUser() {
        User system = new User();
        system.setId(new ObjectId("000000000000000000000000"));
        system.setName("System");
        system.setEmail("no-reply");
        system.setPassword("null");
        return system;
    }
}