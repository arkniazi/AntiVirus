package com.example.capk.antivirus;

import android.text.style.TabStopSpan;
import android.util.Base64;

import com.google.common.io.BaseEncoding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.util.Base64.*;

/**
 * Created by capk on 2/16/18.
 */

public class SHA {
// github source https://gist.github.com/kostiakoval/9856908
public static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
        int halfbyte = (data[i] >>> 4) & 0x0F;
        int two_halfs = 0;
        do {
            if ((0 <= halfbyte) && (halfbyte <= 9))
                buf.append((char) ('0' + halfbyte));
            else
                buf.append((char) ('a' + (halfbyte - 10)));
            halfbyte = data[i] & 0x0F;
        } while(two_halfs++ < 1);
    }
    return buf.toString();
}

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA1");

        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        char[] charArray = convertToHex(md.digest()).toCharArray();

        try {
            byte[] byteArray = Hex.decodeHex(charArray);
            return BaseEncoding.base64().encode(byteArray);
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String SHA256(byte[] text)  {
        try {
        MessageDigest md = MessageDigest.getInstance("SHA256");

        md.update(text, 0, text.length);
        char[] charArray = convertToHex(md.digest()).toCharArray();

            byte[] byteArray = Hex.decodeHex(charArray);
            return BaseEncoding.base64().encode(byteArray);
        } catch (DecoderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
