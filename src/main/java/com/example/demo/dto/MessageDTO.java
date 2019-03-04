package com.example.demo.dto;

import java.util.List;

public class MessageDTO {

    // 批次id
    private Integer batchId;

    // 封装的多条任务
    private List<String> datas;

    // 模拟处理多条任务的耗时
    private Integer elapsedTime;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
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

    @Override
    public String toString() {
        return "MessageDTO{" +
                "batchId='" + batchId + '\'' +
                ", datas=" + datas +
                ", elapsedTime=" + elapsedTime +
                '}';
    }
}
