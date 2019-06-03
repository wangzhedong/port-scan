package com.wzd.port.model;

import lombok.Data;

@Data
public class SeriesIP {

    private String prefixIp;
    private int startIPNum;
    private int endIPNum;

    public SeriesIP(String prefixIp, int startIPNum, int endIPNum) {
        this.prefixIp = prefixIp;
        this.startIPNum = startIPNum;
        this.endIPNum = endIPNum;
    }
}
