# (더즌) 메시징사업팀 백엔드 개발자 채용 과제

---

# 요구사항 구현여부

## 1. server-client간 socket 연결
- 3개 이상의 클라이언트와 연결하기위해 멀티쓰레드를 사용
- 효율적인 자원관리를 위해 try-with-resources 사용

## 2. client->server 암호화된 메시지 전송
- 임의로 작성한 AES-128 방식으로 암호화
- 암호화 과정에서 추가적으로 Base64 인코딩/디코딩
- 공통으로 사용되는 부분이라 따로 클래스를 선언하여 관리
   
## 3. server에서 성공적으로 받았으면 복호화 후 받은 메시지 출력 및 저장
(최근 10개 메시지는 복호화 후 queue 저장)
- ArrayBlockingQueue 를 사용하여 메세지 관리, 크기는 10으로 지정
- 효율적인 관리를 위해서 따로 클래스를 만들어서 관리

## 4. server는 받은 메시지를 client로 반환
(queue Full 시 삭제되는 메시지는 현재 반환하는 메시지 뒤에 붙여서 같이 client로 반환)
- 큐 안의 메세지가 10개일 경우 가장 오래된 메세지 값을 삭제(최신 메세지 10개를 유지)
- 삭제된 메세지는 반환 메세지 뒤에 ex) 삭제된 메세지: xxxxx 형식으로 출력

## 5. client 받은 메시지 출력
- ex) 반환 메세지: xxxx  형식으로 출력


---
# 실행 방법

1. EchoServerApplication(에코서버) 을 먼저 실행한다.
2. 에코서버가 대기중이라는 로그를 확인한 후 EchoClient 를 실행한다
3. 3개 이상의 클라이언트를 확인 해보기 위해서 실행을 설정해야한다.
4. 상단에 있는 목록중 Run 을 클릭하고 Edit Configurations... 를 누른다

  ![image](https://github.com/gettekim/echo/assets/51043714/12c8fd3c-1da9-41df-abb8-2de904dfa30c)

5. "+" 모양을 클릭하여 Application을 누르고 빌드랑 Run 할 클래스를 EchoClient 로 입력 한 뒤 원하는 개수의 클라이언트 만큼 생성한다.

![image](https://github.com/gettekim/echo/assets/51043714/b1aeee12-aed7-41fd-a34f-0eccbba43369)

6. 각각의 클라이언트를 실행시켜 문자를 입력한다.

![image](https://github.com/gettekim/echo/assets/51043714/a9cf8612-e73a-4201-9548-d38c752f5cf2)




     
