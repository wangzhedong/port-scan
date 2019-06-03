package com.wzd.port.scan;

import com.wzd.port.model.ResponseVO;
import com.wzd.port.model.Result;
import com.wzd.port.thread.ScanSeriesPort;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class PortScan {


    /**
     * 多线程扫描目标主机一个段的端口开放情况
     *
     * @param ip
     *            待扫描IP或域名,eg:180.97.161.184 www.zifangsky.cn
     * @param startPort
     *            起始端口
     * @param endPort
     *            结束端口
     * @param timeout
     *            连接超时时间
     * */
    public ResponseVO scanSeriesPorts(String ip, int startPort, int endPort, int timeout) {
        boolean flag = false;
        try {
            flag = InetAddress.getByName(ip).isReachable(timeout);
        } catch (IOException e) {
            log.error(e.getMessage());
            flag = false;
        }
        if(!flag){
            return ResponseVO.errorInstance("该"+ip+"地址不可用");
        }

        if(startPort < 0 || endPort > 65535){
            return ResponseVO.errorInstance("端口不能小于0或者大于65535");
        }

        List<Future<Result>> resultList = new ArrayList<Future<Result>>();

        //线程数
        int threadNumber = endPort - startPort;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < threadNumber; i++) {
            ScanSeriesPort scanSeriesPort = new ScanSeriesPort(ip, startPort, endPort,
                    threadNumber, i, timeout);
            Future<Result> res = threadPool.submit(scanSeriesPort);
            //不能直接取结果，会导致线程阻塞
            resultList.add(res);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        while (true) {
            if (threadPool.isTerminated()) {
                log.info(ip+"的所有端口扫描结束");
                List<Result> results = new ArrayList<>();
                for( Future<Result> fr:resultList){
                    try {
                        results.add(fr.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return ResponseVO.successInstance(results);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ResponseVO scanPointPorts(String ip, Set<Integer> ports, int timeout) {
        boolean flag = false;
        try {
            flag = InetAddress.getByName(ip).isReachable(timeout);
        } catch (IOException e) {
            log.error(e.getMessage());
            flag = false;
        }
        if(!flag){
            return ResponseVO.errorInstance("该"+ip+"地址不可用");
        }

        if(startPort < 0 || endPort > 65535){
            return ResponseVO.errorInstance("端口不能小于0或者大于65535");
        }

        List<Future<Result>> resultList = new ArrayList<Future<Result>>();

        //线程数
        int threadNumber = endPort - startPort;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < threadNumber; i++) {
            ScanSeriesPort scanSeriesPort = new ScanSeriesPort(ip, startPort, endPort,
                    threadNumber, i, timeout);
            Future<Result> res = threadPool.submit(scanSeriesPort);
            //不能直接取结果，会导致线程阻塞
            resultList.add(res);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        while (true) {
            if (threadPool.isTerminated()) {
                log.info(ip+"的所有端口扫描结束");
                List<Result> results = new ArrayList<>();
                for( Future<Result> fr:resultList){
                    try {
                        results.add(fr.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return ResponseVO.successInstance(results);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
