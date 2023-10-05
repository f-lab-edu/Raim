package flab.project.controller;

import flab.project.dto.UserDto;
import flab.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid UserDto userDto) {

        userService.registrationUser(UserDto.getUser(userDto));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicated/{email}")
    public ResponseEntity<HttpStatus> duplicatedEmail(@PathVariable String email) {

        if (userService.duplicatedEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }
}
