package com.example.demo.service;


import com.example.demo.dto.MessageDTO;
import com.example.demo.dto.SlaveNodeDTO;
import com.example.demo.util.CommonCache;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MasterService extends BaseService {

    @Autowired
    private RestTemplate restTemplate;

    public static List<MessageDTO> dataSource = new ArrayList<>();


    /**
     * 构造100条任务
     */
    void init() {

        for (int i = 0; i < 100; i++) {
            MessageDTO dto = new MessageDTO();
            List<String> datas = new ArrayList<>();
            dto.setBatchId(i + 1);
            int value = (int) (Math.random() * 3) + 1;
            for (int j = 0; j < value; j++) {
                datas.add("data content " + j);
            }
            dto.setDatas(datas);
            dto.setElapsedTime(value * 1000);
            dataSource.add(dto);

        }
        logger.info("datasource.size=" + dataSource.size());

    }


    /**
     * 1.对接上游数据源
     * 2.对关联数据进行打包并设置标签（标签为关联数据中相同字段hash值）
     * 3.根据hash值取余，确定向哪个从节点发送任务
     * 4.接收到响应结果后，才会继续发送下一个任务。超过重试次数，则将从节点缓存中剔除，确保发送的任务能够都被执行
     */
    public void run() {

        // 初始化任务数据
        init();

        // 对接上游数据源，此处为了演示，模拟一个定时任务来发送数据
        ExecutorService timer = Executors.newScheduledThreadPool(10);
        ((ScheduledExecutorService) timer).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                // 获取需要发送任务的从节点信息
                int slaveNums = CommonCache.slaveNodeSet.size();
                logger.info("当前从节点个数为：{}", slaveNums);
                if (slaveNums == 0) return;
                for (MessageDTO dto : dataSource) {
                    int shard = dto.getBatchId().hashCode() % slaveNums;
                    SlaveNodeDTO slaveNodeDTO = CommonCache.slaveNodeSet.get(shard);

                    String url = "http://" + slaveNodeDTO.getHost() + ":" + slaveNodeDTO.getPort() + "/executor/run";
                    String param = new Gson().toJson(dto);
                    String result = null;
                    try {
                        result = pushDataByREST(url, param, HttpMethod.POST);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    logger.info("该任务id[{}]派发到从节点[{}]成功", dto.getBatchId(), slaveNodeDTO);
                    if (null != result && "".equals(result)) {
                        break;
                    } else {
                        continue;
                    }

                }
            }
        }, 50, 20, TimeUnit.SECONDS);
    }
}
