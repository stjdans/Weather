# Weather
공공 데이터  날씨 API 를 이용한 샘플 프로젝트  
viewmodel, flow 를 이용 mvvm 구조의 반응형 프로그램으로 구현

### 라이브러리
+ Hit
+ Retrofit
+ Room
+ Data Binding
+ Data Store - proto
+ Google Map

### 화면 구성
+ 실황
  현재 위치와 사용자 설정 위치의 날씨 실황 데이터 표시
  
  + 주소 검색
    검색을 통한 위치 데이터 표시
    
+ 단기 예보
  현재 위치와 사용자 설정 위치의 날씨 단기 예보 데이터 표시

+ 날씨 지도
  Google Map 의 Marker 를 이용 지도상의 실황 날씨 이미지 표시
