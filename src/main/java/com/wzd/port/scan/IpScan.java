package com.wzd.port.scan;

import com.wzd.port.thread.ScanSeriesIp;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 扫描ip地址
 */
@Slf4j
public class IpScan {

    /**
     * 多线程扫描IP段及端口段
     * @param startIpPreixAddr
     * @param startIpNum
     * @param endIpNum
     */
    public void scanLargeIpAndPorts(String startIpPreixAddr,int startIpNum,int endIpNum) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //线程数
        int threadNumber = (endIpNum - startIpNum)/2 ==0 ? 1:(endIpNum - startIpNum)/2;
        for (int i = 0; i < threadNumber; i++) {
            ScanSeriesIp scanSeriesIp = new ScanSeriesIp(startIpPreixAddr,startIpNum,endIpNum,threadNumber,i);
            threadPool.execute(scanSeriesIp);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        while (true) {
            if (threadPool.isTerminated()) {
                log.info("连续ip扫描结束");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 扫描ip段+指定端口
     * @param startIpPreixAddr
     * @param startIpNum
     * @param endIpNum
     */
    public void scanSeriesIpAndPointPorts(String startIpPreixAddr,int startIpNum,int endIpNum) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //线程数
        int threadNumber = (endIpNum - startIpNum)/2 ==0 ? 1:(endIpNum - startIpNum)/2;
        for (int i = 0; i < threadNumber; i++) {
            ScanSeriesIp scanSeriesIp = new ScanSeriesIp(startIpPreixAddr,startIpNum,endIpNum,threadNumber,i);
            threadPool.execute(scanSeriesIp);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        while (true) {
            if (threadPool.isTerminated()) {
                log.info("连续ip扫描结束");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
