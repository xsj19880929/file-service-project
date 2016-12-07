package core.api.file.token;

import core.framework.crypto.AES;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

/**
 * @author nick.guo
 */
public class TokenBuilder {
    private static final String KEY = "ygccw168";
    private static final String MESSAGE = "ygccw";
    private static final char SUFFIX = '|';
    private String cookie;
    private Token token;
    private String path;

    public TokenBuilder() {
    }

    public TokenBuilder setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public TokenBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public TokenBuilder(Token token) {
        this.token = token;
    }


    public Token build() throws Exception {
        AES aes = new AES();
        key(aes);
        return new Token(parseByte2HexStr(aes.encrypt(encryptedMessage(cookie, MESSAGE, path, newDate()))));
    }

    public Token decode() throws Exception {
        AES aes = new AES();
        key(aes);
        return decode(new String(aes.decrypt(parseHexStr2Byte(token.get())), "UTF-8"));
    }

    private void key(AES aes) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(KEY.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        aes.setKey(enCodeFormat);
    }

    private Token decode(String message) {
        String[] strs = message.split("\\" + SUFFIX);
        token.setCookie(strs[0]);
        token.setPath(strs[2]);
        token.setDate(strs[3]);

        return token;
    }

    private byte[] encryptedMessage(String... messages) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(SUFFIX);
        }
        return sb.toString().getBytes("UTF-8");
    }

    public String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return new byte[0];
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public String newDate() {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
    }

    public static class Token {
        private final String content;
        private String cookie;
        private String date;
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Token(String content) {
            this.content = content;
        }

        public String get() {
            return content;
        }

        @Override
        public String toString() {
            return content;
        }
    }


   /* public static void main(String[] args) throws Exception {
        String cookie = UUID.randomUUID().toString();
        Token token = new TokenBuilder().setCookie("123").setPath("/private/20151228/842f059d3d6be.jpg").build();
//       Token token = new Token("6A849BA1FD1186CB7A76026C9F47EDD0F9CEC5F7CFED6B4391604B542149E0A7C014B6FA3A1652F179CF62F2C538358F9C1F8BEDE8EBB10989CD06F7E92A0F501D1C528726A5365F77EA8FD096F21C84D0212A69B8BF0A32443D9283F7424E7811F5F5FB1397006518B6EA478E0A375A892189B37DCDF55DF569511B03EE5960");
        System.out.println(token);
        new TokenBuilder(token).decode().getCookie();
        System.out.println(token.getCookie());
        System.out.println(token.getPath());
        System.out.println(token.getDate());
        System.out.println(token.toString().length());

    }*/

}
