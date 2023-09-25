package com.dozn.echo.client;

import com.dozn.echo.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class WebSocketClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {

        try (Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
             DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

            log.info("서버에 연결.");

            CryptoUtils cryptoUtils = new CryptoUtils();

            // 서버 응답을 받는 스레드 시작
            Thread responseThread = new Thread(new ResponseReceiver(dataIn));
            responseThread.start();

            while (true) {

                // 사용자로부터 문자열 입력
                String messageToSend = reader.readLine();

                // 문자열을 바이트 배열로 변환하고 암호화
                byte[] encryptedData = cryptoUtils.encrypt(messageToSend);
                // 암호문 길이와 암호문 전송
                dataOut.writeInt(encryptedData.length);
                dataOut.write(encryptedData);
                dataOut.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private record ResponseReceiver(DataInputStream dataIn) implements Runnable {
        @Override
            public void run() {
                try {
                    while (true) {
                        // 서버 응답 받기
                        String responseMessage = dataIn.readUTF();
                        log.info("반환 메세지 : {}", responseMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}