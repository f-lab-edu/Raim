package flab.project.controller;

import flab.project.domain.RequestHistory;
import flab.project.dto.CommonResponseDto;
import flab.project.dto.EmailResponseDto;
import flab.project.dto.UserDto;
import flab.project.service.RequestHistoryService;
import flab.project.service.SmsService;
import flab.project.service.UserService;
import flab.project.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static flab.project.util.ValidationUtils.validatePhoneNumber;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final SmsService smsService;
    private final RequestHistoryService requestHistoryService;

    /**
     *
     * TODO: 인증이 처리된 verification들 제거해야 함.
     */
    @PostMapping
    public ResponseEntity<HttpStatus> registration(@RequestBody UserDto userDto) {
        UserDto.validUserDto(userDto);

        userService.registrationUser(UserDto.getUser(userDto));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicated/{email}")
    public ResponseEntity<HttpStatus> duplicatedEmail(@PathVariable String email) {
        ValidationUtils.validateEmail(email);

        userService.duplicatedEmail(email);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<CommonResponseDto<EmailResponseDto>> findEmail(@RequestParam("phoneNumber") String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        String email = userService.findEmail(phoneNumber);

        return ResponseEntity.ok(CommonResponseDto.of(HttpStatus.OK.value(), "", EmailResponseDto.of(email)));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<HttpStatus> sendSmsCode(@PathVariable String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        smsService.sendAuthenticationSms(phoneNumber);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{phoneNumber}/{code}")
    public ResponseEntity<HttpStatus> checkSmsCode(@PathVariable String phoneNumber,
                                                   @PathVariable String code) {
        validatePhoneNumber(phoneNumber);

        smsService.checkSmsCode(phoneNumber, code);

        return ResponseEntity.ok().build();
    }
}
