package flab.project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import flab.project.dto.CommonResponseDto;
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

        userService.duplicatedEmail(email);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<CommonResponseDto<String>> findEmail(@RequestParam("phoneNumber") String phoneNumber) {
        String email = userService.findEmail(phoneNumber);

        return ResponseEntity.ok(CommonResponseDto.of(HttpStatus.OK.value(), "", email));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<HttpStatus> sendSmsCode(@PathVariable String phoneNumber) {
        HttpStatusCode httpStatusCode = smsService.sendAuthenticationSms(phoneNumber);

        if (!httpStatusCode.is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{phoneNumber}/{code}")
    public ResponseEntity<HttpStatus> checkSmsCode(@PathVariable String phoneNumber,
                                                   @PathVariable String code) {

        smsService.checkSmsCode(phoneNumber, code);

        return ResponseEntity.ok().build();
    }
}
