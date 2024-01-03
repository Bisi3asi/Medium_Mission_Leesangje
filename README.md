# MEDIUM_MISSION
## 개발 기간
1차: `23. 12. 04 ~ 23. 12. 15`    
2차: `23. 12. 26 ~ 24. 01. 04`

## 구현 기능(1차)
- [x] 게시글 기능 (MVP)
    - [x] CRUD
    - [x] 개인 보호글, 공개글
    - [x] 특정 회원의 글 조회    
              
- [x] 회원 기능 (MVP)
    - [x] 회원가입, 로그인, 로그아웃
    - [x] JWT HTTP ONLY
                  
- [x] 댓글 기능
    - [x] CRUD
    - [x] 비공개 댓글(작성자 본인과 게시글 작성자만 열람 가능)
                           
- [x] 이미지 첨부 기능 (MVP)
    - [x] 미리보기 (썸네일) 이미지 수정, 추가
    
---
    
## 구현 기능(2차)
`필수 미션`
- [x] 회원 클래스에 유료 회원 필드 추가 (MVP)
- [x] 게시글 클래스에 유료 필드 추가 (MVP)
    - [x] 유료회원만 유료 게시글 조회 가능
    - [x] 유료회원만 유료 게시글 작성 가능   
- [x] 기본 테스트 데이터 init (100개) (MVP)
- [ ] 검색 필터링, 정렬 (QueryDSL)
- [x] 토스트 UI 마크다운 에디터 적용
    - [x] 뷰어 적용 ~~SSR, 테일윈드 마크다운 뷰어 적용 불가로 인해 직접 CSS 커스텀 작업~~
- [ ] 토스트 UI 에디터 이미지 파일 업로드 기능
- [x] OAUTH2 카카오 로그인
- [ ] 크로스 도메인 (Front, Back 분리)
- [x] 배포, 도메인 연결, Github Action, ~~Jenkins~~
- [ ] 배포, 도메인 연결, 쿠버네티스, 무중단 CI/CD
- [ ] 정산 기능 구현 (TOSS PAYMENT)

`별도 작업 내용`
- [x] UI Carousel, 앵커 기능
- [x] 회원정보, 마이페이지 구현

`todo`
- [ ] 이미지 Volume 분리로 인한 배포 시 영속성 부여
- [ ] 게시글 포스팅 전 썸네일 이미지 미리보기 기능
- [ ] 블로그 (내 게시글) 페이지 UI 조정
      
---
    
## 참고사항
`source code`
1. git clone 후 최초 실행 전 `global/util/initData` 를 `Profile(!prod)` 로 바꾼 후 실행(테스트 데이터)
2. Application run 확인
3. global/util/initData 를 `Profile(!dev)` 로 변경 : 테스트 케이스 누적 방지 위함
    
`domain URL` [여기 있습니다.](https://medium.bi3a.app)

