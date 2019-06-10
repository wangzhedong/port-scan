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
 * 扫描连续ip
 */
@Slf4j
public class ScanSeriesIp implements Callable<List<Result>> {

    /**
     * ip前缀：192.169.1.
     */
    private String startIpPreixAddr;

    /**
     * 类型
     */
    private String type;

    // 起始和结束端口，线程数，这是第几个线程，超时时间
    private int threadNumber, currentThreadNum, timeout;

    /**
     * 起始和终止的ip段
     */
    private int[] startEndIpNum;

    /**
     * 起始和终止的端口段
     */
    private int[] startEndPort;
    /**
     * 指定端口集合
     */
    private Set<Integer> portSet;

    public ScanSeriesIp(String startIpPreixAddr,int[] startEndIpNum, int[] startEndPort,int threadNumber, int currentThreadNum, int timeout ) {
        this.startIpPreixAddr = startIpPreixAddr;
        this.type = TypeEnum.SERIES.getType();
        this.threadNumber = threadNumber;
        this.currentThreadNum = currentThreadNum;
        this.timeout = timeout;
        this.startEndIpNum = startEndIpNum;
        this.startEndPort = startEndPort;
    }

    public ScanSeriesIp(String startIpPreixAddr,int[] startEndIpNum,Set<Integer> portSet,int threadNumber, int currentThreadNum, int timeout ) {
        this.startIpPreixAddr = startIpPreixAddr;
        this.startEndIpNum = startEndIpNum;
        this.threadNumber = threadNumber;
        this.currentThreadNum = currentThreadNum;
        this.portSet = portSet;
        this.type = TypeEnum.POINT.getType();;
        this.timeout = timeout;
    }

    @Override
    public List<Result> call() throws Exception {
        int ip = 0;
        List<Result> list = new ArrayList<>();
        for (ip = startEndIpNum[0] + currentThreadNum; ip <= startEndIpNum[1]; ip += threadNumber) {
            String ipAddr = this.startIpPreixAddr+ip;
            //如果是端口段，就端口段扫描，否则扫描指定端口
            PortScan portScan = new PortScan();
            if(type.equals(TypeEnum.SERIES.getType())){
                List<Result> r = portScan.scanSeriesPorts(ipAddr, startEndPort[0], startEndPort[1], this.timeout);
                if(r != null){
                    list.addAll(r);
                    log.info("获取结果："+r.toString());
                }
            }else{
                List<Result> r = portScan.scanPointPorts(ipAddr,this.portSet,this.timeout);
                if(r != null){
                    list.addAll(r);
                    log.info("获取结果："+r.toString());
                }
            }
        }
        return list;
    }
}
