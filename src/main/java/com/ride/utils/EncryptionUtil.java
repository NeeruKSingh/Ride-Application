package com.ride.utils;


import com.ride.constants.Constants;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class EncryptionUtil {


    public static String getEncryptedPwd(String password){
        String hash = Constants.MD5_HASH;
        String md5Hex = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        return md5Hex;
    }
}
