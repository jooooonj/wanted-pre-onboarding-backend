# wanted-pre-onboarding-backend

## 이름 : 이재준

## 애플리케이션 실행 방법
- git clone -> application-secret.yml 파일 추가 (환경변수)
- 환경변수
  - ${dev.db.driver-class-name} : 개발서버 db driver
  - ${dev.db.url} : 개발서버 db url
  - ${dev.db.username} : 개발서버 db username
  - ${dev.db.password} : 개발서버 db password-
  - ${prod.db.driver-class-name} : 운영서버 db driver
  - ${prod.db.url} : 운영서버 db url
  - ${prod.db.username} : 운영서버 db username
  - ${prod.db.password} : 운영서버 db password
  - ${jwt.secretKey} : jwtToken 암호화,복호화에 사용할 시크릿키
 
---
 
## Table 구조
- Member
  - bigint(20) id (PK)
  - datetime(6) create_date
  - datetime(6) modify_date
  - varchar(255) email (not null)
  - varchar(255) password (not null)

- Post
  - bigint(20) id (PK)
  - datetime(6) create_date
  - datetime(6) modify_date
  - varchar(255) title (not null)
  - varchar(255) content (not null)
  - bigint(20) member_id (FK)
 
### 회원 (MEMBER)
id: 식별 가능한 ID값
email: 이메일 
user_password: 비밀번호

### 게시글 (POST)
id: 식별 가능한 ID값
title : 제목
content: 내용
member_id : 게시글 작성자 id (MEMBER와 N:1 관계)

---
 
## 구현한 API의 동작을 촬영한 데모 영상 링크



## 구현 방법 및 이유에 대한 간략한 설명
### 로그인,회원가입
- Spring Security를 통해 접근 관리
  - Spring Security를 통해 쉽고 간편하게 보안을 설정하고 접근 권한을 제어할 수 있습니다.
  - - 회원가입, 로그인, 게시글 목록 조회, 게시글 단건 조회 엔드포인트는 누구나 접근 가능하고, 그 외는 권한 체크를 하도록 구현했습니다.
      
- JwtToken, JwtAuthorization, JwtTokenProvider 구현
  - 환경변수인 ${jwt.secretKey}를 가지고 JwtTokenProvider 객체는 JwtToken을 만들어서 사용자에게 제공하거나 사용자가 가지고온 JwtToken값을 복호화해 유효한지 검증합니다.
  - JwtAuthorization은 doFilterInternal을 오버라이딩해서 시큐리티가 제공하는 filter에서 역할을 수행하여 JwtToken값을 검증할 수 있게끔 구현했습니다.

- 회원가입
  - JoinMemberRequest(String email, String password)를 받아서 유효성 검증 후 회원가입을 시킵니다.
    - 유효성 검증은 Validation으로 진행하였고, 예외는 IllegalArgumentException로 통일하고 메시지로 식별 가능하도록 했습니다.
  - 비밀번호는 passwordEncoder를 사용하여 Bcrypt로 암호화합니다.

  - 컨트롤러에서 유효성 검증을 통과하면 서비스 계층에서 해당 이메일로 회원을 조회해봄으로써 중복된 이메일이 있는지 검증합니다.

- 로그인
  - LoginMemberRequest를 통해 email과 password를 통해 요청을 받았습니다.
    - 유효성 검사는 회원가입과 마찬가지로 Validation을 통해 검증하였습니다.

  - 로그인에 성공하면 JwtToken을 발급합니다.

---
### 게시글

- 게시글 작성
  - CreatePostRequest로 title과 content를 받아서 게시글 작성을 처리합니다.
    - 공백이 요청값으로 올 시에는 예외를 반환하도록 하였습니다.
    - JwtToken을 가지고 있는지 확인하여 접근을 제어합니다.
    - 접근이 허가되면 @AuthenticationPrincipal를 통해 실제 요청을 보낸 사용자의 정보를 얻어서 게시글을 생성할때 작성자로 처리합니다.
   
- 게시글 목록 조회
  - page와 size 정보를 받아서 게시글을 페이징처리 하여 응답합니다.
  - Entity의 캡슐화를 위해서 Repository 계층에서 데이터 조회와 동시에 PostResponse에 매핑하여 반환합니다.

- 게시글 단건 조회
  - id 값을 통해 해당 id 값을 가지고 있는 게시글을 조회합니다.
  -  Entity의 캡슐화를 위해서 Repository 계층에서 데이터 조회와 동시에 PostResponse에 매핑하여 반환합니다.

- 게시글 수정
  - ModifyPostRequest와 id 값을 통해 해당 id 값을 가진 게시글을 수정할 수 있습니다.
  - 서비스 계층에서 게시글 작성자의 id와 수정을 요청한 회원의 id를 검증하여 게시글 작성자 본인만 게시글을 수정할 수 있도록 검증합니다.
  - Transactional의 dirty check 특징을 이용하여 게시글을 수정합니다.

- 게시글 삭제
  - 서비스 계층에서 게시글 작성자의 id와 수정을 요청한 회원의 id를 검증하여 게시글 작성자 본인만 게시글을 삭제할 수 있도록 검증합니다.
  - 게시글 수정에 사용한 메소드를 재사용합니다.

---

## API 명세(request/response 포함)

### 회원가입, 로그인 API

![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/12626d15-a816-49e3-8a96-212821f8472c)

### Post API
![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/63c2ebf2-854c-41a2-95bb-7cd819e0906a)



