package com.wzd.port.controller;

import com.wzd.port.constant.TypeEnum;
import com.wzd.port.model.PortModel;
import com.wzd.port.model.ResponseVO;
import com.wzd.port.model.SeriesIP;
import com.wzd.port.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("scan")
public class PortController {

    private final int timeout = 800;

    @Autowired
    private ScanService scanService;

    @GetMapping("test")
    public String test(){
        return "哈哈哈";
    }

    @PostMapping("start")
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

        if(ipType.equals(TypeEnum.SERIES.getType()) && portType.equals(TypeEnum.SERIES.getType())){
            //ip段+端口段
            if(ips.size() != 2){
                return ResponseVO.errorInstance("ip段格式不对");
            }
            if(ports.size() != 2){
                return ResponseVO.errorInstance("端口段格式不对");
            }
            SeriesIP seriesIP = handleSeriesIP(ips.get(0),ips.get(1));
            int[] startEndPort = {ports.get(0),ports.get(1)};
            return scanService.scanSeriesIpPorts(seriesIP.getPrefixIp(),seriesIP.getStartEndIpNum(),startEndPort,timeout);
        }else if(ipType.equals(TypeEnum.SERIES.getType()) && portType.equals(TypeEnum.POINT.getType())){
            //ip段+指定端口
            if(ips.size() != 2){
                return ResponseVO.errorInstance("ip段格式不对");
            }
            SeriesIP seriesIP = handleSeriesIP(ips.get(0),ips.get(1));
            Set<Integer> portSet = new HashSet<>();
            ports.forEach(item -> portSet.add(item));
            return scanService.scanSeriesIpAndPointPorts(seriesIP.getPrefixIp(),seriesIP.getStartEndIpNum(), portSet,  timeout);
        }else if(ipType.equals(TypeEnum.POINT.getType()) && portType.equals(TypeEnum.SERIES.getType())){
            //指定ip+端口段
            Set<String> ipSet = new HashSet<>();
            int[] startEndPort = {ports.get(0),ports.get(1)};
            ips.forEach(item -> ipSet.add(item));
            return scanService.scanPointIpAndSeriesPorts(ipSet,startEndPort, timeout);

        }else if(ipType.equals(TypeEnum.POINT.getType()) && portType.equals(TypeEnum.POINT.getType())){
            //指定ip+指定端口
            Set<String> ipSet = new HashSet<>();
            Set<Integer> portSet = new HashSet<>();
            ips.forEach(item -> ipSet.add(item));
            ports.forEach(item -> portSet.add(item));
            return scanService.scanPointIpPorts( ipSet, portSet,timeout);
        }else{
            return ResponseVO.errorInstance("无ip或者端口类型匹配");
        }
    }

    /**
     * 处理ip段的参数
     * @param startIP
     * @param endIP
     * @return
     */
    private SeriesIP handleSeriesIP(String startIP,String endIP){
        String prefixIp = startIP.substring(0,startIP.lastIndexOf(".")+1);
        int startIPNum = Integer.valueOf(startIP.substring(startIP.lastIndexOf(".")+1,startIP.length()));
        int endIPNum = Integer.valueOf(endIP.substring(endIP.lastIndexOf(".")+1,endIP.length()));
        int[] startEndIpNum = {startIPNum,endIPNum};
        return new SeriesIP(prefixIp,startEndIpNum);
    }



    /*public static void main(String[] args){

    }*/

}
