# MEDIUM_MISSION
## 개발 기간
`23. 12. 04 ~ 23. 12. 15`

## 구현 기능
-[x] 게시글 기능 (MVP)
    -[x] CRUD
    -[x] 개인 보호글, 공개글
    -[x] 특정 회원의 글 조회    
  
-[ ] 회원 기능 (MVP)
    -[ ] 회원가입, 로그인, 로그아웃
    -[ ] JWT
-[ ] 댓글 기능
    -[ ] CRUD
-[x] 이미지 첨부 기능

## 참고사항(실행 순서)
1. git clone 후 최초 실행 전 `global/util/initData` 를 `Profile(!prod)` 로 바꾼 후 실행(테스트 데이터)
2. Application run 확인
3. global/util/initData 를 `Profile(!dev)` 로 변경 : 테스트 케이스 누적 방지 위함