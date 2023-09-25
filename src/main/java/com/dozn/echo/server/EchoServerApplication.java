package com.dozn.echo.server;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.dozn.echo.utils.CryptoUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
        private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
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
                    String deletedMessage="";

                    // 메시지 큐에 저장
                    if (queue.size() >= 10) {
                        deletedMessage = queue.poll(); // 큐가 꽉 찼을 경우 첫 번째 요소 제거
                    }
                    queue.offer(receivedMessage);
                    log.info("받은 메세지 : {}", receivedMessage);
                    log.info("큐 : {}",queue);
                    log.info("큐 개수 : {}", queue.size());

                    // 클라이언트에 응답
                    if(deletedMessage != ""){
                        receivedMessage += " 삭제된 메세지 : "+deletedMessage;
                    }
                    dataOut.writeUTF(receivedMessage);
                    dataOut.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
