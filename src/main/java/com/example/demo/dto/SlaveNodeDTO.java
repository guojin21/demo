package com.example.demo.dto;

public class SlaveNodeDTO {

    // 主机ip
    private String host;

    // 端口
    private String port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "SlaveNodeDTO{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
