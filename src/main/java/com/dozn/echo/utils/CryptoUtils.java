package com.dozn.echo.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


@Component
public class CryptoUtils {

    private final String secretkey = "134abcd309294ba6";
    private final String iv = "95b3ab063e45e1a7117414c1a9196afa";

    public byte[] encrypt(String data) throws Exception {
        //암호화 설정
        SecretKeySpec secretKey = new SecretKeySpec(secretkey.getBytes(), "AES");
        IvParameterSpec ivPS = new IvParameterSpec(Hex.decodeHex(iv));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivPS);

        byte[] convertedData = data.getBytes("UTF-8");
        byte[] encryptedData = cipher.doFinal(convertedData);

        return Base64.encodeBase64(encryptedData);
    }

    public String decrypt(byte[] data) throws Exception{
        //암호화 설정
        SecretKeySpec secretKey = new SecretKeySpec(secretkey.getBytes(), "AES");
        IvParameterSpec ivPS = new IvParameterSpec(Hex.decodeHex(iv));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivPS);

        byte[] decryptedMessage = cipher.doFinal(Base64.decodeBase64(data));

        String receivedMessage = new String(decryptedMessage, "UTF-8");

        return receivedMessage;

    }

}
