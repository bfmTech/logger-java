package logger.bfmtech;

import logger.bfmtech.common.LoggerMethod;
import logger.bfmtech.console.ConsoleLogger;
import logger.bfmtech.file.FileLogger;
import logger.bfmtech.http.HttpLogger;

/**
 * 初始化Logger 日志
 */
public class BfmLogger {
    public static logger.bfmtech.common.Logger initialize(String appName, LoggerMethod method) throws Exception {
        if (LoggerMethod.Console == method) {
            return new ConsoleLogger(appName);
        } else if (LoggerMethod.File == method) {
            return new FileLogger(appName);
        } else if (LoggerMethod.Http == method) {
            return new HttpLogger(appName);
        } else {
            return new ConsoleLogger(appName);
        }
    }
}
