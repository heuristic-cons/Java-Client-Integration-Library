/**
 * -----------------------------------------------------------------------------
 * File=Encryptor.java
 * Company=Solidsoft Reply
 * Copyright © 2017 Solidsoft Reply Ltd.
 *
 * Encrypts and decrypts strings.
 * -----------------------------------------------------------------------------
 */
package com.reply.solidsoft.nbs.integration.sample.credentialsservice;

import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Encrypts and decrypts strings.
 */
public class Encryptor {

    /**
     * The encryption algorithm to be used.
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * The number of iterations to apply.
     */
    private static final int ITERATIONS = 7;

    /**
     * Gets a byte array for the pass phrase.
     *
     * @return A byte array for the pass phrase.
     */
    private static byte[] getBytes1() {
        return new byte[]{50, 122, 37, 70, 45, 53, 82, 113, 55, 102, 63, 75, 65, 42, 56, 121, 120, 68, 95, 51, 125, 106, 54, 71, 57, 36, 90, 101, 47, 114, 52, 77, 98, 50, 80, 61, 43, 119, 57, 89, 51, 88, 97, 33, 109, 55, 123, 78, 38, 84, 52, 105, 56, 45, 99, 74, 76, 100, 53, 63, 115, 54, 33, 66, 54, 69, 110, 47, 95, 111, 51, 83, 116, 56, 81, 38, 36, 112, 52, 72, 50, 87, 103, 125, 107, 61, 67, 55, 43, 53, 122, 89, 67, 100, 37, 57, 42, 81, 55, 120};
    }

    /**
     * Encrypts a clear clearText string.
     *
     * @param clearText The clear clearText to be encrypted.
     * @return An encrypted string.
     * @throws java.lang.Exception
     */
    public static String encrypt(String clearText) throws Exception {
        if (clearText == null) {
            clearText = "";
        }

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        String passCode = new String(getBytes1());
        PBEKeySpec spec = new PBEKeySpec(passCode.toCharArray(), saltBytes, ITERATIONS, 256);
        Key secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher aes = Cipher.getInstance(ALGORITHM);
        aes.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = aes.getParameters();
        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] encryptedTextBytes = aes.doFinal(clearText.getBytes("UTF-8"));
        
        //prepend salt and vi
        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);

        return Base64.getEncoder().encodeToString(buffer);
    }
    
    /**
     * Decrypts a cryptographically secure clearText string.
     *
     * @param cipherText The cryptographically secure clearText to be decrypted.
     * @return A decrypted string.
     * @throws java.lang.Exception
     */
    public static String decrypt(String cipherText) throws Exception {
    Cipher aes = Cipher.getInstance(ALGORITHM);
    
    //strip off the salt and iv
    ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(cipherText));
    byte[] saltBytes = new byte[20];
    buffer.get(saltBytes, 0, saltBytes.length);
    byte[] ivBytes = new byte[aes.getBlockSize()];
    buffer.get(ivBytes, 0, ivBytes.length);
    byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
  
    buffer.get(encryptedTextBytes);
    
    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    String passCode = new String(getBytes1());
    PBEKeySpec spec = new PBEKeySpec(passCode.toCharArray(), saltBytes, ITERATIONS, 256);
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
    aes.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
    
    return new String(aes.doFinal(encryptedTextBytes));
  }
}
