package com.dozn.echo.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


public class CryptoUtils {

    private final String secretkey = "134abcd309294ba6";
    private final String iv = "95b3ab063e45e1a7117414c1a9196afa";
    private final SecretKeySpec secretKeySP;
    private final IvParameterSpec ivPS;
    private final Cipher cipher;

    public CryptoUtils() throws Exception {
        this.secretKeySP = new SecretKeySpec(secretkey.getBytes(StandardCharsets.UTF_8), "AES");
        this.ivPS = new IvParameterSpec(Hex.decodeHex(iv));
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }
    public byte[] encrypt(String data) throws Exception {
        //암호화 설정
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySP, ivPS);
        byte[] convertedData = data.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeBase64(cipher.doFinal(convertedData));
    }

    public String decrypt(byte[] data) throws Exception{
        //복호화 설정
        cipher.init(Cipher.DECRYPT_MODE, secretKeySP, ivPS);
        byte[] decryptedMessage = cipher.doFinal(Base64.decodeBase64(data));
        return new String(decryptedMessage, StandardCharsets.UTF_8);

    }

}
