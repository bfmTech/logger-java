package logger.bfmtech.http;

import logger.bfmtech.common.Consts;
import logger.bfmtech.common.Level;
import logger.bfmtech.common.Logger;
import com.aliyun.openservices.log.common.LogItem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.TextUtils;

import com.aliyun.openservices.aliyun.log.producer.Callback;
import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
import com.aliyun.openservices.aliyun.log.producer.Result;
import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;

/**
 * 上传日志信息到 阿里云sls
 */
public class HttpLogger extends Logger {
    // 配置AccessKey、服务入口、Project名称、Logstore名称等相关信息。
    // 阿里云访问密钥AccessKey。更多信息，请参见访问密钥。阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维。
    private String accessId;
    private String accessKey;
    // Project名称。
    private String projectName = "";
    // Logstore名称。
    private String logstoreName = "";
    // 应用 名称。
    private String appName = "";
    // 发送日志
    private LogProducer producer;

    public HttpLogger(String appName) throws Exception {
        this.appName = appName;
        String endpoint = System.getenv("LOGGER_ALIYUN_ENDPOINT");
        if (TextUtils.isEmpty(endpoint)) {
            endpoint = "cn-hangzhou.log.aliyuncs.com";
        }
        projectName = System.getenv("LOGGER_ALIYUN_PROJECTNAME");
        if (TextUtils.isEmpty(projectName)) {
            projectName = "k8s-log-custom-zwdfroh2";
        }
        logstoreName = System.getenv("LOGGER_ALIYUN_LOGSTORENAME");
        if (TextUtils.isEmpty(logstoreName)) {
            logstoreName = "config-operation-log";
        }
        accessId = System.getenv("LOGGER_ALIYUN_ACCESSKEYID");
        if (TextUtils.isEmpty(accessId)) {
            throw new Exception("invalid env LOGGER_ALIYUN_ACCESSKEYID");
        }
        accessKey = System.getenv("LOGGER_ALIYUN_ACCESSKEYSECRET");
        if (TextUtils.isEmpty(accessKey)) {
            throw new Exception("invalid env LOGGER_ALIYUN_ACCESSKEYSECRET");
        }
        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setBatchSizeThresholdInBytes(1 * 1024 * 1024);
        producerConfig.setLingerMs(3000);
        producerConfig.setBatchCountThreshold(1000);
        producer = new LogProducer(producerConfig);
        producer.putProjectConfig(new ProjectConfig(projectName, endpoint, accessId, accessKey, ""));
    }

    @Override
    public void Log(Level level, String... messages) {
        if (messages.length > 0) {
            sendLog(level, messages);
        }
    }

    private void sendLog(Level level, String... messages) {
        // 失败重试3次
        int retryNum = 2;
        while (retryNum > 0) {
            retryNum--;
            LogItem logItem = new LogItem();
            logItem.PushBack("content", Consts.GetApplicationLogStr(level, appName, messages, 5));
            List<LogItem> logItems = new ArrayList<LogItem>();
            logItems.add(logItem);
            try {
                producer.send(this.projectName, this.logstoreName, this.appName, "", logItems,
                        new MyCallback(messages));
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
                sendLog(level, messages);
            } catch (ProducerException e) {
                e.printStackTrace();
                sendLog(level, messages);
            }
        }

    }

    @Override
    public void Close() {
        try {
            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyCallback implements Callback {
        private String[] msg;

        public MyCallback(String... msg) {
            this.msg = msg;
        }

        @Override
        public void onCompletion(Result result) {
            if (result != null && !result.isSuccessful()) {
                System.out.println(msg);
            }
        }
    }

}
