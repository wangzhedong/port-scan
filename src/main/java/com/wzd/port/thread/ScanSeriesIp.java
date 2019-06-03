package com.wzd.port.thread;

import com.wzd.port.model.ResponseVO;
import com.wzd.port.scan.PortScan;
import lombok.extern.slf4j.Slf4j;

/**
 * 扫描连续ip
 */
@Slf4j
public class ScanSeriesIp implements Runnable {

    /**
     * ip前缀：192.169.1.
     */
    private String startIpPreixAddr;

    // 起始和结束端口，线程数，这是第几个线程，超时时间
    private int startIpNum, endIpNum, threadNumber, currentThreadNum, timeout;

    public ScanSeriesIp(String startIpPreixAddr, int startIpNum, int endIpNum, int threadNumber, int currentThreadNum) {
        this.startIpPreixAddr = startIpPreixAddr;
        this.startIpNum = startIpNum;
        this.endIpNum = endIpNum;
        this.threadNumber = threadNumber;
        this.currentThreadNum = currentThreadNum;
    }

    @Override
    public void run() {
        int ip = 0;
        for (ip = startIpNum + currentThreadNum; ip <= endIpNum; ip += threadNumber) {
            String ipAddr = this.startIpPreixAddr+ip;
            PortScan portScan = new PortScan();
            ResponseVO vo = portScan.scanSeriesPorts(ipAddr, 20, 30, 800);
            log.info("获取结果："+vo.toString());
        }
    }
}
