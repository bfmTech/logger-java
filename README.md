# logger-java

logger sdk java 版本。将日志内容按照统一格式通过控制台、文件、http上传到阿里云sls，支持缓存上传。


## 安装

```bash
可以把项目clone下来，打包成jar使用
```

## 快速开始

可参考 app/src/test 中的例子

**重点：BfmLogger 应申明为全局变量，初始化一次！！！**

根据情况替换 [应用名称appName]和选择日志上传方式
```go
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
    @Test
    void appHasAGreeting() {
        try {
            Logger myLogger = BfmLogger.initialize("appName", LoggerMethod.Http);
            myLogger.Debug("测试下Debug1");
            myLogger.Info("测试下Info1");
            myLogger.Warn("测试下Warn1");
            myLogger.Access(new AccessLog("测试下Access1", 1, System.currentTimeMillis(),
            System.currentTimeMillis() + 24 * 60 * 60, "referer", "httphost", "interfac",
            "reqquery", "reqbody",
            "resbody", "clientip", "useragent", "headers","X-Request-ID"));
            myLogger.Error(new Exception("这是error测试消息"));
            // log 关闭方法 模式是 LoggerMethod.Http File 时候必须调用 否则会有数据丢失的风险
            myLogger.Close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
```

## 详细说明

日志上传方式

|  上传方式   | 使用环境  | 说明  |
|  ----  | ----  | ----  |
| Console  | 容器平台(推荐) | 日志输出到控制台，Logtail采集 |
| File  | 容器平台 | 日志根据日期保存到指定目录，Logtail采集 |
| Http  | 任意环境(需配置阿里云相关环境变量) | http上传日志到阿里sls |


不同的方法的日志类型level不同 可在查询时筛选
```code
-logger.Debug    //支持多个参数，可根据每个参数检索
-logger.Info     //支持多个参数，可根据每个参数检索
-logger.Warn     //支持多个参数，可根据每个参数检索
-logger.Error    //只接受Error对象 （推送钉钉告警消息）
-logger.Access   //access日志，根据方法提示的参数传递
```

access日志 字段说明

|  字段   | 类型  | 说明  |
|  ----  | ----  | ----  |
| method  | string | 请求方法 |
| status  | number | HTTP请求状态 |
| beginTime  | number | 请求开始时间(秒时间戳) |
| endTime  | number | 请求结束时间(秒时间戳) |
| referer  | string | 请求来源 |
| httpHost  | string | 请求地址 |
| _interface  | string | 请求接口 |
| reqQuery  | string | 请求url参数 |
| reqBody  | string | 请求body参数 |
| resBody  | string | 请求返回参数 |
| clientIp  | string | 客户端IP |
| userAgent  | string | 用户终端浏览器等信息  |
| reqId  | string | header[X-Request-ID]请求id，用于链路跟踪 |
| headers  | string | 其他数据，比如：token |
**关于链路追踪的实现**  
* 获取请求header中的X-Request-ID (Nginx access日志中的req_id) 标记为req_id  
* 所有应用日志记录上req_id  
* 应用上下游调用时，透传 X-Request-ID  
* 可在监控系统中通过req_id在访问日志和应用日志查询到完成的请求链路  


**上传方式为Http时环境变量说明**
|  变量名   | 说明  |
|  ----  | ----  |
| LOGGER_ALIYUN_ENDPOINT  | 阿里云sls公网服务入口 |
| LOGGER_ALIYUN_PROJECTNAME  | 阿里云sls项目（Project） |
| LOGGER_ALIYUN_LOGSTORENAME  | 阿里云sls日志库（Logstore） |
| LOGGER_ALIYUN_ACCESSKEYID  | AccessKey ID，建议使用RAM用户的AccessKey信息。 |
| LOGGER_ALIYUN_ACCESSKEYSECRET  | AccessKey Secret，建议使用RAM用户的AccessKey信息。 |

**非k8s容器平台`File`类型的日志使用说明**
* 日志文件存储在`NODE_APP_DATA`环境变量目录下
* 存储目录为：指定目录/应用名称/主机名/logger-YYYY-MM-DD.log
* 设置文件存储天数 logger.SetStoringDays(30)

## 注意
1、 `appName` 需唯一，且有意义，用于检索和报错时通知负责人。  
2、日志上传方式为`File`或`Http`时，程序退出时必须调用Close()，否则可能导致最后部分日志丢失。  
3、日志上传方式为`Http`时，需要配置阿里云相关环境变量，请联系管理员。  
4、日志默认存储时长为**360**天，如有特殊需求请联系管理员。  
