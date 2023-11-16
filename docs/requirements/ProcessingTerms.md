# 약관 처리 기능

## 엔티티 고민하기
- 유저는 여러 약관에 동의한다.
- 약관은 여러 유저들에게 동의 된다.
- 유저와 약관은 N:M 관계
- JPA를 사용할 때 ManyToMany는 권장되지 않는다.
- 연결 테이블 전략을 활용해 유저 - 유저 동의 - 약관으로 테이블 분리
- 유저 - 유저 동의는 1:N
- 약관 - 유저 동의는 1:M

## 기능 요구 사항
- [x] UserDto의 body로 동의된 약관 사항들을 입력받는다.
  - [x] requiredTerms1: boolean
  - [x] requiredTerms2: boolean
  - [x] OptionalLocationTerm: boolean
- [x] 필수 약관을 동의하지 않으면 예외가 발생한다.
- [x] 필수 약관 동의를 저장해야 한다.
- [x] 선택 약관 동의를 저장해야 한다.
- [x] 약관을 보여줘야 한다.