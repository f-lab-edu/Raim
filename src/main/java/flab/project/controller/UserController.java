package flab.project.controller;

import flab.project.dto.CommonResponseDto;
import flab.project.dto.EmailResponseDto;
import flab.project.dto.UserDto;
import flab.project.exception.ErrorResponse;
import flab.project.service.RequestHistoryService;
import flab.project.service.SmsService;
import flab.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static flab.project.util.ValidationUtils.validateEmail;
import static flab.project.util.ValidationUtils.validatePhoneNumber;

@Tag(name = "User API", description = "User 회원 가입 및 인증 등을 제공하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final SmsService smsService;
    private final RequestHistoryService requestHistoryService;

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @Parameter(name = "UserDto", description = "User 회원 가입을 위한 데이터.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공."),
            @ApiResponse(responseCode = "400", description = "요청 형식이 올바르지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 유효 기간을 지났습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "인증이 필요합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "인증 기록이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 가입된 유저입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public void registration(HttpServletRequest request,
                             @RequestParam
                             @Parameter(description = "이메일 암호키", in = ParameterIn.QUERY)
                             String emailVerification,
                             @RequestParam
                             @Parameter(description = "SMS 암호키", in = ParameterIn.QUERY)
                             String smsVerification,
                             @RequestBody UserDto userDto) {
        HttpSession session = request.getSession();
        requestHistoryService.checkAccessRequest(session.getId());

        userDto.validUserDto();

        userService.registrationUser(userDto, emailVerification, smsVerification);
    }

    @Operation(summary = "이메일 중복 확인", description = "이미 가입된 이메일인지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복된 메일 없음."),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/available/{email}")
    public ResponseEntity availableEmail(
            @PathVariable
            @Parameter(description = "이메일", in = ParameterIn.PATH, example = "asd123@asd.com") String email) {
        validateEmail(email);

        return ResponseEntity.ok().header("email-verification-key", userService.availableEmail(email)).build();
    }

    @Operation(summary = "이메일 찾기", description = "휴대전화 번호로 가입된 이메일을 찾습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일을 찾았습니다."),
            @ApiResponse(responseCode = "400", description = "휴대전화 형식이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "가입된 이메일이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/find")
    public CommonResponseDto<EmailResponseDto> findEmail(
            @RequestParam("phoneNumber")
            @Parameter(description = "전화번호", in = ParameterIn.QUERY, example = "01012345678")
            String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        String email = userService.findEmail(phoneNumber);

        return CommonResponseDto.of("", EmailResponseDto.of(email));
    }

    @Operation(summary = "SMS 인증 코드 보내기", description = "SMS 인증 문자를 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SMS 인증 문자를 보냈습니다."),
            @ApiResponse(responseCode = "400", description = "휴대전화 형식이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버가 요청을 처리하지 못했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{phoneNumber}")
    public void sendSmsCode(
            @PathVariable
            @Parameter(description = "전화번호", in = ParameterIn.PATH, example = "01012345678")
            String phoneNumber) {
        validatePhoneNumber(phoneNumber);

        smsService.sendAuthenticationSms(phoneNumber);
    }

    @Operation(summary = "SMS 인증하기", description = "SMS 인증 코드로 인증을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "휴대전화 인증되었습니다."),
            @ApiResponse(responseCode = "400", description = "휴대전화 형식이 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "휴대전화 인증을 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "휴대전화 인증 기록이 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

    })
    @GetMapping("/{phoneNumber}/{code}")
    public ResponseEntity checkSmsCode(@PathVariable
                                       @Parameter(description = "전화번호", in = ParameterIn.PATH, example = "01012345678")
                                       String phoneNumber,
                                       @PathVariable
                                       @Parameter(description = "인증번호", in = ParameterIn.PATH, example = "123456")
                                       String code) {
        validatePhoneNumber(phoneNumber);

        return ResponseEntity.ok().header("sms-verification-key", smsService.checkSmsCode(phoneNumber, code)).build();
    }
}
