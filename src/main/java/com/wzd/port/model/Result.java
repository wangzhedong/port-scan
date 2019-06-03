package com.wzd.port.model;

import lombok.Data;

@Data
public class Result {

    private String ip;

    private int port;

    private String status;

    public Result(String ip, int port, String status) {
        this.ip = ip;
        this.port = port;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Result{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", status='" + status + '\'' +
                '}';
    }
}
