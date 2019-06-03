package com.wzd.port.model;

import lombok.Data;

import java.util.List;

@Data
public class PortModel {

    /**
     * ip段：192.168.0.1--192.168.0.255
     * 指定IP：[192.168.0.1,192.168.0.2,192.168.0.2]
     */
    private List<String> ips;

    /**
     * 端口段：0--65535
     * 指定端口：[0,1,2,3]
     */
    private List<String> ports;

    /**
     * ip类型
     */
    private String ipType;

    /**
     * 端口类型
     */
    private String portType;

}
