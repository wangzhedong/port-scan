package com.wzd.port.thread;

import com.wzd.port.constant.TypeEnum;
import com.wzd.port.model.ResponseVO;
import com.wzd.port.scan.PortScan;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 扫描指定ip
 */
@Slf4j
public class ScanPointIp implements Runnable {


    /**
     * 指定ip的集合
     */
    private Set<String> ipSet;

    /**
     * 指定端口的集合
     */
    private Set<Integer> portSet;
    /**
     * 起始和终止的端口段
     */
    private int[] startEndPort;

    /**
     * 类型
     */
    private String type;

    // 线程数，这是第几个线程，超时时间
    private int threadNumber, currentThreadNum, timeout;

    public ScanPointIp(Set<String> ipSet,int[] startEndPort, int threadNumber, int currentThreadNum, int timeout) {
        this.ipSet = ipSet;
        this.startEndPort = startEndPort;
        this.type = TypeEnum.POINT.getType();
        this.threadNumber = threadNumber;
        this.currentThreadNum = currentThreadNum;
        this.timeout = timeout;
    }

    public ScanPointIp(Set<String> ipSet,Set<Integer> portSet, int threadNumber, int currentThreadNum, int timeout) {
        this.ipSet = ipSet;
        this.portSet = portSet;
        this.type = TypeEnum.SERIES.getType();
        this.threadNumber = threadNumber;
        this.currentThreadNum = currentThreadNum;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        int ip = 0;
        String[] ips = ipSet.toArray(new String[ipSet.size()]); // Set转数组
        for (ip = 0 + currentThreadNum; ip <= ips.length - 1; ip += threadNumber) {
            String ipAddr = ips[ip];
            PortScan portScan = new PortScan();
            if(type.equals(TypeEnum.POINT.getType())){
                ResponseVO vo = portScan.scanSeriesPorts(ipAddr, startEndPort[0],  startEndPort[1], timeout);
                log.info("获取结果："+vo.toString());
            }else{
                ResponseVO vo = portScan.scanPointPorts(ipAddr,portSet,timeout);
                log.info("获取结果："+vo.toString());
            }

        }
    }
}
