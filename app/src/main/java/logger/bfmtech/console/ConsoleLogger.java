package logger.bfmtech.console;

import logger.bfmtech.common.Consts;
import logger.bfmtech.common.Level;
import logger.bfmtech.common.Logger;

/**
 * 输出日志到控制台
 */
public class ConsoleLogger extends Logger {

    private String appName;

    public ConsoleLogger(String appName) {
        this.appName = appName;
    }

    @Override
    public void Log(Level level, String... messages) {
        if (messages.length > 0) {
            if (level == Level.Error) {
                System.err.println(Consts.GetApplicationLogStr(level, appName, messages,4));
            } else {
                System.out.println(Consts.GetApplicationLogStr(level, appName, messages,4));
            }
        }
    }

    @Override
    public void Close() {

    }
}
