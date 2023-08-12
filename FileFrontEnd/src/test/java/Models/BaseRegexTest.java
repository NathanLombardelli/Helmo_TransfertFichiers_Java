package Models;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BaseRegexTest {

    // DIGIT
    @Test
    void notADigitTest() {
        String input = "TEST";
        assertFalse(input.matches(BaseRegex.digit));
        input = "3 TEST";
        assertFalse(input.matches(BaseRegex.digit));
    }

    @Test
    void validDigitTest() {
        for (int i = 0; i <= 9; i++) {
            String input = String.valueOf(i);
            assertTrue(input.matches(BaseRegex.digit));
        }
    }

    @Test
    void invalidDigitTest() {
        String input = "-9";
        assertFalse(input.matches(BaseRegex.digit));
        input = "12";
        assertFalse(input.matches(BaseRegex.digit));
    }

    // PORT
    @Test
    void notAPortTest() {
        String input = "TEST";
        assertFalse(input.matches(BaseRegex.port));
        input = "65535TEST";
        assertFalse(input.matches(BaseRegex.port));
    }

    @Test
    void validPortTest() {
        for (int i = 1; i <= 65535; i++) {
            String input = String.valueOf(i);
            assertTrue(input.matches(BaseRegex.port));
        }
    }

    @Test
    void invalidPortTest() {
        String input = "65536";
        assertFalse(input.matches(BaseRegex.port));
        input = "-65535";
        assertFalse(input.matches(BaseRegex.port));
    }

    // SIZE
    @Test
    void notASizeTest() {
        String input = "TEST";
        assertFalse(input.matches(BaseRegex.size));
        input = "3343 TEST";
        assertFalse(input.matches(BaseRegex.size));
    }

    @Test
    void validSizeTest() {
        String input = "9999999999";
        assertTrue(input.matches(BaseRegex.size));
        input = "1";
        assertTrue(input.matches(BaseRegex.size));
        input = "1234509";
        assertTrue(input.matches(BaseRegex.size));
    }

    @Test
    void invalidSizeTest() {
        String input = "99999999991";
        assertFalse(input.matches(BaseRegex.size));
        input = "-1";
        assertFalse(input.matches(BaseRegex.size));
    }

    // LINE
    @Test
    void invalidLineTest() {
        String input = "TEST";
        assertFalse(input.matches(BaseRegex.line));
        input = "3343 TEST\r\n";
        assertFalse(input.matches(BaseRegex.line));
        input = " \r\n";
        assertFalse(input.matches(BaseRegex.line));
    }

    @Test
    void validLineTest() {
        String input = "\r\n";
        assertTrue(input.matches(BaseRegex.line));
    }

    // VISIBLECHAR
    @Test
    void invalidVisibleCharTest() {
        String input = "AA";
        assertFalse(input.matches(BaseRegex.visibleChar));

        input = "😁";
        assertFalse(input.matches(BaseRegex.visibleChar));
    }

    @Test
    void validVisibleCharTest() {
        String input = " ";
        assertTrue(input.matches(BaseRegex.visibleChar));
        input = "A";
        assertTrue(input.matches(BaseRegex.visibleChar));
        input = "/";
        assertTrue(input.matches(BaseRegex.visibleChar));
        input = "%";
        assertTrue(input.matches(BaseRegex.visibleChar));
        input = "!";
        assertTrue(input.matches(BaseRegex.visibleChar));
    }

    // PASSCHAR
    @Test
    void invalidPassCharTest() {
        String input = "!";
        assertFalse(input.matches(BaseRegex.passChar));
        input = " ";
        assertFalse(input.matches(BaseRegex.passChar));
    }

    @Test
    void validPassCharTest() {
        String input = "A";
        assertTrue(input.matches(BaseRegex.passChar));
        input = "ù";
        assertTrue(input.matches(BaseRegex.passChar));
    }

    // BINARY
    @Test
    void invalidBinaryTest() {
        String input = "!A";
        assertFalse(input.matches(BaseRegex.binary));
        input = "/9&";
        assertFalse(input.matches(BaseRegex.binary));
    }

    @Test
    void validBinaryTest() {
        String input = "1";
        assertTrue(input.matches(BaseRegex.binary));
        input = "ù";
        assertTrue(input.matches(BaseRegex.binary));
        input = " ";
        assertTrue(input.matches(BaseRegex.binary));
        input = "#";
        assertTrue(input.matches(BaseRegex.binary));
    }

    // PASSWORD
    @Test
    void invalidPasswordTest() {
        String input = "TEST";
        assertFalse(input.matches(BaseRegex.password));
        input = "ADMIN!";
        assertFalse(input.matches(BaseRegex.password));
        input = "AD MIN";
        assertFalse(input.matches(BaseRegex.password));
        input = "XWmjfsu3PCobwVtyuxp9gu9otsZMw8DfxrqJ7MkyYu47XaxM9bqM";
        assertFalse(input.matches(BaseRegex.password));
    }

    @Test
    void validPasswordTest() {
        String input = "G2mQfqvQtB7WNsh6VnGLe47zHKRRUSXNtPKE3XS42itVxYEnin";
        assertTrue(input.matches(BaseRegex.password));
    }

    // BL
    @Test
    void invalidBLTest() {
        String input = "TEST";
        assertFalse(input.matches(BaseRegex.bl));
        input = "  ";
        assertFalse(input.matches(BaseRegex.bl));
    }

    @Test
    void validBLTest() {
        String input = " ";
        assertTrue(input.matches(BaseRegex.bl));
    }

    // LETTER
    @Test
    void invalidLetterTest() {
        String input = "9";
        assertFalse(input.matches(BaseRegex.letter));
        input = "é";
        assertFalse(input.matches(BaseRegex.letter));
        input = " ";
        assertFalse(input.matches(BaseRegex.letter));
        input = "#";
        assertFalse(input.matches(BaseRegex.letter));
    }

    @Test
    void validLetterTest() {
        String input = "A";
        assertTrue(input.matches(BaseRegex.letter));
        input = "a";
        assertTrue(input.matches(BaseRegex.letter));
        input = "Z";
        assertTrue(input.matches(BaseRegex.letter));
        input = "z";
        assertTrue(input.matches(BaseRegex.letter));
    }

    // DIGIT LETTER
    @Test
    void invalidDigitLetterTest() {
        String input = "A1";
        assertFalse(input.matches(BaseRegex.digit_letter));
        input = " ";
        assertFalse(input.matches(BaseRegex.digit_letter));
        input = "12";
        assertFalse(input.matches(BaseRegex.digit_letter));
    }

    @Test
    void validDigitLetterTest() {
        String input = "A";
        assertTrue(input.matches(BaseRegex.digit_letter));
        input = "a";
        assertTrue(input.matches(BaseRegex.digit_letter));
        input = "Z";
        assertTrue(input.matches(BaseRegex.digit_letter));
        input = "z";
        assertTrue(input.matches(BaseRegex.digit_letter));
        input = "1";
        assertTrue(input.matches(BaseRegex.digit_letter));
        input = "9";
        assertTrue(input.matches(BaseRegex.digit_letter));
    }

    // FILE NAME
    @Test
    void invalidDigitFilenameTest() {
        String input = "Photo123454321891111111111.png";
        assertFalse(input.matches(BaseRegex.filename));
        input = " ";
        assertTrue(input.matches(BaseRegex.filename));
        input = "Photo12345111!11.png";
        assertFalse(input.matches(BaseRegex.filename));
    }

    @Test
    void validDigitFilenameTest() {
        String input = "Photo1.png";
        assertTrue(input.matches(BaseRegex.filename));
        input = "Photo12345432189.png";
        assertTrue(input.matches(BaseRegex.filename));
        input = ".";
        assertTrue(input.matches(BaseRegex.filename));
    }

    // DOMAIN
    @Test
    void invalidDomainTest() {
        String input = "Helmo-1";
        assertFalse(input.matches(BaseRegex.domain));
        input = "Helmo-1";
        assertFalse(input.matches(BaseRegex.domain));
        input = "Helm";
        assertFalse(input.matches(BaseRegex.domain));
        input = "Helmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm";
        assertFalse(input.matches(BaseRegex.domain));
    }

    @Test
    void validDomainTest() {
        String input = "Helmo.1";
        assertTrue(input.matches(BaseRegex.domain));
    }

    // HASH FILENAME
    @Test
    void invalidHashFileName(){
        String input = "deklp";
        assertFalse(input.matches(BaseRegex.hash_filename));
        input = "cerkdkeodkednekded,edk,ed,ekd,eod,edekd,ed,ede,d,ekd,ed!";
        assertFalse(input.matches(BaseRegex.hash_filename));
        input = "cerkdkeodkednekmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm";
        assertFalse(input.matches(BaseRegex.hash_filename));
    }

    @Test
    void validHashFileName(){
        String input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertTrue(input.matches(BaseRegex.hash_filename));

    }

    // HASH FILECONTENT
    @Test
    void invalidHashFileContent(){
        String input = "deklp";
        assertFalse(input.matches(BaseRegex.hash_filecontent));
        input = "cerkdkeodkednekded,edk,ed,ekd,eod,edekd,ed,ede,d,ekd,ed!";
        assertFalse(input.matches(BaseRegex.hash_filecontent));
        input = "cerkdkeodkednekmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm";
        assertFalse(input.matches(BaseRegex.hash_filecontent));
    }

    @Test
    void validHashFileContent(){
        String input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertTrue(input.matches(BaseRegex.hash_filecontent));

    }

    // FILE INFO
    @Test
    void invalidFileInfo(){
        String input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertFalse(input.matches(BaseRegex.file_info));
        input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1 80deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertFalse(input.matches(BaseRegex.file_info));
        input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1 78596582816 deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertFalse(input.matches(BaseRegex.file_info));
    }

    @Test
    void validFileInfo(){
        String input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1 60 deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertTrue(input.matches(BaseRegex.file_info));

    }


    // LOGIN
    @Test
    void invalidLogin(){
        String input = "deklpfmp8f5e9z5e2d52d2de5d5z3d2e5de54e1z5s2z5e6d5f4e1";
        assertFalse(input.matches(BaseRegex.login));
        input = "Bap";
        assertFalse(input.matches(BaseRegex.login));
        input = "deklpfmp8ç!§k";
        assertFalse(input.matches(BaseRegex.login));
    }

    @Test
    void validLogin(){
        String input = "LebonBaptiste71";
        assertTrue(input.matches(BaseRegex.login));

    }
}
