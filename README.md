## Tarot For U · AI 타로 앱
사용자가 입력한 고민과 직접 뽑은 카드를 바탕으로 AI 타로마스터가 리딩해 주는 안드로이드 앱입니다. 친구·연인과 실시간으로 카드를 번갈아 뽑아 보는 궁합 타로도 제공합니다.

포텐데이 X 클로바스튜디오 해커톤 프로젝트에서 출발했습니다.

[<img src="https://img.shields.io/badge/Google Play-414141?style=flat&logo=googleplay&logoColor=white"/>](https://play.google.com/store/apps/details?id=com.fourleafclover.tarot&pcampaignid=web_share)

| 시기 | 내용 |
|---|---|
| 2023.12.08 ~ 12.17 | 해커톤 |
| 2024.03 | 1차 배포 · AI 타로마스터 리딩 |
| 2024.05 ~ 10 | 2차 배포 · 실시간 궁합 타로 |
| 2026.11 | 3차 배포(3.1) · 새 백엔드, App Links, API 36 대응 |

![그래픽 이미지](https://github.com/312TEN015/TarotForU-android/assets/69448918/afc99fc3-948d-4254-b304-a1b341cbf84b)


## 구현 영상
- 1차 · AI 타로 리딩: [vimeo.com/895448158](https://vimeo.com/895448158?share=copy)
- 2차 · 실시간 궁합: [youtube.com/shorts/1zXtICehyCg](https://youtube.com/shorts/1zXtICehyCg?feature=share)


## 기술 스택

<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=Kotlin&logoColor=white"/>  <img src="https://img.shields.io/badge/Jetpack Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white"/>  <img src="https://img.shields.io/badge/Hilt-34A853?style=flat&logo=android&logoColor=white"/>  <img src="https://img.shields.io/badge/Retrofit-48B983?style=flat&logo=square&logoColor=white"/>  <img src="https://img.shields.io/badge/Socket.IO-010101?style=flat&logo=socketdotio&logoColor=white"/>

- **UI**: Jetpack Compose, Navigation Compose
- **상태·비동기**: ViewModel, StateFlow, Coroutines
- **DI**: Hilt
- **네트워크**: Retrofit, OkHttp, Socket.IO(궁합 실시간 통신)
- **공유**: Android App Links (3차 배포에서 Firebase Dynamic Links를 대체)
- **빌드**: AGP 9, Kotlin 2.2, targetSdk 36

서버는 [TarotServer](https://github.com/dlawldn10/TarotServer)(Node.js · Express · Socket.IO · Claude API)를 사용합니다.


## 3차 배포 (2026.11) 주요 변경
- **새 백엔드 연결**: 리딩 생성, 궁합 방, 데모 설정을 TarotServer로 옮겼습니다. 데모 설정은 Firestore 호출 두 번 대신 `GET /config/demo` 한 번으로 받습니다.
- **공유 링크**: 종료된 Firebase Dynamic Links를 Android App Links로 바꿨습니다.
- **운영 공지**: 앱 시작 시 점검 안내와 강제 업데이트를 띄웁니다.
- **궁합 버그 수정**: 방장 기기에서 내 카드와 상대 카드가 뒤바뀌던 문제, 대기방을 나갈 때의 크래시를 고쳤습니다.
- **UX**: 카드 덱 중앙 정렬과 뽑은 카드 애니메이션, 리딩 로딩 중 안내 문구, 나의 타로 저장 안내 시트를 추가했습니다.
- **플랫폼 최신화**: targetSdk 36에 맞추고 빌드 도구를 올렸습니다.


## 디렉토리 구조

[Tarot/app/.../tarot](https://github.com/312TEN015/TarotForU-android/tree/main/Tarot/app/src/main/java/com/fourleafclover/tarot)
```
├─ MainActivity.kt, MyApplication.kt
├─ constant        카드·주제·설정 상수
├─ data
│  └─ repository   TarotRepository (Remote / Fake·데모 모드)
├─ demo            데모 모드 전용 데이터·테마·ViewModel
├─ di              Hilt 모듈 (Network, Repository)
├─ network         Retrofit 서비스
├─ notice          점검·강제 업데이트 공지
├─ ui
│  ├─ component    공용 컴포넌트 (카드 슬라이더, 다이얼로그, 로딩 등)
│  ├─ navigation   NavGraph, 화면 enum, 뒤로가기 처리
│  ├─ screen
│  │  ├─ main      홈, 온보딩
│  │  ├─ fortune   고민 입력 → 카드 뽑기 → 결과
│  │  ├─ harmony   궁합 방 생성·입장·채팅·결과
│  │  ├─ loading   리딩·방 생성·초대 로딩
│  │  └─ my        나의 타로, 공유 받은 결과
│  └─ theme
└─ utils           공유, 환경설정, 로그 등
```


## 서비스 소개
<img width="1920" alt="서비스 소개 1" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/ae4b27b5-e005-4770-aa78-e21b9fbed950">
<img width="1920" alt="서비스 소개 2" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/f1e01ff5-eb8a-4082-97a0-78336e1dbaf4">
<img width="1920" alt="서비스 소개 3" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/3906ea1f-8ceb-49b1-8a82-ef4ff0f19f8f">



## 주요 화면 및 기능
<img width="1920" alt="주요 화면 1" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/0a185445-33b5-4d2f-b1fb-fc1caa9f47cc">
<img width="1920" alt="주요 화면 2" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/2a9ffc25-9099-4fc2-8dc1-6f5c17083da4">
<img width="1920" alt="주요 화면 3" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/6e673a21-47e7-4e64-adf6-e812d8ab3e01">
