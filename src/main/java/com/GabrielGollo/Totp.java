package com.GabrielGollo;

import org.apache.commons.codec.binary.Base32;
import static org.apache.commons.codec.binary.Hex.encodeHexString;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import static javax.crypto.Mac.getInstance;


import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.lang.reflect.UndeclaredThrowableException;

import static java.lang.System.arraycopy;
import static java.lang.Long.toHexString;
import static java.lang.System.currentTimeMillis;

public class Totp {
    Base32 base32;
    int millisToGenerateCode;
    Totp(){
        millisToGenerateCode = 30000;
        base32 = new Base32();
    }
    public String getOTPCode(String key){
        long step = getStep();

        byte[] bytes = base32.decode(key);
        String hexKey = encodeHexString(bytes);

        String steps = toHexString(step).toUpperCase();
        while (steps.length() < 16) {
            steps = "0" + steps;
        }
        final byte[] text = hexStr2Bytes(steps);
        final byte[] keyBytes = hexStr2Bytes(hexKey);
        final byte[] hash = hmac_sha1(keyBytes, text);
        final int offset = hash[hash.length - 1] & 0xf;
        final int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        final int otp = binary % 1000000;

        String result = Integer.toString(otp);
        while (result.length() < 6) {
            result = "0" + result;
        }
        return result;
    }

    private long getStep() {
        return currentTimeMillis() / millisToGenerateCode;
    }

    private static byte[] hexStr2Bytes(final String hex) {
        final byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        final byte[] ret = new byte[bArray.length - 1];
        arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    private static byte[] hmac_sha1(final byte[] keyBytes, final byte[] text) {
        try {
            final Mac hmac = getInstance("HmacSHA1");
            final SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (final GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

}
