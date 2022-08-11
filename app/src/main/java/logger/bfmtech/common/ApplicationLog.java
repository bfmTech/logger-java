package logger.bfmtech.common;

/*
 *  ApplicationLog 日志格式参数
 */
public class ApplicationLog {
    private String appname;
    private Level level;
    private String logtime;
    private String message;
    private String stack;

    public ApplicationLog(String appname, Level level, String logtime, String message, String stack) {
        this.appname = appname;
        this.level = level;
        this.logtime = logtime;
        this.message = message;
        this.stack = stack;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s] [%s] [%s] %s", logtime, level, appname, stack, message);
    }

}