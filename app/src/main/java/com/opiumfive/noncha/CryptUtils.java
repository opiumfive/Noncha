package com.opiumfive.noncha;

import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String CHARSETNAME = "UTF8";
    private static final String ALGORITHM = "AES";


    public static String encryptString(String string) {
        byte[] aesData;
        String base64 = "";

        try {
            aesData = encrypt(new HidingUtil().get(), string.getBytes(CHARSETNAME));
            base64 = Base64.encodeToString(aesData, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64;
    }

    public static String decryptString(String string) {
        byte[] debase64;
        String result = "";

        try {
            debase64 = Base64.decode(string, Base64.NO_WRAP);
            byte[] aesDecrypted = decrypt(new HidingUtil().get(), debase64);
            result = new String(aesDecrypted, CHARSETNAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static byte[] decrypt(String k, byte[] plainBytes) throws Exception {
        byte[] keyBytes = k.getBytes(CHARSETNAME);
        byte[] keyBytes16 = new byte[16];
        System.arraycopy(keyBytes, 0, keyBytes16, 0, Math.min(keyBytes.length, 16));
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes16, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[16];
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(plainBytes);
    }

    private static byte[] encrypt(String k, byte[] plainBytes) throws Exception {
        byte[] keyBytes = k.getBytes(CHARSETNAME);
        byte[] keyBytes16 = new byte[16];
        System.arraycopy(keyBytes, 0, keyBytes16, 0, Math.min(keyBytes.length, 16));
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes16, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[16];
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(plainBytes);
    }

    public static String StringToMD5(String in) {
        MessageDigest md = null;
        try { md = MessageDigest.getInstance("MD5"); } catch (Exception e) {}
        md.update(in.getBytes(),0,in.length());
        String s = new BigInteger(1, md.digest()).toString(16);
        if (s.length() == 31) s = "0"+s;
        return s;
    }
}
