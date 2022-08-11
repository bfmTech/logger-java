package logger.bfmtech.common;
/**
 * 日志的传送方式
 * Console  打印到输出台
 * File     保存到本地
 * Http     上传到阿里云日志平台
 */
public enum LoggerMethod {
    Console,
    File,
    Http
}
