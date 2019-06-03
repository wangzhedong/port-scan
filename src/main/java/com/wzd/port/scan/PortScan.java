package com.wzd.port.scan;

import com.wzd.port.model.ResponseVO;
import com.wzd.port.model.Result;
import com.wzd.port.thread.ScanPointPort;
import com.wzd.port.thread.ScanSeriesPort;
import com.wzd.port.utils.NetWorkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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
     * 多线程扫描端口段
     * @param ip
     * @param startPort
     * @param endPort
     * @param timeout
     * @return
     */
    public ResponseVO scanSeriesPorts(String ip, int startPort, int endPort, int timeout) {
        boolean flag = NetWorkUtils.isPing(ip,timeout);
        if(!flag){
            return ResponseVO.errorInstance("该"+ip+"地址不可用");
        }
        String msg = NetWorkUtils.checkPort(startPort,endPort);
        if(!StringUtils.isEmpty(msg)){
            return ResponseVO.errorInstance(msg);
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

    /**
     * 扫描指定端口
     * @param ip
     * @param ports
     * @param timeout
     * @return
     */
    public ResponseVO scanPointPorts(String ip, Set<Integer> ports, int timeout) {
        boolean flag = NetWorkUtils.isPing(ip,timeout);
        if(!flag){
            return ResponseVO.errorInstance("该"+ip+"地址不可用");
        }

        if(ports == null || ports.isEmpty()){
            ResponseVO.errorInstance("该"+ip+"端口为空");
        }

        List<Future<Result>> resultList = new ArrayList<Future<Result>>();

        //线程数
        int threadNumber = (ports.size())/2 ==0 ? 1:(ports.size())/2;

        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < threadNumber; i++) {
            ScanPointPort scanSeriesPort = new ScanPointPort(ip, ports,
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
