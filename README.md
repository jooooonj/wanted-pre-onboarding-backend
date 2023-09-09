# Board (Member, Post) API

## 이름 : 이재준

## GIT 컨벤션
- 1~7 과제를 이슈에 등록하고 이슈와 연결된 브랜치를 만들어서 작은 단위로 해결해나갔습니다.
- PR -> MERGE 방식을 통해 main브랜치 프로젝트의 완성도를 높였습니다.

## 애플리케이션 실행 방법
### LOCAL (docker-compose X)
- git clone
- src/main/resources 경로에 application-secret.yml 파일 생성 -> 환경변수 기입
  - dev_db_driver-class-name : 개발서버 db 드라이버
  - dev_db_driver-url : 개발서버 db url
  - dev_db_driver-username : 개발서버 db명
  - dev_db_driver-password : 개발서버 db 패스워드
  - dev_sever_port : 개발서버 포트
  - jwt_secretKey : JWT 토큰 암호화/복호화에 사용할 key

 - 실행

 ### LOCAL, PROD (docker-compose O)
- git clone
- src/main/resources 경로에 application-secret.yml 파일 생성 -> 환경변수 기입
  - ${prod_db_driver-class-name} : 운영서버 db 드라이버
  - ${prod_db_driver-url} : 운영서버 db url
  - ${prod_db_driver-username} : 운영서버 db명
  - ${prod_db_driver-password} : 운영서버 db 패스워드
  
- 프로젝트 하위 경로에 docker-compose.yml 환경변수 기입
  - ${MARIA_DATABASE} : db명
  - ${MARIADB_ROOT_HOST} :  root 계정 호스트
  - ${MARIADB_ROOT_PASSWORD} : root 계정 패스워드
  - ${prod_db_url} : db url
  - ${prod_db_username} : 위에 root계정으로 설정했기 때문에 root
  - ${prod_db_password} : db에 설정한 패스워드
  - ${jwt_secretKey} : JWT 토큰 암호화/복호화에 사용할 key

- 순서대로 cli 실행
  - chmod 744 gradlew  
  - ./gradlew claen build
  - docker compose up
 
### 엔드포인트 호출 방법
- 회원가입
 - 엔드포인트 : POST /api/member/join
 - request body : email, password
 - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/598d988f-f4e2-471e-a654-5c052f6e9f7b)

- 로그인
  - 엔드포인트 : POST /api/member/login
  - request body : email, password
  - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/a20e213a-774d-4036-aab8-0d28b43c8e82)

- 게시글 작성
  - 로그인 후 접근 가능
  - 엔드포인트 : POST /api/posts
  - request body : title, content
  - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/430cba22-b986-498e-885d-8a7bc31a90f7)


- 게시글 목록 조회
  - 엔드포인트 : GET /api/posts
  - request param : page, size 
  - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/9d3a9422-93df-4ee1-8910-b859d8201ee6)

- 게시글 단건 조회
  - 엔드포인트 : GET /api/posts/{id}
  - {id} : 조회하려는 게시글 id
  - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/edc598a8-497b-471f-8f58-58f234c14ced)

- 게시글 수정
  - 로그인 후 접근 가능
  - 엔드포인트 : PATCH /api/posts{id}
  - {id} : 수정하려는 게시글 id
  - request body : title, content
  - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/d5ce3d53-7600-4247-a0a9-2da3786de67e)

- 게시글 삭제
  - 로그인 후 접근 가능
  - 엔드포인트 : DELETE /api/posts/{id}
  - {id} : 삭제하려는 게시글 id
  - ![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/e5557346-a664-4e94-a97a-450558643b17)

---
 
## Table 구조
![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/ccdcd584-4a49-4c71-8e02-8e27d15f8694)

### MEMBER
  - bigint(20) id (PK) : Member를 식별할 수 있는 ID값
  - datetime(6) create_date : 회원 가입한 날짜
  - datetime(6) modify_date : 정보가 마지막으로 수정된 날짜
  - varchar(255) email (not null) : 사용자가 로그인에 사용할 이메일 
  - varchar(255) password (not null) : 사용자가 로그인에 사용할 비밀번호

### POST
  - bigint(20) id (PK) : POST를 식별할 수 있는 ID값
  - datetime(6) create_date : 게시글을 작성한 날짜
  - datetime(6) modify_date : 게시글을 마지막으로 수정한 날짜
  - varchar(255) title (not null) : 게시글 제목
  - varchar(255) content (not null) : 게시글 내용
  - bigint(20) member_id (FK) : 게시글 작성자의 ID값
 
---
 
## 구현한 API의 동작을 촬영한 데모 영상 링크
https://youtu.be/dAA3PYXUgg4

## 구현 방법 및 이유에 대한 간략한 설명
### 보안/인가 및 로그인,회원가입
- Spring Security를 통해 접근 관리
  - Spring Security를 통해 쉽고 간편하게 보안을 설정하고 접근 권한을 제어할 수 있습니다.
  - 회원가입, 로그인, 게시글 목록 조회, 게시글 단건 조회 엔드포인트는 누구나 접근 가능하고, 그 외는 권한 체크를 하도록 구현했습니다.
      
- JwtToken, JwtAuthorization, JwtTokenProvider 구현
  - 환경변수인 ${jwt.secretKey}를 가지고 JwtTokenProvider 객체는 JwtToken을 만들어서 사용자에게 제공하거나 사용자가 가지고온 JwtToken값을 복호화해 토큰값이 유효한지 검증합니다.
  - JwtAuthorization은 doFilterInternal을 오버라이딩해서 시큐리티가 제공하는 filter에서 역할을 수행하여 JwtToken값이 유효한지 검증에 성공해야만 접근 가능하도록 구현하였습니다.

- 회원가입
  - JoinMemberRequest(String email, String password)를 받아서 유효성 검증 후 회원가입을 시킵니다.
  - 유효성 검증은 Validation으로 진행하였고, 예외는 가독성을 위해 별도의 예외를 만들어서 GloberExceptionHandler로 관리해주었습니다. 
  - 비밀번호는 passwordEncoder를 사용하여 Bcrypt로 암호화합니다.
  - 컨트롤러에서 유효성 검증을 통과하면 서비스 계층에서 해당 이메일로 회원을 조회해봄으로써 중복된 이메일이 있는지 검증합니다.
  - 중복된 이메일이 없으면 회원가입에 성공합니다.

- 로그인
  - LoginMemberRequest를 통해 email과 password를 통해 요청을 받았습니다.
  - 유효성 검사는 회원가입과 마찬가지로 Validation을 통해 검증하였습니다.
  - 서비스 계층에서는 해당 email로 가입된 계정이 존재하는지 확인 후 비밀번호가 일치하는지 확인합니다.
  - 로그인 성공시 JwtToken을 발급합니다.
---
### 게시글 작성,조회,수정,삭제

- 게시글 작성
  - 접근자의 토큰을 검증하고 토큰이 존재하지 않거나, 만료시 접근이 불가합니다.
  - 접근에 성공했다면 Validation을 통해 제목과, 내용을 확인하고, 공백이 요청값으로 올 시에는 예외를 반환하도록 하였습니다.
  - 유효성 검증도 통과하면 접근자를 작성자로 하는 게시글을 생성합니다.
   
- 게시글 목록 조회
  - 목록 조회의 경우는 검증 없이도 접근 가능하도록 구현했습니다.
  - page와 size 정보를 받아서 게시글을 페이징처리 하여 응답합니다.
  - Entity의 캡슐화를 위해서 Repository 계층에서 데이터 조회와 동시에 PostResponse에 매핑하여 반환합니다.

- 게시글 단건 조회
  - 단건 조회의 경우는 검증 없이도 접근 가능하도록 구현했습니다.
  - id 값을 통해 해당 id 값을 가지고 있는 게시글을 조회합니다.
  - Entity의 캡슐화를 위해서 Repository 계층에서 데이터 조회와 동시에 PostResponse에 매핑하여 반환합니다.

- 게시글 수정
  - 토큰 검증 후 접근할 수 있습니다.
  - ModifyPostRequest와 id 값을 통해 해당 id 값을 가진 게시글을 수정할 수 있습니다.
  - 서비스 계층에서 게시글 작성자의 id와 수정을 요청한 회원의 id를 검증하여 게시글 작성자 본인만 게시글을 수정할 수 있도록 검증합니다.
  - Transactional의 dirty check 특징을 이용하여 게시글을 수정합니다.

- 게시글 삭제
  - 서비스 계층에서 게시글 작성자의 id와 수정을 요청한 회원의 id를 검증하여 게시글 작성자 본인만 게시글을 삭제할 수 있도록 검증합니다.
  - 게시글 수정에 사용한 메소드를 재사용합니다.

### 예외처리
- 예외는 가독성을 위해 ErrorCode와 ErrorResponse를 통해 각각 상황에 맞는 예외를 구현하였습니다.
- GlobalExceptionHandler 를 구현하여 단순히 예외를 던지지 않고 예외를 추적하는 로그를 찍은 후 규격에 맞는 예외 정보를 반환하도록 구현했습니다.

### 테스트
- 테스트는 Controller, Service, Repository가 각 역할에 대해 제대로 수행하는지 세분화하여 단위테스트로 작성했습니다.
- Controller에서는 API 요청시 접근 권한과, DTO의 유효성 검증을 올바르게 하는지와 검증이 통과한 후 실제 서비스 메소드를 호출하는지까지 테스트하였습니다.
- Service는 값 유효성 검증과 실제 로직이 제대로 수행되는지 테스트하였습니다.
- Repository는 직접 작성한 쿼리 위주로 데이터의 가공 및 조회가 제대로 수행되는지 테스트했습니다.

---

## API 명세(request/response 포함)

### 회원가입, 로그인 API

![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/ef169c93-d83e-4b92-910f-a1922e1de653)


### Post API
![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/eb46fc24-0794-44bc-a247-3b0699b792cd)
![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/6d1204e6-1358-4301-be3d-de8767ef6aa3)

## 클라우드 환경
- API 주소 : 49.50.173.42
![image](https://github.com/jooooonj/wanted-pre-onboarding-backend/assets/110995932/4f872d3c-9c23-47ee-8739-0453ea4a23a9)

