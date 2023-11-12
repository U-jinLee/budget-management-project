![logo](src/main/resources/static/img/logo.jpg)

# Budget management project BankStake

## Table of Contents
- [개요](#개요)
- [Skils](#skils)
- [Installation](#Installation)
- [Running Tests](#running-tests)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [구현과정(설계 및 의도)](#구현과정(설계-및-의도))
- [TIL 및 회고](#til-및-회고)
- [Authors](#authors)
- [References](#references)

## 개요
**돈 모아서 뭐하겠노? 소고기 사묵겠지.. 당신을 소고기를 지켜줄! BankStake🐮** <br>
본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션입니다. 이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.

## Skils
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/JAVA-17-green) ![Static Badge](https://img.shields.io/badge/Spring_boot-REST-green)<br/>
ORM: ![Static Badge](https://img.shields.io/badge/JPA-green) ![Static Badge](https://img.shields.io/badge/QueryDsl-blue)<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/h2-2.1.214-blue) <br/>
배포 : ![Static Badge](https://img.shields.io/badge/Docker-039BC6) ![Static Badge](https://img.shields.io/badge/AWS-EC2-orange) <br/>

## Installation

Install my-project with npm

```bash
./gradlew bootjar
./gradlew bootrun
```

<br/>


## Running Tests

To run tests, run the following command

```bash
  ./gradlew test
```

> ![Static Badge](https://img.shields.io/badge/Test_Passed-20/20-green)
![coverage](https://user-images.githubusercontent.com/48683566/56673924-0b84ae00-66b1-11e9-93ac-fb76ff96a5a0.png)

<br/>


## API Reference

<details>
<summary>Get all items - click</summary>

#### Request
```javascript
  GET /api/items
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

    [{
        "id": 10,
        "name": "shirt",
        "color": "red",
        "price": "$23"
    },...
    ]
```
</details>
<details>
<summary>Get all items - click</summary>

#### Request
```javascript
  GET /api/items
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

    [{
        "id": 10,
        "name": "shirt",
        "color": "red",
        "price": "$23"
    },...
    ]
```
</details>
<details>
<summary>Get all items - click</summary>

#### Request
```javascript
  GET /api/items
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Response
```http
    HTTP/1.1 200
    Content-Type: application/json

    [{
        "id": 10,
        "name": "shirt",
        "color": "red",
        "price": "$23"
    },...
    ]
```
</details>

<br/>


## 프로젝트 진행 및 이슈 관리

[![Notion](https://img.shields.io/badge/Github_project-%23000000.svg?style=for-the-badge&logo=Github&logoColor=white)](https://github.com/users/U-jinLee/projects/1)

<br/>


## 구현과정(설계 및 의도)
(노션, 블로그 등의 페이지로 안내 가능)
<details>
<summary>유저 모델과 실행결과 모델관의 관계 설정 시 00 고려 - click</summary>

- 의존성 문제
    1. Press `Ctrl` + `f` on your keyboard, to bring out the search modal.
    2. Enter the name of the badge you need.
    3. Copy the appropriate `![Name](link)` element and paste it in your Markdown file (e.g. README.md)
- 00가 00 하는 문제

</details>

<details>
<summary>00 구현 시 동시성 고려 - click</summary>

- 의존성 문제
    1. Press `Ctrl` + `f` on your keyboard, to bring out the search modal.
    2. Enter the name of the badge you need.
    3. Copy the appropriate `![Name](link)` element and paste it in your Markdown file (e.g. README.md)
- 00가 00 하는 문제

</details>

<details>
<summary>RESTful API 설계 - click</summary>

- 의존성 문제
    1. Press `Ctrl` + `f` on your keyboard, to bring out the search modal.
    2. Enter the name of the badge you need.
    3. Copy the appropriate `![Name](link)` element and paste it in your Markdown file (e.g. README.md)
- 00가 00 하는 문제

</details>

<br/>


## TIL 및 회고

<details>
<summary>Django ORM 조회 시 발생하는 00 버그 - click</summary>

- 의존성 문제
    1. Press `Ctrl` + `f` on your keyboard, to bring out the search modal.
    2. Enter the name of the badge you need.
    3. Copy the appropriate `![Name](link)` element and paste it in your Markdown file (e.g. README.md)
- 00가 00 하는 문제

</details>

<details>
<summary>Django ORM 조회 시 발생하는 00 버그 - click</summary>

- 의존성 문제
    1. Press `Ctrl` + `f` on your keyboard, to bring out the search modal.
    2. Enter the name of the badge you need.
    3. Copy the appropriate `![Name](link)` element and paste it in your Markdown file (e.g. README.md)

- 00가 00 하는 문제

</details>

(또는 블로그, 노션 등 링크 연동)
- [LocalDateTime을 통해 이번 달의 기간을 뽑는 코드]()
- [Class의 RequestParam은 Method의 @PathVariable에 커버가 된다]()
  - [심지어 사용하지 않아도 된다]()
- [QueryDsl 세팅]()
<br/>

## References

- [Awesome Readme Templates](https://awesomeopensource.com/project/elangosundar/awesome-README-templates)
- [Awesome README](https://github.com/matiassingers/awesome-readme)
- [How to write a Good readme](https://bulldogjob.com/news/449-how-to-write-a-good-readme-for-your-github-project)


