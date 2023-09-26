package com.dozn.echo.utils;

import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;


@Component
public class CryptoUtils {

    private final String secretkey = "134abcd309294ba6";
    private final String iv = "95b3ab063e45e1a7117414c1a9196afa";
    private SecretKeySpec secretKeySP;
    private IvParameterSpec ivPS;
    private Cipher cipher;
    @PostConstruct
    public void init() throws Exception {
        secretKeySP = new SecretKeySpec(secretkey.getBytes(), "AES");
        ivPS = new IvParameterSpec(Hex.decodeHex(iv));
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public byte[] encrypt(String data) throws Exception {
        //암호화 설정

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySP, ivPS);

        byte[] convertedData = data.getBytes("UTF-8");
        byte[] encryptedData = cipher.doFinal(convertedData);

        return Base64.encodeBase64(encryptedData);
    }

    public String decrypt(byte[] data) throws Exception{
        //암호화 설정
        cipher.init(Cipher.DECRYPT_MODE, secretKeySP, ivPS);

        byte[] decryptedMessage = cipher.doFinal(Base64.decodeBase64(data));
        String receivedMessage = new String(decryptedMessage, "UTF-8");

        return receivedMessage;

    }

}
