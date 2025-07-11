package library.project.controller;

import library.project.dto.LoginRequest;
import library.project.dto.LoginResponse;
import library.project.dto.RoleRequest;
import library.project.model.Role;
import library.project.model.User;
import library.project.repository.RoleRepository;
import library.project.security.JwtUtil;
import library.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("project/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<String> info(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("""
                    As admin you can:
                        > /admin/remove/{login} (DELETE)
                        > /admin/users          (GET)
                        > /admin/role           (DELETE with RoleRequest in body)
                        > /admin/role           (POST with RoleRequest in body)
                    """);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(),
                            loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        LoginResponse responseBody = new LoginResponse(token);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest req) {
        try {
            userService.register(req);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Registered successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());
        }
    }


    //////////////// Admin actions /////////////////

    @DeleteMapping("/admin/remove/{login}")
    public ResponseEntity<User> removeUser(@PathVariable String login) {
        Optional<User> userToRemove = userService.findByLogin(login);
        if (userToRemove.isPresent()) {
            userToRemove.get().setRoles(Collections.emptySet());

            return ResponseEntity.ok(userService.save(userToRemove.get()));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/users")
    public List<User> allUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/admin/role")
    public ResponseEntity<Set<Role>> removeRole(@RequestBody RoleRequest req) {
        Optional<User> user = userService.findByLogin(req.getLogin());
        if (user.isEmpty()) {
            System.out.println("User with that login does not exist");
            return ResponseEntity.notFound().build();
        }

        Optional<Role> roleToRemove = roleRepository.findByName(req.getName());
        if (roleToRemove.isEmpty()){
            System.out.println("Specified role does not exist");
            return ResponseEntity.notFound().build();
        }

        if (!user.get().getRoles().contains(roleToRemove.get())) {
            System.out.println("User does not have specified role");
            return ResponseEntity.notFound().build();
        }

        Set<Role> newUserRoles = new HashSet<>(Collections.emptySet());
        for (Role role : user.get().getRoles()) {
            if (!role.equals(roleToRemove.get())) newUserRoles.add(role);
        }
        user.get().setRoles(newUserRoles);
        return ResponseEntity.ok(userService.save(user.get()).getRoles());
    }

    @PostMapping("/admin/role")
    public ResponseEntity<Set<Role>> addRole(@RequestBody RoleRequest req) {
        Optional<User> user = userService.findByLogin(req.getLogin());
        if (user.isEmpty()) {
            System.out.println("User with that login does not exist");
            return ResponseEntity.notFound().build();
        }

        Optional<Role> roleToAdd = roleRepository.findByName(req.getName());
        if (roleToAdd.isEmpty()){
            System.out.println("Specified role does not exist");
            return ResponseEntity.notFound().build();
        }

        if (user.get().getRoles().contains(roleToAdd.get())) {
            System.out.println("User already has specified role");
            return ResponseEntity.accepted().build();
        }

        Set<Role> roles = user.get().getRoles();
        roles.add(roleToAdd.get());
        user.get().setRoles(roles);
        return ResponseEntity.ok(userService.save(user.get()).getRoles());
    }
}
