package com.wzd.port.thread;

import com.wzd.port.model.Result;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 扫描指定端口
 */
@Slf4j
public class ScanPointPort implements Callable<Result> {

    private String ip; // 目标IP
    private Set<Integer> portSet; // 待扫描的端口的Set集合
    private int threadNumber, serial, timeout; // 线程数，这是第几个线程，超时时间

    public ScanPointPort(String ip, Set<Integer> portSet, int threadNumber,
                       int serial, int timeout) {
        this.ip = ip;
        this.portSet = portSet;
        this.threadNumber = threadNumber;
        this.serial = serial;
        this.timeout = timeout;
    }


    @Override
    public Result call() throws Exception {
        int port = 0;
        Result result = null;
        Integer[] ports = portSet.toArray(new Integer[portSet.size()]); // Set转数组
        try {
            InetAddress address = InetAddress.getByName(ip);
            Socket socket;
            SocketAddress socketAddress;
            if (ports.length < 1)
                return null;
            for (port = 0 + serial; port <= ports.length - 1; port += threadNumber) {
                Thread th = Thread.currentThread();
                log.info("ip:" + ip + " 端口 " + ports[port] + " 线程名称：" + th.getName());

                socket = new Socket();
                socketAddress = new InetSocketAddress(address, ports[port]);
                try {
                    socket.connect(socketAddress, timeout);
                    socket.close();
                    result = new Result(ip, ports[port], "开放");
                    log.info("result"+result.toString());
                } catch (IOException e) {
                    result = new Result(ip, ports[port], "关闭");
                    log.info("result"+result.toString());
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return result;
    }
}
