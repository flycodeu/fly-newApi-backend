package com.flySdk.Utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 加密
 */
public class SignUtils {

    /**
     * 加密用户参数和secret
     * @param body
     * @param secretKey
     * @return
     */
    public  static String genSign(String body, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA1);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }

}
