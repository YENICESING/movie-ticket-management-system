package com.rabbiter.cm.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
///////这段 Java 代码定义了一个名为 ProcessContextAware 的组件类，它实现了 ServletContextAware 接口。
// 该类的主要目的是在 Spring 应用程序启动时，根据操作系统的不同，执行关闭占用指定端口的进程的操作。
// 它使用 @Value 注解从配置文件中获取 server.port 的值，然后通过执行系统命令来查找并关闭占用该端口的进程。
@Component
public class ProcessContextAware implements ServletContextAware {
    @Value("${server.port}")
    private String port;

    @Override
    public void setServletContext(ServletContext servletContext) {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows系统关闭占用指定端口的逻辑
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr " + port);
                Process process = processBuilder.start();
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.trim().split("\\s+");
                    String pid = tokens[tokens.length - 1];
                    ProcessBuilder killProcess = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /PID " + pid);
                    killProcess.start();
                }
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // Linux或Mac OS系统关闭占用指定端口的逻辑
                ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "lsof -ti:" + port + " | xargs kill -9");
                processBuilder.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}