package library.project.service;

import library.project.dto.LoginRequest;
import library.project.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    User save(User user);
    void register(LoginRequest req);
    Optional<User> findByLogin(String login);
}
