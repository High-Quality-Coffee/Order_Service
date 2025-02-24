<img src="" alt="배너" width="100%"/>

<br/>
<br/>

# 0. Getting Started (서비스 구성 및 실행 방법)
https://order-service.store

<br/>
<br/>

# 1. About Project (프로젝트 설명)
- 프로젝트 목적: 음식 주문 관리 서비스 개발
- 프로젝트 설명: 기존에는 음식점에서 전화로 주문을 받고 종이에 직접 기록하는 방식이 많았지만 이제는 대부분의 주문이 온라인으로 이루어지고 있습니다.
이를 반영하여, 효율적인 주문 관리와 처리를 위한 웹 서비스를 구현하려고 했습니다!!

<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 박규원 | 조수빈 | 김소진 | 임규진 |
|:------:|:------:|:------:|:------:|
| <img src="https://avatars.githubusercontent.com/u/125748258?v=4" alt="박규원" width="150"> | <img src="https://avatars.githubusercontent.com/u/97503991?v=4" alt="조수빈" width="150"> | <img src="https://avatars.githubusercontent.com/u/170385509?v=4" alt="김소진" width="150"> | <img src="https://avatars.githubusercontent.com/u/113866973?v=4" alt="임규진" width="150"> |
| Lead | BE | BE | BE |
| [GitHub](https://github.com/High-Quality-Coffee) | [GitHub](https://github.com/chobeebee) | [GitHub](https://github.com/sojinnuna) | [GitHub](https://github.com/kylim99) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **권한별 회원 관리**:
  - 회원가입 시 DB에 유저정보가 등록됩니다.

- **배송지**:
  - 사용자 인증 정보를 통해 로그인합니다.

- **주문**:
  - 캘린더 UI를 통해 동아리 관련 일정 추가&삭제가 가능합니다.
  - 체크박스를 통해 종료되거나 이미 수행한 일정을 표시할 수 있습니다.

- **결제**:
  - 대학 내 동아리를 검색할 수 있습니다.
  - 검색 시 해당 동아리가 업로드한 홍보글이 보여집니다.

- **상품**:
  - 홍보글 등록을 통해 동아리를 홍보할 수 있습니다.

- **리뷰**:
  - 새로운 동아리를 만들어 관리할 수 있습니다.


<br/>
<br/>

# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 박규원    |  <img src="https://avatars.githubusercontent.com/u/125748258?v=4" alt="박규원" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>팀 리딩 및 커뮤니케이션</li><li>JWT 기반 회원 도메인 관리</li><li>배송지 관리</li><li>인프라 전담 관리</li></ul>     |
| 조수빈   |  <img src="https://avatars.githubusercontent.com/u/97503991?v=4" alt="조수빈" width="100">| <ul><li>메인 페이지 개발</li><li>동아리 만들기 페이지 개발</li><li>커스텀훅 개발</li></ul> |
| 김소진   |  <img src="https://avatars.githubusercontent.com/u/170385509?v=4" alt="김소진" width="100">    |<ul><li>홈 페이지 개발</li><li>로그인 페이지 개발</li><li>동아리 찾기 페이지 개발</li><li>동아리 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>  |
| 임규진    |  <img src="https://avatars.githubusercontent.com/u/113866973?v=4" alt="임규진" width="100">    | <ul><li>회원가입 페이지 개발</li><li>마이 프로필 페이지 개발</li><li>커스텀훅 개발</li></ul>    |

<br/>
<br/>

# 5. Technology Stack (기술 스택)

## 5.2 BackEnd
|  |  |  |
|-----------------|-----------------|-----------------|
| React    |  <img src="https://github.com/user-attachments/assets/e3b49dbb-981b-4804-acf9-012c854a2fd2" alt="React" width="100"> | 18.3.1    |
| StyledComponents    |  <img src="https://github.com/user-attachments/assets/c9b26078-5d79-40cc-b120-69d9b3882786" alt="StyledComponents" width="100">| 6.1.12   |
| MaterialUI    |  <img src="https://github.com/user-attachments/assets/75a46fa7-ebc0-4a9d-b648-c589f87c4b55" alt="MUI" width="100">    | 5.0.0  |
| DayJs    |  <img src="https://github.com/user-attachments/assets/3632d7d6-8d43-4dd5-ba7a-501a2bc3a3e4" alt="DayJs" width="100">    | 1.11.12    |

<br/>

## 5.3 Infra
|  |  |  |
|-----------------|-----------------|-----------------|
| Firebase    |  <img src="https://github.com/user-attachments/assets/1694e458-9bb0-4a0b-8fe6-8efc6e675fa1" alt="Firebase" width="100">    | 10.12.5    |

<br/>

## 5.4 Version & Communication
|  |  |
|-----------------|-----------------|
| Git    |  <img src="https://github.com/user-attachments/assets/483abc38-ed4d-487c-b43a-3963b33430e6" alt="git" width="100">    |
| Git Kraken    |  <img src="https://github.com/user-attachments/assets/32c615cb-7bc0-45cd-91ea-0d1450bfc8a9" alt="git kraken" width="100">    |
| Notion    |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="100">    |

<br/>
<br/>

# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
우리의 브랜치 전략은 아래와 같은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

<img src="https://github.com/user-attachments/assets/f683ef08-c485-4447-a1d8-a5c6468e093a" alt="git flow" width="500">


- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 이 브랜치에서 이루어집니다.
  
- develop Branch
  - 통합 기능 관리 브랜치 입니다
  - feat에서 개발한 기능을 develop 브랜치에서 통합하여 관리합니다.
 
- feat Branch
  - 기능 개발 브랜치 입니다.
  - 기능 단위로 브랜치를 나누어 기능을 개발하였습니다.
 
- refactor Branch
  - 코드 리팩토링 브랜치 입니다.
  - 코드 리팩토링이 필요한 경우 refactor 브랜치에서 작업했습니다.
 
- release Branch
  - 배포 전 버전을 관리하는 브랜치 입니다.
  - 최종 배포하기 전 테스트를 진행하고, 이상이 없다면 Main브랜치로 배포를 진행합니다.
 
- hotfix Branch
  - 핫픽스를 관리하는 브랜치 입니다.
  - 배포된 환경에서 수정사항이 발생했을 경우, hotfix 브랜치에서 관리하였습니다.

<br/>
<br/>

# 8. Convention

## 네이밍 컨벤션
함수명은 카멜 케이스를 기본으로 하고 컬럼명은 스네이크케이스를 기본으로 한다.
```
// 카멜 케이스
camelCase
// 스네이크 케이스
snake_case
```

<br/>

<br/>
<br/>

# 9. 커밋 컨벤션


<br/>
<br/>
