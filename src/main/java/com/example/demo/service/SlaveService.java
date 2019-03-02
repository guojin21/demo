package com.example.demo.service;

import com.example.demo.dto.SlaveNodeDTO;
import com.example.demo.util.IPUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class SlaveService extends BaseService {

    @Value("${app.master.uri}")
    private String masterUrl;

    @Value("${server.port}")
    private String port;


    public void run() {

        SlaveNodeDTO slaveNodeDTO = new SlaveNodeDTO();
        slaveNodeDTO.setHost(IPUtil.getLocalHostLANAddress().getHostAddress());
        slaveNodeDTO.setPort(port);
        String param = new Gson().toJson(slaveNodeDTO);

        // 向主节点发送一条消息，告知从节点已上线，添加到主节点缓存中
        try {
            pushDataByREST(masterUrl, param, HttpMethod.POST);
        } catch (Exception e) {
            logger.error("向主节点发起注册从节点失败", e);
        }

    }
}
