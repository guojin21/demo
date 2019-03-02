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


    @PostMapping("/in")
    public String in(@RequestBody SlaveNodeDTO slaveNodeDTO) {
        boolean success = CommonCache.slaveNodeSet.add(slaveNodeDTO);
        return success ? "success" : "fail";
    }
}
