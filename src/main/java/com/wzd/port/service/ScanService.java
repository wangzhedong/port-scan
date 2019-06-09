package com.wzd.port.service;

import com.wzd.port.model.ResponseVO;
import com.wzd.port.model.Result;
import com.wzd.port.thread.ScanPointIp;
import com.wzd.port.thread.ScanSeriesIp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class ScanService {

    /**
     * 多线程扫描IP段+端口段
     *
     * @param startIpPreixAddr
     * @param startEndIpNum
     * @param startEndPort
     * @param timeout          800毫秒
     */
    public ResponseVO scanSeriesIpPorts(String startIpPreixAddr, int[] startEndIpNum, int[] startEndPort, int timeout) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        List<Future<ResponseVO>> resultList = new ArrayList<Future<ResponseVO>>();

        //线程数
        int threadNumber = (startEndIpNum[1] - startEndIpNum[0]) / 2 == 0 ? 1 : (startEndIpNum[1] - startEndIpNum[0]) / 2;
        for (int i = 0; i < threadNumber; i++) {
            ScanSeriesIp scanSeriesIp = new ScanSeriesIp(startIpPreixAddr, startEndIpNum, startEndPort, threadNumber, i, timeout);
            //threadPool.execute(scanSeriesIp);
            Future<ResponseVO> res = threadPool.submit(scanSeriesIp);
            resultList.add(res);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        return this.handleResult(threadPool,resultList);
    }

    /**
     * 扫描ip段+指定端口
     *
     * @param startIpPreixAddr
     * @param startEndIpNum
     * @param ports
     * @param timeout          800毫秒
     */
    public ResponseVO scanSeriesIpAndPointPorts(String startIpPreixAddr, int[] startEndIpNum, Set<Integer> ports, int timeout) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        List<Future<ResponseVO>> resultList = new ArrayList<Future<ResponseVO>>();

        //线程数
        int threadNumber = (startEndIpNum[1] - startEndIpNum[0]) / 2 == 0 ? 1 : (startEndIpNum[1] - startEndIpNum[0]) / 2;
        for (int i = 0; i < threadNumber; i++) {
            ScanSeriesIp scanSeriesIp = new ScanSeriesIp(startIpPreixAddr, startEndIpNum, ports, threadNumber, i, timeout);
            //threadPool.execute(scanSeriesIp);
            Future<ResponseVO> res = threadPool.submit(scanSeriesIp);
            resultList.add(res);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        return this.handleResult(threadPool,resultList);
    }

    /**
     * 扫描指定ip+端口段
     *
     * @param ipSet
     * @param startEndPort
     * @param timeout
     */
    public ResponseVO scanPointIpAndSeriesPorts(Set<String> ipSet, int[] startEndPort, int timeout) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        List<Future<ResponseVO>> resultList = new ArrayList<Future<ResponseVO>>();

        //线程数
        int threadNumber = (ipSet.size()) / 2 == 0 ? 1 : (ipSet.size()) / 2;
        for (int i = 0; i < threadNumber; i++) {
            ScanPointIp scanPointIp = new ScanPointIp(ipSet, startEndPort, threadNumber, i, timeout);
            //threadPool.execute(scanPointIp);
            Future<ResponseVO> res = threadPool.submit(scanPointIp);
            resultList.add(res);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        return this.handleResult(threadPool,resultList);

    }

    /**
     * 扫描指定ip+指定端口段
     *
     * @param ipSet
     * @param portSet
     * @param timeout
     */
    public ResponseVO scanPointIpPorts(Set<String> ipSet, Set<Integer> portSet, int timeout) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        List<Future<ResponseVO>> resultList = new ArrayList<Future<ResponseVO>>();

        //线程数
        int threadNumber = (ipSet.size()) / 2 == 0 ? 1 : (ipSet.size()) / 2;
        for (int i = 0; i < threadNumber; i++) {
            ScanPointIp scanPointIp = new ScanPointIp(ipSet, portSet, threadNumber, i, timeout);
            //threadPool.execute(scanPointIp);
            Future<ResponseVO> res = threadPool.submit(scanPointIp);
            resultList.add(res);
        }
        threadPool.shutdown();
        // 每秒中查看一次是否已经扫描结束
        return this.handleResult(threadPool,resultList);
    }

    /**
     * 处理返回结果
     * @param threadPool
     * @param resultList
     * @return
     */
    private ResponseVO handleResult(ExecutorService threadPool, List<Future<ResponseVO>> resultList) {
        // 每秒中查看一次是否已经扫描结束
        while (true) {
            if (threadPool.isTerminated()) {
                log.info("扫描IP段+端口段结束");
                List<Result> list = new ArrayList<>();
                for (Future<ResponseVO> fr : resultList) {
                    try {
                        ResponseVO vo = fr.get();
                        if (vo.getStatus().equals("1")) {
                            List<Result> results = (List<Result>) vo.getData();
                            list.addAll(results);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return ResponseVO.successInstance(list);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
