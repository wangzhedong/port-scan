package com.wzd.port.thread;

import com.wzd.port.model.Result;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 扫描连续端口
 */
@Slf4j
public class ScanSeriesPort implements Callable<List<Result>> {
    // 目标IP
    private String ip;

    // 起始和结束端口，线程数，这是第几个线程，超时时间
    private int startPort, endPort, threadNumber, serial, timeout;

    public ScanSeriesPort(String ip, int startPort, int endPort,
                          int threadNumber, int serial, int timeout) {
        this.ip = ip;
        this.startPort = startPort;
        this.endPort = endPort;
        this.threadNumber = threadNumber;
        this.serial = serial;
        this.timeout = timeout;
    }

    @Override
    public List<Result> call() throws Exception {
        int port = 0;
        List<Result> resultList = new ArrayList<>();
        try {
            InetAddress address = InetAddress.getByName(ip);
            Socket socket;
            SocketAddress socketAddress;
            for (port = startPort + serial; port <= endPort; port += threadNumber) {
                Thread th = Thread.currentThread();
                log.info("ip:" + ip + " 端口 " + port + " 线程名称：" + th.getName());

                socket = new Socket();
                socketAddress = new InetSocketAddress(address, port);
                try {
                    socket.connect(socketAddress, timeout); // 超时时间
                    socket.close();
                    Result r = new Result(ip, port, "开放");
                    resultList.add(r);
                } catch (IOException e) {
                    Result r = new Result(ip, port, "关闭");
                    resultList.add(r);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
