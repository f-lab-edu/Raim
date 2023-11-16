package flab.project.service;

import flab.project.domain.Terms;
import flab.project.dto.TermResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TermService {

    private final TermRepository termRepository;

    public TermResponseDto getPolicyTerm(long id) {
        Terms findTerms = termRepository.findById(id).orElseThrow(() -> new KakaoException(ExceptionCode.TERM_NOT_FOUND));

        return TermResponseDto.of(findTerms);
    }

    public TermResponseDto getLocationTerm() {
        Terms findTerms = termRepository.findById(Terms.OPTIONAL_LOCATION_TERMS).orElse(new Terms());

        return TermResponseDto.of(findTerms);
    }
}
