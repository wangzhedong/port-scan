package com.wzd.port.controller;

import com.wzd.port.constant.TypeEnum;
import com.wzd.port.model.PortModel;
import com.wzd.port.model.ResponseVO;
import com.wzd.port.model.SeriesIP;
import com.wzd.port.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("port")
public class PortController {

    private final int timeout = 800;

    @Autowired
    private ScanService scanService;

    @PostMapping("scan")
    public ResponseVO scan(@RequestBody PortModel port){
        if(port == null ){
            return ResponseVO.errorInstance("数据不能为空！");
        }
        List<String> ips = port.getIps();
        List<Integer> ports = port.getPorts();
        String ipType = port.getIpType();
        String portType = port.getPortType();
        if(ips == null || ips.isEmpty()){
            return ResponseVO.errorInstance("ip不能为空！");
        }
        if(ports == null || ports.isEmpty()){
            return ResponseVO.errorInstance("端口不能为空！");
        }
        if(StringUtils.isEmpty(ipType)){
            return ResponseVO.errorInstance("ip类型不能为空！");
        }
        if(StringUtils.isEmpty(portType)){
            return ResponseVO.errorInstance("端口类型不能为空！");
        }

        if(ipType.equals(TypeEnum.SERIES) && portType.equals(TypeEnum.SERIES)){
            //ip段+端口段
            if(ips.size() != 2){
                return ResponseVO.errorInstance("ip段格式不对");
            }
            if(ports.size() != 2){
                return ResponseVO.errorInstance("端口段格式不对");
            }
            SeriesIP seriesIP = handleSeriesIP(ips.get(0),ips.get(1));
            int[] startEndPort = {ports.get(0),ports.get(1)};
            scanService.scanSeriesIpPorts(seriesIP.getPrefixIp(),seriesIP.getStartEndIpNum(),startEndPort,timeout);
        }else if(ipType.equals(TypeEnum.SERIES) && portType.equals(TypeEnum.POINT)){
            //ip段+指定端口
            if(ips.size() != 2){
                return ResponseVO.errorInstance("ip段格式不对");
            }
            SeriesIP seriesIP = handleSeriesIP(ips.get(0),ips.get(1));
            Set<Integer> portSet = new HashSet<>();
            ports.forEach(item -> portSet.add(item));
            scanService.scanSeriesIpAndPointPorts(seriesIP.getPrefixIp(),seriesIP.getStartEndIpNum(), portSet,  timeout);
        }else if(ipType.equals(TypeEnum.POINT) && portType.equals(TypeEnum.SERIES)){
            //指定ip+端口段
            Set<String> ipSet = new HashSet<>();
            int[] startEndPort = {ports.get(0),ports.get(1)};
            ips.forEach(item -> ipSet.add(item));
            scanService.scanPointIpAndSeriesPorts(ipSet,startEndPort, timeout);

        }else if(ipType.equals(TypeEnum.POINT) && portType.equals(TypeEnum.POINT)){
            //指定ip+指定端口
            Set<String> ipSet = new HashSet<>();
            Set<Integer> portSet = new HashSet<>();
            ips.forEach(item -> ipSet.add(item));
            ports.forEach(item -> portSet.add(item));
            scanService.scanPointIpPorts( ipSet, portSet,timeout);
        }

        return ResponseVO.successInstance();
    }

    /**
     * 处理ip段的参数
     * @param startIP
     * @param endIP
     * @return
     */
    private SeriesIP handleSeriesIP(String startIP,String endIP){
        String prefixIp = startIP.substring(0,startIP.lastIndexOf("."));
        int startIPNum = Integer.valueOf(startIP.substring(startIP.lastIndexOf(".")+1,startIP.length()));
        int endIPNum = Integer.valueOf(endIP.substring(endIP.lastIndexOf(".")+1,endIP.length()));
        int[] startEndIpNum = {startIPNum,endIPNum};
        return new SeriesIP(prefixIp,startEndIpNum);
    }



    public static void main(String[] args){

    }

}
