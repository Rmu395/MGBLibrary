package library.project.service.impl;

import library.project.dto.LoginRequest;
import library.project.model.Role;
import library.project.model.User;
import library.project.repository.RoleRepository;
import library.project.repository.UserRepository;
import library.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void register(LoginRequest req) {
        if (userRepository.findByLogin(req.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Error...");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new IllegalStateException("There is no role... ROLE_USER"));

        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(u);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
