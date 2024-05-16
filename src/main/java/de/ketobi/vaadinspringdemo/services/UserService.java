package de.ketobi.vaadinspringdemo.services;

import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.repositories.UserRepository;
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
}