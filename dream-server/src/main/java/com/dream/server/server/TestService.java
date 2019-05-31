package com.dream.server.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Api("testService")
@RestController
@RequestMapping("testService")
public class TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @ApiOperation("test")
    @GetMapping("test")
    public String updateVehicleHealthState() {
        logger.info("进来了");
        return "test";
    }
}
