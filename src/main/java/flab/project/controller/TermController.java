package flab.project.controller;

import flab.project.dto.CommonResponseDto;
import flab.project.dto.TermResponseDto;
import flab.project.service.TermService;
import flab.project.util.ValidationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Terms API", description = "약관 내용을 제공하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/term")
public class TermController {

    private final TermService termService;

    @GetMapping("/policy")
    public CommonResponseDto<TermResponseDto> getTerm(@RequestParam long policyId) {
        return CommonResponseDto.of("약관 데이터", termService.getPolicyTerm(policyId));
    }

    @GetMapping("/location")
    public CommonResponseDto<TermResponseDto> getLocation() {

        return CommonResponseDto.of("약관 데이터", termService.getLocationTerm());
    }
}
