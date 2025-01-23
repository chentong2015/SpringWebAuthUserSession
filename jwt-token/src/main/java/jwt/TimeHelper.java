package jwt;

import java.util.Date;

public class TimeHelper {

    // 600s = 10 minutes Token有效期时间
    private static int expirationTimeMs = 600000;

    private static long getCurrentTimeMillis() {
        return (new Date()).getTime();
    }

    public static Date generateCurrentDate() {

        return new Date(getCurrentTimeMillis());
    }

    public static Date generateExpirationDate() {

        return new Date(getCurrentTimeMillis() + expirationTimeMs);
    }
}
