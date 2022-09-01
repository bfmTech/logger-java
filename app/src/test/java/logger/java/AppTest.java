/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package logger.java;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import logger.bfmtech.BfmLogger;
import logger.bfmtech.common.AccessLog;
import logger.bfmtech.common.Logger;
import logger.bfmtech.common.LoggerMethod;

class AppTest {
    public int a = 0;
    private String ss = "这是测试的消息这是这";
    @Test
    void appHasAGreeting() {
        try {
            // for( int i =0 ;i< 10;i++){
            //     ss += ss;
            // }
            // System.out.println("ss长度==="+ss.length());
            ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);
            // a = 0;
            /**
             * 日志的传送方式
             * LoggerMethod.Console 打印到输出台
             * LoggerMethod.File 保存到本地
             * LoggerMethod.Http 上传到阿里云日志平台
             */
            
            Logger myLogger = BfmLogger.initialize("appName", LoggerMethod.Http);
            myLogger.Debug("测试下Debug1");
            myLogger.Info("测试下Info1");
            myLogger.Warn("测试下Warn1");
            myLogger.Access(new AccessLog("测试下Access1", 1, System.currentTimeMillis(),
            System.currentTimeMillis() + 24 * 60 * 60, "referer", "httphost", "interfac",
            "reqquery", "reqbody",
            "resbody", "clientip", "useragent", "headers","X-Request-ID"));
            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                a++;
                // System.out.println(a+"");
                myLogger.Error(new Exception(ss + a));
                // if (a == 2000) {
                //     scheduledExecutorService.shutdown();
                // }
            }, 0, 1, TimeUnit.MILLISECONDS);
            // 
            myLogger.Error(new Exception("这是error测试消息" + a));
            Thread.sleep(40000);
            // log 关闭方法 模式是 LoggerMethod.Http File 时候必须调用 否则会有数据丢失的风险
            myLogger.Close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
