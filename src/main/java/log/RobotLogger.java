package log;

public class RobotLogger {
    private static final LogWindowSource defaultLogSource;

    static {
        defaultLogSource = new LogWindowSource(100);
    }

    private RobotLogger() {
    }

    public static void debug(String strMessage) {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }

    public static LogWindowSource getDefaultLogSource() {
        return defaultLogSource;
    }
}
