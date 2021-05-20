package utils;

public class BitsConversions {
    private static final long UNSIGNED_MASK = 0x7fffffffffffffffL;
    private static final double DOUBLE_NORM = 1.0 / (1L << 53);

    public static double toDouble(long n) {
        double d = n & UNSIGNED_MASK;
        if (n < 0) {
            d += 0x1.0p63;
        }
        return d;
    }

    public static double doubleNorm(long n) {
        return (n >>> 11) * DOUBLE_NORM;
    }
}
