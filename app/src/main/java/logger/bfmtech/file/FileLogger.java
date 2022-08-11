package logger.bfmtech.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import logger.bfmtech.common.Consts;
import logger.bfmtech.common.Level;
import logger.bfmtech.common.Logger;

/**
 * 把日志存入本地文件
 */
public class FileLogger extends Logger {
    // filePath string // 文件保存路径
    private String appName;
    private int maxBufferLength; // 最大缓存日志条数
    private int maxBufferSize; // 最大缓存字符串大小
    private int bufferSize; // 缓存日志字符串大小
    private String filePath;
    private Deque<String> queue; // 作为普通队列（先进先出）
    // 创建任务队列
    private ScheduledExecutorService scheduledExecutorService;

    public FileLogger(String appName) throws Exception {
        this.appName = appName;
        InetAddress ip = InetAddress.getLocalHost();
        maxBufferSize = 1 * 1024 * 1024;
        maxBufferLength = 100;
        bufferSize = 0;
        filePath = String.format("%s/%s/%s/", "/var/winnerlogs", appName, ip.getHostName());
        Consts.createFileDirectory(filePath);
        queue = new LinkedList<String>();
        scheduledExecutorService = new ScheduledThreadPoolExecutor(10);
        createInterval();
    }

    @Override
    public void Log(Level level, String... messages) {
        if (messages.length > 0) {
            String msg = Consts.GetApplicationLogStr(level, appName, messages);
            bufferSize += msg.length();
            queue.add(msg);
            if (queue.size() >= maxBufferLength || bufferSize >= maxBufferSize) {
                flush();
            }
        }
    }

    @Override
    public void Close() {
        flush();
    }

    private void createInterval() {
        // 执行任务 每 3s 执行一次
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            flush();
        }, 3, 3, TimeUnit.SECONDS);
    }

    private void flush() {
        if (queue.size() > 0) {
            bufferSize = 0;
            // 失败重试3次
            int retryNum = 2;
            while (retryNum > 0) {
                try {
                    writeFile();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                retryNum--;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (retryNum <= 0) {
                for (int i = 0; i < queue.size(); i++) {
                    System.out.println(queue.poll());
                }
            }
        }
    }

    private void writeFile() throws Exception {
        File file = new File(String.format("%s/logger-%s.log", filePath, Consts.GetData()));
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
        while (queue.iterator().hasNext()) {
            w.write(queue.poll() + "\n");
        }
        w.flush(); // 清除缓冲区
        w.close(); // 关闭流
    }
}