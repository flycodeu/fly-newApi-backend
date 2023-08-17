package com.flyBackedn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

/**
 * 正则测试
 */
@SpringBootTest
public class TestRegex {
    public static boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null)) {
            if (ipAddress.equals("localhost")) {
                return true;
            } else {
                return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
            }
        }
        return false;
    }


    @Test
    void testRegex() {
        String ipAddress = "112.110";
        boolean validIPAddress = isValidIPAddress(ipAddress);
        System.out.println(validIPAddress);
    }
}
