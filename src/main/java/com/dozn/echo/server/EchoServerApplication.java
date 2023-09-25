package com.dozn.echo.server;

import com.dozn.echo.utils.CryptoUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@SpringBootApplication
public class EchoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EchoServerApplication.class, args);
    }

    @PostConstruct
    public void startEchoServer() {
        try (ServerSocket serverSocket = new ServerSocket(8080)){

            System.out.println("에코서버 대기중...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.debug("연결완료");
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final CryptoUtils cryptoUtils = new CryptoUtils();
        private final MessageQueue queue = new MessageQueue();
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream())){

                while (true) {

                    // 클라이언트로부터 메시지 수신
                    int messageLength = dataIn.readInt();
                    byte[] encryptedMessage = new byte[messageLength];
                    dataIn.readFully(encryptedMessage);

                    // 메시지 복호화
                    String receivedMessage = cryptoUtils.decrypt(encryptedMessage);
                    log.info("받은 메세지: {}", receivedMessage);
                    // 큐 작업 실행
                    String resultMessage = queue.proceed(receivedMessage);
                    // 큐 상태 확인
                    queue.getQueueStatus();

                    // 클라이언트에 응답
                    dataOut.writeUTF(resultMessage);
                    dataOut.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
