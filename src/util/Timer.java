
package util;

/**
 * Provides a simple mechanism to track elapsed clock time
 */
public class Timer {
    private final static double NANOSECS_IN_SEC = 1e9;
    private final static double NANOSECS_IN_MSEC = 1e6;

    private long timestamp;

    public Timer() {
        restart();
    }

    public void restart() {
        this.timestamp = System.nanoTime();
    }

    /**
     * @return the elapsed time in seconds
     */
    public double elapsedSeconds() {
        return elapsedNanoseconds() / NANOSECS_IN_SEC;
    }

    /**
     * @return the elapsed time in milliseconds
     */
    public double elapsedMilliseconds() {
        return elapsedNanoseconds() / NANOSECS_IN_MSEC;
    }

    /**
     * @return the elapsed time in nanoseconds
     */
    public long elapsedNanoseconds() {
        return System.nanoTime() - this.timestamp;
    }
}
