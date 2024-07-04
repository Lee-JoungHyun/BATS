# Bats 🦇 - Bitcoin Automated Trading System

## 📋 목차
- 프로젝트 진행기간
- 개요
- 주요 서비스
- 서비스 화면
- 주요 기술
- 프로젝트 산출물
- 팀원 역할 분배


## 📅 프로젝트 진행 기간
2022.06.10 ~ 2022.12.15 

## ✨ 개요
```
Bats는 AI를 통해 학습한 데이터로 UpBit API를 사용해
비트코인 자동매매하는 안드로이드 어플리케이션 입니다
```

## 💻 주요 기능

### 자동 매매 전략 수립 
- Keras 에서 GRU모델을 이용해 시계열데이터를 분석
- 1분뒤의 값을 예측하여 예측값에 맞게 매도 or 매수를 진행
### 알고리즘을 활용한 자동매매
- GRU모델로 분석한 1분뒤 데이터를 현재 시장가와 비교
- RSI(상대강도지수)를 통해 과매도 과매수구간에 거래를 진행
- UpBit API를 사용한 매매 수행
### 실시간 가격 데이터
- UpBit Open API 를 사용해 가져온 CandleStick Chart로 표현
### 알림 시스템
- 가격 변동, 매매 실행등의 중요한 이벤트에 대한 푸시 알림 제공
- 사용자 지정 알림 설정 가능

## 📱 서비스 화면

1. 메인 화면

<img width="322" alt="Bats_메인화면" src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/5c63d29a-dc00-4606-9bdf-2323904f03d3">

2. 회원 가입

<img width="322" alt="Bats_회원가입" src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/9e2187f7-b6bd-46e4-80a4-114891c28335">

3. 비번 찾기

<img width="322" alt="Bats_비번찾기" src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/bb78d7b0-4415-44a9-a4ce-2c247c69e818">


4. 개인 화면

<img width="322" alt="Bats_개인화면" src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/a3eb1a48-ce20-4966-b85b-afe843e93228">


5. 거래 단위

<img width="322" alt="Bats_거래단위" src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/a35eb4c8-8914-4c40-a1a0-4826dbbaa57e">


6. 거래 로그

<img width="322" alt="Bats_거래로그" src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/cd557f3a-a3d5-4435-80a1-38f5495e88f9">


## 🛠 주요 기술
**Client app**

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)

**APIServer**

![Django](https://img.shields.io/badge/django-%23092E20.svg?style=for-the-badge&logo=django&logoColor=white)
![DjangoREST](https://img.shields.io/badge/DJANGO-REST-ff1709?style=for-the-badge&logo=django&logoColor=white&color=ff1709&labelColor=gray)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

**머신러닝**

<img src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=white">
<img src="https://img.shields.io/badge/jupyter-F37626?style=for-the-badge&logo=jupyter&logoColor=white">
<img src="https://img.shields.io/badge/Keras-D00000?style=for-the-badge&logo=Keras&logoColor=white">
<img src="https://img.shields.io/badge/tensorflow-FF6F00?style=for-the-badge&logo=Tensorflow&logoColor=white">


**배포**

![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">



**협업 툴**

![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)


## 👩‍💻 팀원 역할 분배

|이중현|박무근|장종엽|
|:---:|:---:|:---:|
|<img src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/7b4906b8-903e-4786-a132-2c579d3ded8e" width="250px" height="200px"> |<img src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/d0899143-fb21-48fe-a1c8-f045561dee56"  width="250px" height="200px">|<img src="https://github.com/Lee-JoungHyun/BATS/assets/98592001/0042107e-1780-4169-8f34-b18fce50730e"  width="250px" height="200px">|
|Android|APIServer|MachineLearning|