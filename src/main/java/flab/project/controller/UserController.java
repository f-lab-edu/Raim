package flab.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import flab.project.dto.UserDto;
import flab.project.service.SmsService;
import flab.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SmsService smsService;

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

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<HttpStatus> sendSmsCode(@PathVariable String phoneNumber) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        HttpStatusCode httpStatusCode = smsService.sendAuthenticationSms(phoneNumber);

        if (!httpStatusCode.is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{phoneNumber}/{code}")
    public ResponseEntity<HttpStatus> checkSmsCode(@PathVariable String phoneNumber,
                                                   @PathVariable String code) {

        if (!smsService.checkSmsCode(phoneNumber, code)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }
}
