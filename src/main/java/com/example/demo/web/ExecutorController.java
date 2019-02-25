package com.example.demo.web;


import com.example.demo.dto.MessageDTO;
import com.example.demo.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@RestController
@RequestMapping("/executor")
public class ExecutorController {

    private static Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Value("${server.port}")
    private String port;


    /**
     * 异步并使用线程池来处理主节点派发来的任务
     * @param dto
     * @return
     */
    @PostMapping("/run")
    public String run(MessageDTO dto) {
        logger.info("接收到任务{}，开始执行", dto);

        //固定线程数为10个
        Executors.newFixedThreadPool(10, new ThreadFactory() {

            //为线程起个名字，便于分析和定位
            String name = "slave_" + port;

            //线程计数器
            private int counter;

            @Override
            public Thread newThread(Runnable r) {
                counter++;
                return new Thread(r, name + "_Thread_" + counter);
            }
        }).execute(() -> {
            try {
                // 模拟该任务所需执行时间
                Thread.sleep(dto.getElapsedTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return "success";
    }

}
