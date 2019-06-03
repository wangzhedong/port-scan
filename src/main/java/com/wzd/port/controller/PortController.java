package com.wzd.port.controller;

import com.wzd.port.constant.TypeEnum;
import com.wzd.port.model.PortModel;
import com.wzd.port.model.ResponseVO;
import com.wzd.port.model.SeriesIP;
import com.wzd.port.scan.IpScan;
import com.wzd.port.scan.PortScan;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("port")
public class PortController {

    @PostMapping("largeScan")
    public ResponseVO largeScan(@RequestBody PortModel port){
        if(port == null ){
            return ResponseVO.errorInstance("数据不能为空！");
        }
        List<String> ips = port.getIps();
        List<String> ports = port.getPorts();
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
            String startPort = ports.get(0);
            String endPort = ports.get(1);
            IpScan ip = new IpScan();
            ip.scanLargeIpAndPorts(seriesIP.getPrefixIp(),seriesIP.getStartIPNum(),seriesIP.getEndIPNum());
        }else if(ipType.equals(TypeEnum.SERIES) && portType.equals(TypeEnum.POINT)){
            //ip段+指定端口
            if(ips.size() != 2){
                return ResponseVO.errorInstance("ip段格式不对");
            }
            SeriesIP seriesIP = handleSeriesIP(ips.get(0),ips.get(1));

        }else if(ipType.equals(TypeEnum.POINT) && portType.equals(TypeEnum.SERIES)){
            //指定ip+端口段
        }else if(ipType.equals(TypeEnum.POINT) && portType.equals(TypeEnum.POINT)){
            //指定ip+指定端口
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
        return new SeriesIP(prefixIp,startIPNum,endIPNum);
    }



    public static void main(String[] args){
        IpScan ip = new IpScan();
        ip.scanLargeIpAndPorts("127.0.0.",0,1);
        //port.scanLargePorts("192.0.0.1", 20, 30, 10,800);
    }

}
