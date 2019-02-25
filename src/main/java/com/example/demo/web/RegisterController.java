package com.example.demo.web;


import com.example.demo.dto.SlaveNodeDTO;
import com.example.demo.util.CommonCache;
import org.springframework.web.bind.annotation.*;

/**
 * 注册从节点信息到主节点缓存中
 */
@RestController
@RequestMapping("/register")
public class RegisterController {


    @GetMapping("/in")
    public String in(String host,String port) {
        SlaveNodeDTO slaveNodeDTO = new SlaveNodeDTO();
        slaveNodeDTO.setPort(port);
        slaveNodeDTO.setHost(host);
        boolean success = CommonCache.slaveNodeSet.add(slaveNodeDTO);
        return success ? "success" : "fail";
    }
}
