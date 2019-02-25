package com.example.demo.dto;

import java.util.List;

public class MessageDTO {

    // 批次id
    private String batchId;

    // 封装的多条任务
    private List<String> datas;

    // 模拟处理多条任务的耗时
    private Integer elapsedTime;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
