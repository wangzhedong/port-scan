package com.wzd.port.utils;

import com.wzd.port.model.ResponseVO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class NetWorkUtils {

    /**
     * 监测网络是否联通
     * @param ip
     * @param timeout
     * @return
     */
    public static boolean isPing(String ip,int timeout){
        boolean flag = false;
        try {
            flag = InetAddress.getByName(ip).isReachable(timeout);
        } catch (IOException e) {
            log.error(e.getMessage());
            flag = false;
        }
       return flag;
    }

    /**
     * 判断端口
     * @param startPort
     * @param endPort
     * @return
     */
    public static String checkPort(int startPort,int endPort){
        if(startPort >= endPort){
            String str = "开始端口不能大于或等于结束端口";
            return str;
        }

        if(startPort < 0 || endPort > 65535){
            String str = "端口不能小于0或者大于65535";
        }
        return "";
    }

}
