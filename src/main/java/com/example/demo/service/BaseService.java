package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;

public class BaseService {



    protected static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    private static HttpHeaders headers = null;

    static {
        headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        headers.setAccept(Arrays.asList(new MediaType[]{new MediaType("application", "json", Charset.forName("UTF-8"))}));
    }

    @Autowired
    protected static RestTemplate restTemplate = new RestTemplate();



    /**
     * @describe: 使用RestTemplate推送数据
     * @date: 2018/8/14 18:13
     **/
    protected String pushDataByREST(String url, String params, HttpMethod method) throws Exception{
        HttpEntity<String> requestEntity = new HttpEntity<String>(params, headers);
        logger.info("调用参数:" + requestEntity.toString());
        ResponseEntity<String> exchange = restTemplate.exchange(url, method, requestEntity, String.class);
        String responseResult = exchange == null ? "" : exchange.getBody();
        if (null == responseResult) {
            responseResult = "";
        }
        logger.info("调用返回结果:" + responseResult);
        return responseResult;
    }

}
