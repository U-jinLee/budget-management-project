![logo](src/main/resources/static/img/logo.jpg)

# Budget management project BankStake

## Table of Contents
- [개요](#개요)
- [Skils](#skils)
- [Installation](#Installation)
- [Running Tests](#running-tests)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [ERD](#구현과정(설계-및-의도))
- [TIL 및 회고](#til-및-회고)

## 개요
**돈 모아서 뭐하겠노? 소고기 사묵겠지.. 당신을 소고기를 지켜줄! BankStake🐮** <br>
본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션입니다. 
이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.

## Skill
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/JAVA-17-green) ![Static Badge](https://img.shields.io/badge/Spring_boot-REST-green)<br/>
ORM: ![Static Badge](https://img.shields.io/badge/JPA-green) ![Static Badge](https://img.shields.io/badge/QueryDsl-blue)<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/h2-2.1.214-blue) <br/>

## Installation

```bash
./gradlew bootjar
./gradlew bootrun
```

## Running Tests
```bash
  ./gradlew test
```

> ![Static Badge](https://img.shields.io/badge/Test_Passed-18/18-green)<br>
> ![coverage](src/main/resources/static/img/test_result.png)

## API Reference
> [![POST_MAN](https://img.shields.io/badge/POSTMAN_-CLICK🖱-%23000000.svg?style=for-the-badge&logo=postman&logoColor=white)](https://documenter.getpostman.com/view/18383272/2s9YXo2L9p)<br>
> ![Project_](src/main/resources/static/img/api_reference.png)

## 프로젝트 진행 및 이슈 관리

> [![Notion](https://img.shields.io/badge/Github_project_-CLICK🖱-%23000000.svg?style=for-the-badge&logo=Github&logoColor=white)](https://github.com/users/U-jinLee/projects/1)<br>
> ![Project_](src/main/resources/static/img/project.png)


## ERD
> ![Project_](src/main/resources/static/img/ERD.png)
> - 유저와 예산은 `1:N`의 관계를 가지고 예산과 지출은 `1:N`의 관계
>   - 예산 테이블은 사용한 금액과 관련된 파생 속성을 가지고 있다.
> - 예산 테이블은 생성 날짜를 계산해 한 달이 지나면 다시 생산할 수 있다.
> - 카테고리는 정렬을 위한 선택횟수 파생 속성을 가진다.
> - 지출통계의 통계데이터용 테이블 존재

## TIL 및 회고
- [LocalDateTime을 통해 기간 정보 뽑아먹기]()
- [Class의 RequestParam은 Method의 @PathVariable에 커버가 된다]()
  - [심지어 사용하지 않아도 된다]()
- [QueryDsl 세팅](https://diligent-mangosteen-06d.notion.site/Querydsl-bb19c5e700564401a5bf7d4f2eca29b8?pvs=4)
- [금액 관련 자바 정리]()
- [Optional.of() 대신 Optional.ofNullable() 사용하기]()
<br/>