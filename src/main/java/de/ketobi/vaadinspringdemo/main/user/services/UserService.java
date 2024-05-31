package de.ketobi.vaadinspringdemo.main.user.services;

import com.vaadin.flow.server.VaadinSession;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByName(username).orElseThrow();
        if (user.getPassword().equals(password)) {
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

    public User getUserById(ObjectId id){
        if(id.equals(new ObjectId("000000000000000000000000"))) {
            return getSystemUser();
        }
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> getAllUsersExceptTheCurrentUser() {
        User currentUser = getCurrentUser();
        return userRepository.findAll().stream().filter(user -> !user.getId().equals(currentUser.getId())).collect(Collectors.toList());
    }

    public ArrayList<User> getAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}