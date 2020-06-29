# 블록체인 헌혈증서 기부 시스템

## 소개

헌혈증서를 블록체인을 통해 쉽고 안전하게 기부할 수 있는 시스템을 안드로이드 어플리케이션으로 구현하였습니다. 

모든 헌혈증서 기부 거래는 블록체인 네트워크에서 투명하게 관리됩니다.

카카오 자회사 GroundX에서 개발한 블록체인 어플리케이션 플랫폼인 Klaytn을 이용하였고, Klaytn SDK인 [Caver-java](https://github.com/klaytn/caver-java)를 사용하여 android 프로젝트 내 구현하였습니다.

실제 구현은 Baobab testnet에서 진행되었습니다.

## 주요 기능

1. 회원 가입 및 키 생성
   - 사용자가 회원 가입을 할 때, 내부적으로 사용자의 Public key와 Private key를 생성합니다. 
   - Public key는 데이터베이스에 회원 가입 정보와 함께 등록되고, Private key는 로컬 디바이스에 저장합니다.
   - 이를 통해 사용자가 직접 자신의 지갑을 만들 필요 없을 뿐만 아니라, 또한 블록체인이 낯선 사용자들에게 진입 장벽을 낮추었습니다.
   
   ![register](https://github.com/pleiades-s/2020spring_41class_team2/blob/master/images/register.png)
   
2. 헌혈증서 OCR 스캔
   - 헌혈증서 발급 데이터베이스는 한국 적십자사에 존재한다는 가정 하에 진행합니다. 
   - 본 프로젝트에서는 헌혈증서를 토큰의 형태로 발급하고 거래하는 것이 아니라 현재 발행되는 종이 헌혈증서를 촬영하여 적십자사의 발급 정보 데이터베이스와 조회하고 유효한 헌혈증서이면 시스템에 등록하는 방식으로 구현하였습니다.
   
   ![scan](https://github.com/pleiades-s/2020spring_41class_team2/blob/master/images/scan.png)

3. 헌혈증서 기부 요청 글 작성
   - 헌혈증서를 기부 받고자 주변 사람들 혹은 소셜네트워크에 글을 올려 기부를 요청하는 번거로움을 해소하고자, 사용자는 자신의 사연을 올려서 기부를 요청할 수 있습니다.

   ![post](https://github.com/pleiades-s/2020spring_41class_team2/blob/master/images/post.png)
   
4. 헌혈증서 기부
   - 사용자는 자신의 타임라인에 나타난 사연들을 읽고 특정 사연에 대해서 기부를 할 수 있습니다. 기부 가능 개수는 시스템에 최대 등록한 헌혈 증서 개수 만큼 가능합니다.
   - 사용자는 "기부 하기" 버튼을 누르면 (헌혈증서 ID, 수령자 address, timestamp) 정보를 인자로 받고 이를 스마트 컨트랙트 내에 저장하는 함수를 호출합니다. 
   - N 개의 헌혈증서를 기부하게 되면 N 번 트랜잭션을 생성합니다.
   - 이때, 사용자는 회원가입을 통해 생성한 Key를 통해 트랜잭션 서명을 하기 때문에 잔고가 없어 트랜잭션을 네트워크에 반영할 수 없습니다.
   - 따라서 Klaytn의 Fee delegation을 수행하는 서버를 통해 사용자는 서명만 하고 해당 트랜잭션을 서버에 전송하면 서버는 해당 비용을 처리할 수 있는 Private key로 추가 서명을 하여 네트워크에 반영합니다. 
   - 이후 사용자에게 트랜잭션 반영 성공 여부를 띄워주고, 성공 시 트랜잭션의 Hash 값을 제공해줍니다.
   
   ![donate](https://github.com/pleiades-s/2020spring_41class_team2/blob/master/images/donate.png)

5. 헌혈증서 기부 내역 조회
   - 헌혈증서 기부 과정에서 기록된 정보를 조회할 수 있는 스마트 컨트랙트의 View 타입 함수를 호출하여 기부 내역을 조회합니다.
   - 병원 측에서 전산화된 헌혈증서를 접수할 수 있는 [Blood Wallet Admin App](https://github.com/pleiades-s/BloodWallet_Admin)을 통해 사용자는 본인이 기부한 헌혈증서가 어떤 병원에서 접수되었는지 확인할 수 있습니다. (참고로, 병원 접수 기록은 적십자사의 헌혈증서 데이터베이스에서 관리합니다. 적십자사 문의 결과, 현재 병원에서 종이 헌혈증서를 접수하면 병원에서 해당 헌혈증서의 접수 정보를 제출한다고 확인 받았고 본 프로젝트에서는 이를 모킹하여 진행하였습니다.)
   
   ![history](https://github.com/pleiades-s/2020spring_41class_team2/blob/master/images/history.png)
   
   ![hospital](https://github.com/SKKone-blockchain/BloodWallet/blob/master/images/receipt.png)

## 발표자료 및 문서
1. [발표 동영상](https://www.youtube.com/watch?v=EX5R6WsAMf4)

2. [요구사항 명세서 및 시스템 디자인](https://github.com/pleiades-s/2020spring_41class_team2/tree/master/doc)
