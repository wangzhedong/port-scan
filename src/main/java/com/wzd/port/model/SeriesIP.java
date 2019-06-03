package com.wzd.port.model;

import lombok.Data;

@Data
public class SeriesIP {

    private String prefixIp;
    private int[] startEndIpNum;

    public SeriesIP(String prefixIp, int[] startEndIpNum) {
        this.prefixIp = prefixIp;
        this.startEndIpNum = startEndIpNum;
    }
}
