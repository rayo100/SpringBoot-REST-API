package org.adaschool.api.controller.user;

import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.user.*;
import org.adaschool.api.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDTO) {
        User userToAdd = new User(userDTO);
        URI createdUserUri = URI.create("");
        return ResponseEntity.created(createdUserUri).body(usersService.save(userToAdd));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.all();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        User userFound = usersService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(userFound);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id) {
        Optional<User> userToUpdate = usersService.findById(id);
        if (userToUpdate.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        usersService.save(userToUpdate.get());
        return ResponseEntity.ok(userToUpdate.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        usersService.findById(id).ifPresentOrElse(
                action -> this.usersService.deleteById(id),
                () -> { throw new UserNotFoundException(id); }
        );
        return ResponseEntity.ok().build();
    }
}
