package com.wzd.port.thread;

import com.wzd.port.constant.TypeEnum;
import com.wzd.port.model.Result;
import com.wzd.port.scan.PortScan;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 扫描指定ip
 */
@Slf4j
public class ScanPointIp implements Callable<List<Result>> {


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
    public List<Result> call() throws Exception {
        int ip = 0;
        List<Result> list = new ArrayList<>();
        String[] ips = ipSet.toArray(new String[ipSet.size()]); // Set转数组
        for (ip = 0 + currentThreadNum; ip <= ips.length - 1; ip += threadNumber) {
            String ipAddr = ips[ip];
            PortScan portScan = new PortScan();
            if(type.equals(TypeEnum.POINT.getType())){
                List<Result> r = portScan.scanSeriesPorts(ipAddr, startEndPort[0],  startEndPort[1], timeout);
                if(r != null){
                    list.addAll(r);
                    log.info("获取结果："+r.toString());
                }
            }else{
                List<Result> r = portScan.scanPointPorts(ipAddr,portSet,timeout);
                if(r != null){
                    list.addAll(r);
                    log.info("获取结果："+r.toString());
                }
            }
        }
        return list;
    }
}
