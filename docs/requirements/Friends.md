# 친구 관리 기능

## 구현 기능
- [x] 친구 추가
  - [x] 휴대전화
  - [x] email
  - [x] response 만들기
- [ ] 친구 목록
  - [ ] 목록 조회 시 N + 1 문제 해결하기
  - [ ] 숨김 친구와 차단 친구 삭제된 친구는 불러오지 않도록 하기
- [ ] 친구 관리 # /api/friend/{friendId}?friendMode=[ACTIVE, HIDDEN, BLOCK, DELETED]
  - [x] 숨김 친구 # HIDDEN
  - [x] 차단 친구 # BLOCK
    - [ ] 차단된 친구한테는 메시지를 받지 않는다(개인 채팅은 전송되지 않음, 단체 방에선 전송됨)
  - [x] 친구 삭제 # DELETED
    - [x] 숨김 친구만 삭제 가능 #Friend#checkDeletableFriend
    - [x] 이미 삭제된 친구인지 확인 #Friend#checkAlreadyDeleted
    - [ ] 일정 기간 후 친구 삭제 
      - [x] 삭제 일시 기록
  - [x] 친구 ID request로 들어왔을 때 현재 친구 Entity의 User가 로그인한 User가 맞는지 확인 #FriendService#checkUserFriend
- [ ] 일반 int형 id response 시 암호화