package com.dream.server;

import com.dream.sms.ServerTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api("testService")
@RequestMapping
@RestController
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private ServerTest serverTest;

    @ApiOperation("返回json数据")
    @GetMapping("test")
    @ResponseBody
    public String updateVehicleHealthState(String phone) {
        logger.info("进来了");
        serverTest.sendMessage(phone);
        return "test";
    }



}
