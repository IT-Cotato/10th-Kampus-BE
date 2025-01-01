# Kampus Backend

## 레이어 구조

다음 네 가지 레이어로 구성하며, 레이어는 **위에서 아래로만** 참조합니다.

1. **Presentation Layer**
    - 외부 변화(HTTP 요청, UI 변경 등)에 민감한 레이어
    - Controller, 요청/응답 DTO 등이 속함
    - 예) `PostController`

2. **Business Layer**
    - 비즈니스 로직을 담는 레이어
    - 예) `PostService`

3. **Implement Layer**
    - 비즈니스 로직 구현에 필요한 상세 로직을 담당하는 레이어
    - 재사용성과 완결성이 높은 **도구 클래스**들이 존재
    - 예) `PostAppender`, `PostReader`, `PostProcessor`, `PostBatchProcessor`

4. **Data Access Layer**
    - 다양한 자원(DB, 외부 API 등)에 대한 접근을 담당하는 레이어
    - 기술 의존성을 최대한 분리하여 **순수한 인터페이스**를 제공

---

## 레이어 간 규칙

1. **단방향 의존(순방향)**
    - 레이어는 **위에서 아래로만** 참조합니다.
    - 예: `Controller` → `Service` → `Implement` → `DAO`

2. **역방향 참조 금지**
    - 예: `UserService`가 `UserController`를 참조하면 안 됩니다.

3. **하위 레이어 건너뛰기 금지**
    - 예: `Service`(Business Layer)에서 바로 `DAO`(Data Access Layer)를 호출하지 않도록 합니다.
    - 구현 기술에 대한 세부사항은 `Implement Layer`에서 해결하고, `Business Layer`는 구현 로직을 직접 알 필요가 없습니다.

4. **동일 레이어 간 참조 금지**
    - 단, **Implement Layer**는 예외적으로 서로 다른 구현체 간 협력을 허용합니다.
    - 이는 재사용성과 완결성이 높은 도구 클래스들을 만들기 위함입니다.

---

## 예제 코드 설명

### PostController (Presentation Layer)
```java
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<DataResponse<SinglePostResponse>> findSinglePostById(@PathVariable Long postId) {
        PostConcept singlePost = postService.findSinglePost(postId);
        return ResponseEntity.ok(DataResponse.from(SinglePostResponse.from(singlePost)));
    }

    @PostMapping("/single")
    public ResponseEntity<DataResponse<?>> createSinglePost(@RequestBody @Valid NewPostRequest request) {
        postService.createSinglePost(request.content(), request.title(), request.name());
        return ResponseEntity.ok(DataResponse.ok());
    }

    // 기타 API ...
}
```
## 디렉토리 구조 예시
```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── spring
    │   │           └── guide
    │   │               ├── ApiApp.java
    │   │               ├── SampleApi.java
    │   │               ├── domain
    │   │               │   ├── coupon
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   ├── member
    │   │               │   │   ├── api
    │   │               │   │   ├── application
    │   │               │   │   ├── dao
    │   │               │   │   ├── domain
    │   │               │   │   ├── dto
    │   │               │   │   └── exception
    │   │               │   └── model
    │   │               │       ├── Address.java
    │   │               │       ├── Email.java
    │   │               │       └── Name.java
    │   │               ├── global
    │   │               │   ├── common
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   ├── config
    │   │               │   │   ├── SwaggerConfig.java
    │   │               │   │   ├── properties
    │   │               │   │   ├── resttemplate
    │   │               │   │   └── security
    │   │               │   ├── error
    │   │               │   │   ├── ErrorResponse.java
    │   │               │   │   ├── GlobalExceptionHandler.java
    │   │               │   │   └── exception
    │   │               │   └── util
    │   │               └── infra
    │   │                   ├── email
    │   │                   └── sms
    │   │                       ├── AmazonSmsClient.java
    │   │                       ├── SmsClient.java
    │   │                       └── dto
    │   └── resources
    │       ├── application-dev.yml
    │       ├── application-local.yml
    │       ├── application-prod.yml
    │       └── application.yml
```
- 크게 domain, global, infra 패키지로 구분하고, 세부 도메인에 따라 파일을 나누어 관리합니다.
- 각 도메인(coupon, member 등) 내부에 api(Controller), application(Service), dao, domain(Entity), dto, exception 등을 배치해 레이어 간 관심사를 명확히 분리하고 있습니다.
## 협업 가이드
### 이슈 기반 브랜치 전략
작업할 이슈를 생성하고, 해당 이슈에 대한 브랜치를 만듭니다.
브랜치 생성에 대한 가이드는 다음 문서를 참고하세요.
https://github.com/cheese10yun/github-project-management?tab=readme-ov-file#issue-%EA%B8%B0%EB%B0%98-branch-%EC%83%9D%EC%84%B1

기능 구현이 끝나면 Pull Request를 생성하여 리뷰 후 머지합니다.
### 커밋 메시지
이슈 번호를 명시하고, 작업 내용을 간략하게 서술합니다.
#### 예시 
```feat(#123): 게시글 단건 조회 기능 추가```
