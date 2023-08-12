package Models.Cryptography;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA384 {

    public String hash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(data.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = String.format("0%s", hashtext);
            }
            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getFileChecksum(String path, String fileName) throws IOException {
        return getFileChecksum(String.format("%s/%s", path, fileName));
    }

    public String getFileChecksum(String path) throws IOException
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");

            //Get file input stream for reading the file content
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);

            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount;

            //Read file data and update in message digest
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            };

            //close the stream; We don't need it now.
            fis.close();

            //Get the hash's bytes
            byte[] bytes = digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            //return complete hash
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
