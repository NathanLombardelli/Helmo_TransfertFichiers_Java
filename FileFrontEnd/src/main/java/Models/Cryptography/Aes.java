package Models.Cryptography;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Aes {

    private int AES_KEY_SIZE;
    private int GCM_IV_LENGTH;
    private int GCM_TAG_LENGTH;

    public Aes(int AES_KEY_SIZE, int GCM_IV_LENGTH, int GCM_TAG_LENGTH) {
        this.AES_KEY_SIZE = AES_KEY_SIZE;
        this.GCM_IV_LENGTH = GCM_IV_LENGTH;
        this.GCM_TAG_LENGTH = GCM_TAG_LENGTH;
    }

    public SecretKey createKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(AES_KEY_SIZE, SecureRandom.getInstanceStrong());
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            System.out.printf("Impossible to create a AES Key (%d)\n", AES_KEY_SIZE);
        }
        return null;
    }

    public String getKey(SecretKey key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public SecretKey getKey(String key){
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public byte[] createIV() {
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        return IV;
    }

    public String getIv(byte[] iv){
        return Base64.getEncoder().encodeToString(iv);
    }

    public byte[] getIv(String iv){
        return Base64.getDecoder().decode(iv);
    }

    public byte[] encrypt(byte[] data, SecretKey key, byte[] IV) throws Exception {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Encryption
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data, SecretKey key, byte[] IV) throws Exception {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        return cipher.doFinal(data);
    }

    public boolean decrypt(String directoryPath, String filename, SecretKey key, byte[] IV){
        try {
            Path path = FileSystems.getDefault().getPath(directoryPath, filename);
            byte[] fileData = Files.readAllBytes(path);
            fileData = decrypt(fileData, key, IV);
            Files.write(path, fileData);
        } catch (Exception e) {
            System.out.println("[AES] Impossible to decrypt file");
            return false;
        }
        return true;
    }

    public boolean encrypt(String directoryPath, String filename, SecretKey key, byte[] IV) {
        try {
            Path path = FileSystems.getDefault().getPath(directoryPath, filename);
            byte[] fileData = Files.readAllBytes(path);
            fileData = encrypt(fileData, key, IV);
            Files.write(path, fileData);
        } catch (Exception e) {
            System.out.println("[AES] Impossible to crypt file");
            return false;
        }

        return true;
    }
}
