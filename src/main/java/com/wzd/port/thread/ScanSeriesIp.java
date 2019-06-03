package com.wzd.port.thread;

import com.wzd.port.constant.TypeEnum;
import com.wzd.port.model.ResponseVO;
import com.wzd.port.scan.PortScan;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 扫描连续ip
 */
@Slf4j
public class ScanSeriesIp implements Runnable {

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
    public void run() {
        int ip = 0;
        for (ip = startEndIpNum[0] + currentThreadNum; ip <= startEndIpNum[1]; ip += threadNumber) {
            String ipAddr = this.startIpPreixAddr+ip;
            //如果是端口段，就端口段扫描，否则扫描指定端口
            PortScan portScan = new PortScan();
            if(type.equals(TypeEnum.SERIES.getType())){
                ResponseVO vo = portScan.scanSeriesPorts(ipAddr, startEndPort[0], startEndPort[1], this.timeout);
                log.info("获取结果："+vo.toString());
            }else{
                portScan.scanPointPorts(ipAddr,this.portSet,this.timeout);
            }


        }
    }
}
