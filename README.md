## 포텐데이 X 클로바스튜디오 해커톤 프로젝트
사용자가 입력한 고민거리를 기반으로 오로지 자신만을 위한 운세를 받아볼 수 있는 AI 기반 타로 운세 보기 앱 서비스

> 해커톤 기간: 2023.12.08 ~ 2023.12.17
> 
>  2024.03.05 [구글 플레이스토어 출시](https://play.google.com/store/apps/details?id=com.fourleafclover.tarot&pcampaignid=web_share)
> 
> 2024.05 ~ 2024.10 추가 디벨롭 진행

![그래픽 이미지](https://github.com/312TEN015/TarotForU-android/assets/69448918/afc99fc3-948d-4254-b304-a1b341cbf84b)


## 구현 영상
[https://youtu.be/M4QCKSfNTdQ](https://vimeo.com/895448158?share=copy)


## 안드로이드 네이티브 앱 개발 100% 

<img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat&logo=Android Studio&logoColor=white"/>  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=Kotlin&logoColor=white"/>  <img src="https://img.shields.io/badge/jetpackcompose-4285F4?style=flat&logo=jetpackcompose&logoColor=white"/>  <img src="https://img.shields.io/badge/firebase-FFCA28?style=flat&logo=firebase&logoColor=white"/>


## 디렉토리 구조

[TarotForU-android/Tarot/app/.../tarot](https://github.com/312TEN015/TarotForU-android/tree/main/Tarot/app/src/main/java/com/fourleafclover/tarot)
```
│  InitialDatas.kt
│  MainActivity.kt
│  MyApplication.kt
│  SplashViewModel.kt
│  
├─constant
│      Cards.kt
│      Settings.kt
│      Topics.kt
│      
├─data
│      RetrofitData.kt
│      TarotSubjectData.kt
│      
├─network
│      TarotService.kt
│      
├─ui
│  ├─component
│  │      AppNavigationBar.kt
│  │      CardSlider.kt
│  │      Dialogs.kt
│  │      
│  ├─navigation
│  │      BackHandlers.kt
│  │      NavigationGraph.kt
│  │      NavigationUtil.kt
│  │      ScreenEnum.kt
│  │      
│  ├─screen
│  │      HomeScreen.kt  // 메인화면
│  │      InputScreen.kt  // 고민거리 입력
│  │      LoadingScreen.kt
│  │      MyTarotDetailScreen.kt  // 저장한 타로 결과 상세
│  │      MyTarotScreen.kt  // 나의 타로 목록 조회
│  │      OnBoardingScreen.kt
│  │      PickTarotScreen.kt    // 타로 카드 뽑기
│  │      ResultScreen.kt  // 타로 운세 결과 보기
│  │      ShareDetailScreen.kt  // 공유 받은 타로 결과 화면
│  │      
│  └─theme
│          Color.kt
│          Text.kt
│          Theme.kt
│          Type.kt
│          
└─utils
        PreferenceUtil.kt
        RetrofitUtil.kt
        ServiceUtil.kt
        ShareUtil.kt
        TarotCardUtil.kt
```


## 서비스 소개
<img width="1920" alt="서비스 소개 1" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/ae4b27b5-e005-4770-aa78-e21b9fbed950">
<img width="1920" alt="서비스 소개 2" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/f1e01ff5-eb8a-4082-97a0-78336e1dbaf4">
<img width="1920" alt="서비스 소개 3" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/3906ea1f-8ceb-49b1-8a82-ef4ff0f19f8f">



## 주요 화면 및 기능
<img width="1920" alt="주요 화면 1" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/0a185445-33b5-4d2f-b1fb-fc1caa9f47cc">
<img width="1920" alt="주요 화면 2" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/2a9ffc25-9099-4fc2-8dc1-6f5c17083da4">
<img width="1920" alt="주요 화면 3" src="https://github.com/312TEN015/TarotForU-android/assets/69448918/6e673a21-47e7-4e64-adf6-e812d8ab3e01">
