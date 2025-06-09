package com.focus.springboottest.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * navicat 15-16版本的数据库链接，可以通过以下代码进行密码解密
 */
public class NavicatPasswordDecryptor {
    private static final String AES_KEY = "libcckeylibcckey";
    private static final String AES_IV = "libcciv libcciv ";

    public static String decrypt(String encrypted) throws Exception {
        byte[] encryptedBytes = hexStringToByteArray(encrypted);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) throws Exception {
        String encryptedPassword = "F69F7428D1FA65A8E5E576F26AD675A3CFF016492CFA5422BFFB8D346917C4E6";
        System.out.println(decrypt(encryptedPassword));
    }
}