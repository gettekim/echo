package com.dozn.echo.server;

import com.dozn.echo.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EchoServerApplicationTest {

    private EchoServerApplication echoServer;
    private Thread serverThread;


    @BeforeEach
    public void setUp(){
        //Echo 서버를 신규 스레드로 생성
        echoServer = new EchoServerApplication();
        serverThread = new Thread(() -> echoServer.startEchoServer());
        serverThread.start();
    }

    @AfterEach
    public void tearDown() {
        //Echo 서버 스레드를 강제 종료
        serverThread.interrupt();
        try {
            //종료를 기다릴 최대 시간 설정
            serverThread.join(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void 클라이언트에서_보낸메세지가_반환메세지와_일치하는지확인() throws Exception {

        Socket clientSocket = new Socket("localhost", 8080);
        DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());

        CryptoUtils cryptoUtils = new CryptoUtils();

        String message = "테스트 메세지";
        byte[] encryptedMessage = cryptoUtils.encrypt(message);

        dataOut.writeInt(encryptedMessage.length);
        dataOut.write(encryptedMessage);
        dataOut.flush();

        String receivedMessage = dataIn.readUTF();

        clientSocket.close();

        assertThat(receivedMessage).isEqualTo(message);
        log.info("메세지: {}", message);

    }


}
