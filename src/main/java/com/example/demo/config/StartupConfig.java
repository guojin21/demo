package com.example.demo.config;


import com.example.demo.service.MasterService;
import com.example.demo.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class StartupConfig {


    @Value("${app.location.role}")
    private String role;

    @Autowired
    private MasterService masterService;

    @Autowired
    private SlaveService slaveService;

    @PostConstruct
    public void judge() {
        if (role.equals("master")) {
            masterService.run();
        }
        if (role.equals("slave")) {
            slaveService.run();
        }
    }
}
