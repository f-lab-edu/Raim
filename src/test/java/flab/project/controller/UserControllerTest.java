package flab.project.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.dto.UserDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.service.SmsService;
import flab.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SmsService smsService;

    @Test
    void 회원가입() throws Exception {

        UserDto userDto = UserDto.builder()
                .password("asdf1234!")
                .confirmPassword("asdf1234!")
                .name("김개발")
                .nickname("개발남")
                .essentialTerms1(true)
                .essentialTerms2(true)
                .optionalLocationTerms(true)
                .build();

        Mockito.doNothing().when(userService).registrationUser(userDto, "이메일검증키", "SMS검증키");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .param("emailVerification", "이메일검증키")
                        .param("smsVerification", "SMS검증키")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void SMS_인증_확인() throws Exception {

        Mockito.when(smsService.checkSmsCode("01012345678", "123456")).thenReturn("SMS검증키");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/01012345678/123456"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("sms-verification-key", "SMS검증키"));
    }

    @Test
    void SMS_인증_실패() throws Exception {

        Mockito.when(smsService.checkSmsCode("01012345678", "123456"))
                .thenThrow(new KakaoException(ExceptionCode.SMS_CODE_NOT_MATCH));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/01012345678/123456"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(10010))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("인증번호가 올바르지 않습니다."));
    }

    @Test
    void SMS_인증_메일_보내기() throws Exception {

        Mockito.doNothing().when(smsService).sendAuthenticationSms("01012345678");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/01012345678"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void SMS_인증_메일_보내기_실패() throws Exception {
        Mockito.doThrow(new KakaoException(ExceptionCode.API_FAIL)).when(smsService)
                .sendAuthenticationSms("01012345678");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/01012345678"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(70000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("응답할 수 없습니다."));
    }

    @Test
    void 이메일_존재하지_않음() throws Exception {

        Mockito.when(userService.findEmail("01012345678")).thenThrow(new KakaoException(ExceptionCode.EMAIL_NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/find")
                        .param("phoneNumber", "01012345678"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(10003))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }

    @Test
    void 이메일_찾기() throws Exception {

        Mockito.when(userService.findEmail("01012345678")).thenReturn("asd@asd.com");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/find")
                        .param("phoneNumber", "01012345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("asd@asd.com"));
    }

    @Test
    void 유저_회원가입_불가능() throws Exception {
        Mockito.when(userService.availableEmail("asd@asd-asd.com"))
                .thenThrow(new KakaoException(ExceptionCode.DUPLICATED_EMAIL));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/available/asd@asd-asd.com"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(10002))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("사용할 수 없는 이메일입니다."));
    }

    @Test
    void 유저_회원가입_가능() throws Exception {

        Mockito.when(userService.availableEmail("asd@asd.com")).thenReturn("이메일검증키");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/available/asd@asd.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("email-verification-key", "이메일검증키"));
    }
}