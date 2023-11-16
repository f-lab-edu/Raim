package flab.project.service;

import flab.project.domain.Terms;
import flab.project.domain.User;
import flab.project.domain.UserAgreement;
import flab.project.domain.UserAgreement.UserAgreementBuilder;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.TermRepository;
import flab.project.repository.UserAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAgreementService {

    private final UserAgreementRepository userAgreementRepository;
    private final TermRepository termRepository;

    public void setUserEssentialTerms(User user) {
        setUserAgreementTerms(user, Terms.ESSENTIAL_TERMS_1);
        setUserAgreementTerms(user, Terms.ESSENTIAL_TERMS_2);
    }

    public void setUserLocationTerms(User user) {
        setUserAgreementTerms(user, Terms.OPTIONAL_LOCATION_TERMS);
    }

    private void setUserAgreementTerms(User user, long termsId) {
        Terms findTerms = termRepository.findById(termsId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.TERM_NOT_FOUND));

        UserAgreement userAgreement = UserAgreement.builder()
                .user(user)
                .terms(findTerms)
                .build();

        userAgreementRepository.save(userAgreement);
    }
}
