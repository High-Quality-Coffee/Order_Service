<img src="https://github.com/user-attachments/assets/c3491418-e133-41c7-8f85-97dd70589fb4" alt="서비스 소개" width="100%"/>


<br/>
<br/>

# 0. Getting Started (서비스 구성 및 실행 방법)
BE 배포 링크 : https://order-service.store

<br/>
<br/>

# 1. About Project
- 프로젝트 목적: 음식 주문 관리 서비스 개발
- 프로젝트 설명: 기존에는 음식점에서 전화로 주문을 받고 종이에 직접 기록하는 방식이 많았지만 이제는 대부분의 주문이 온라인으로 이루어지고 있습니다.
이를 반영하여, 효율적인 주문 관리와 처리를 위한 웹 서비스를 구현하려고 했습니다!!

<br/>
<img src="https://github.com/user-attachments/assets/87fa39f2-bcb3-4e73-94f1-082e49300ea3" alt="project-architecture">


<br/>
<br/>

# 2. Team14 Members (팀원 및 팀 소개)
| 박규원 | 조수빈 | 김소진 | 임규진 |
|:------:|:------:|:------:|:------:|
| <img src="https://avatars.githubusercontent.com/u/125748258?v=4" alt="박규원" width="150"> | <img src="https://avatars.githubusercontent.com/u/97503991?v=4" alt="조수빈" width="150"> | <img src="https://avatars.githubusercontent.com/u/170385509?v=4" alt="김소진" width="150"> | <img src="https://avatars.githubusercontent.com/u/113866973?v=4" alt="임규진" width="150"> |
| Lead | BE | BE | BE |
| [GitHub](https://github.com/High-Quality-Coffee) | [GitHub](https://github.com/chobeebee) | [GitHub](https://github.com/sojinnuna) | [GitHub](https://github.com/kylim99) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **권한별 회원 관리**:
  - 권한별 회원가입을 진행합니다
  - 권한은 USER(고객), MASTER(관리자), OWNER(가게주인), MANAGER(가게매니저)로 분류되어 있습니다
  - 로그인이 진행되고, 필터 단에서 유저의 권한을 확인하여 토큰을 발급합니다.
  - 발급된 토큰을 기반으로 권한별 API를 처리합니다.
  - Security Config에서 권한을 처리합니다.

- **배송지**:
  - 고객별 배송지를 등록,조회,수정,삭제할 수 있습니다.
  - 배송지별 요청사항을 따로 관리하여 배달 주문시 요청사항을 작성하고, 조회할 수 있습니다.

- **주문**:
  - 주문 취소: 주문 생성 후 5분 이내에만 취소 가능하도록 제한합니다
  - 주문 유형: 온라인 주문과 대면 주문(가게에서 직접 주문) 모두 지원합니다.
  - 대면 주문 처리: 가게 사장님이 직접 대면 주문을 접수합니다.
  
- **결제**:
  - 결제 방식: 카드 결제만 가능하도록 구현합니다
  - PG사 연동: PG사와의 결제 연동은 외주 개발로 진행하며, 결제 관련 내역만 플랫폼의 데이터베이스에 저장합니다.
  - 결제 테이블: 결제 내역을 저장하기 위한 전용 테이블 설계하여 관리합니다.

- **상품**:
  - 상품은 가게별로 정해진 형식을 따라 등록할 수 있도록 구현합니다.
  - 주문한 아이템 테이블을 따로 생성하여 주문내역을 관리합니다.
  - 상품테이블의 정보를 기반으로 주문이 들어오면 주문한 아이템 테이블을 생성합니다.

- **리뷰**:
  - 주문한 내역에 대해서만 리뷰가 가능합니다.
  - 리뷰를 등록하면 별점과 리뷰내역이 리뷰테이블에 저장됩니다.
  - 리뷰가 업데이트 되면, 해당 업체의 총 리뷰 개수는 1이 증가되며, 리뷰 평점이 자동으로 계산됩니다.
  - 해당 업체의 모든 리뷰를 불러와서 리뷰를 새로 계산하는 것이 아닌, 이미 계산된 평점에서 새로운 리뷰 1개를 불러와서 평균 리뷰점수를 업데이트 합니다.
 
- **업체**:
  - 한식, 중식, 분식, 치킨, 피자 카테고리로 분류하였습니다.
  - 추후에 음식점 카테고리를 추가하거나 수정할 수 있도록 하였습니다.
  - 초기에는 광화문 근처로 한정하여 운영하며, 향후 확장을 고려한 지역 분류 시스템을 설계하였습니다.
  - 지역별 필터링, 지역정보 수정 및 추가 등이 가능 하도록 했습니다
 
- **AI API 연동**
  - **상품 설명 자동 생성:** AI API를 연동하여 가게 사장님이 상품 설명을 쉽게 작성할 수 있도록 지원하였습니다.
  - **AI 요청 기록:** AI API 요청 질문과 대답은 모두 데이터베이스에 저장하였습니다.
 
- **데이터 보존 및 삭제 처리**
  - **데이터 보존:** 모든 데이터는 완전 삭제되지 않고 숨김 처리로 관리하였습니다.
  - **상품 숨김:** 개별 상품도 숨김 처리 가능하도록 구현(숨김과 삭제는 다른 기능)하였습니다
  - **데이터 감사 로그:** 모든 정보에 생성일, 생성 아이디, 수정일, 수정 아이디, 삭제일, 삭제 아이디를 포함하였습니다.


<br/>
<br/>

# 4. ERD
erdcloud : https://www.erdcloud.com/d/sT8WDsAXXqfbB9Gay
<br/>
<br/>
<img src="https://github.com/user-attachments/assets/22d04d68-7885-4a56-b2ff-2aef93f95232" alt="erd">


<br/>
<br/>



# 5. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 박규원    |  <img src="https://avatars.githubusercontent.com/u/125748258?v=4" alt="박규원" width="100"> | <ul><li>프로젝트 계획 및 관리</li><li>팀 리딩 및 커뮤니케이션</li><li>JWT 기반 회원 도메인 관리</li><li>배송지 관리</li><li>인프라 전담 관리</li></ul>     |
| 조수빈   |  <img src="https://avatars.githubusercontent.com/u/97503991?v=4" alt="조수빈" width="100">| <ul><li>상품 서비스 개발</li><li>리뷰 서비스 개발</li><li>주문, 업체 서비스와의 통신을 위한 RestTemplate 구축</li></ul> |
| 김소진   |  <img src="https://avatars.githubusercontent.com/u/170385509?v=4" alt="김소진" width="100">|  <ul><li>업체 서비스 개발</li><li>카테고리별 업체 관리</li><li>지역별 업체 관리</li> |
| 임규진    |  <img src="https://avatars.githubusercontent.com/u/113866973?v=4" alt="임규진" width="100">| <ul><li>주문 서비스 개발</li><li>결제 서비스 개발</li><li>AI 상품 설명 기능 개발</li></ul>  |

<br/>
<br/>

# 6. Technology Stack (기술 스택)

## 6.1 BackEnd
|  |  |  |
|-----------------|-----------------|-----------------|
| SpringBoot    |  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white" alt="SpringBoot" width="200"> | 3.4.3    |
| Java    |  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white" alt="Java" width="200" > | 17 |
| Spring Data JPA    |  <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=SpringDataJPA&logoColor=white" alt="JPA" width="200" >    | 5.0.0  |
| QueryDSL    |  <img src="https://img.shields.io/badge/QueryDSL-0769AD?style=for-the-badge&logo=QueryDSL&logoColor=white" alt="QueryDSL" alt="QueryDSL" width="200" >    | 1.11.12    |
| Spring Security |  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white" alt="QueryDSL" alt="QueryDSL" width="200">    | 3.4.2    |

<br/>

## 6.2 Infra
|  |  |  |
|-----------------|-----------------|-----------------|
| PostgreSQL  |  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=PostgreSQL&logoColor=white" alt="PostgreSQL" width="200">    | 16.3 |
| pgAdmin    |  <img src="https://img.shields.io/badge/pgAdmin-4169E1?style=for-the-badge&logo=pgAdmin&logoColor=white" alt="pgAdmin" width="200">    | latest  |
| AWS ec2    |  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white" alt="ec2" width="200">    | |
| AWS RDS    |  <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=AmazonRDS&logoColor=white" alt="rds" width="200">    | |
| Nginx Proxy Manager |  <img src="https://img.shields.io/badge/Nginx Proxy Manager-F15833?style=for-the-badge&logo=NginxProxyManager&logoColor=white" alt="Firebase" width="200">    | latest |
| Docker  |  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white" alt="docker" width="200">    |  |

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
참고 : https://hqc24.tistory.com/9
<br/>
<br/>
```

[feat]: 회원탈퇴 기능 수정

[목적]: 기존 hard-delete 방식 대신, soft-delete 방식을 적용하여 기능 수정을 하기 위해서.  

[목표]: soft-delete 방식으로 회원탈퇴를 구현하여, 유저가 물리적으로 삭제되는 것이 아닌,
	   상태값으로 탈퇴여부를 관리하여, 유저 계정 복구에 대비.  

[달성도]: 

  - soft-delete 구현 완료.  

  - 멤버 테이블에 탈퇴여부 상태값 추가 완료.  

[기타]: DB 과부화를 방지하기 위해서, 탈퇴 회원은 30일 이후 자동 삭제되도록 처리할 필요가 있음.

```


<br/>
<br/>
