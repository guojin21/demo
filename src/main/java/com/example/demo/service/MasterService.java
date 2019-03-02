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

    public static Set<MessageDTO> dataSource = new HashSet<MessageDTO>();


    /**
     * 构造100条任务
     */
    void init() {

        for (int i = 0; i < 100; i++) {
            MessageDTO dto = new MessageDTO();
            List<String> datas = new ArrayList<>();
            dto.setBatchId("task" + i);
            int value = (int) (Math.random() * 3) + 1;
            for (int j = 0; j < value; j++) {
                datas.add("data content " + j);
            }
            dto.setDatas(datas);
            dto.setElapsedTime(value * 1000);
            dataSource.add(dto);
        }

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


//        // 自旋等待，直到从节点上线
//        while(CommonCache.slaveNodeSet.size() == 0){
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            continue;
//        }


        // 对接上游数据源，此处为了演示，模拟一个定时任务来发送数据
        ExecutorService timer = Executors.newScheduledThreadPool(10);
        ((ScheduledExecutorService) timer).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                // 获取需要发送任务的从节点信息
                int slaveNums = CommonCache.slaveNodeSet.size();
                for (MessageDTO dto : dataSource) {
                    int shard = dto.getBatchId().hashCode() % slaveNums;
                    SlaveNodeDTO slaveNodeDTO = CommonCache.slaveNodeSet.get(shard);

                    String url = "http://" + slaveNodeDTO.getHost() + ":" + slaveNodeDTO.getPort() + "/executor/run";
                    String param = new Gson().toJson(dto);
                    for (int count = 3; count > 0; count--) {
                        if (count == 0) {
                            // TODO 将该任务存储到数据库或者文件中
                            logger.info("该任务[{}]派发超过3次依然失败，存储到文件或数据库：", param);
                        }
                        try {
                            String result = pushDataByREST(url, param, HttpMethod.POST);
                            logger.info("该任务[{}]派发成功", param);
                            if (null != result && "".equals(result)) {
                                break;
                            } else {
                                continue;
                            }
                        } catch (Exception e) {
                            logger.error("派发任务异常", e.getMessage());
                            continue;
                        }

                    }
                }
            }
        }, 50, 20, TimeUnit.SECONDS);
    }
}
