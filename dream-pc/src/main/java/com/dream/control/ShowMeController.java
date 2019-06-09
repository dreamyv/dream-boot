package com.dream.control;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class ShowMeController {

    private static final Logger logger = LoggerFactory.getLogger(ShowMeController.class);

    @ApiOperation("跳转自我介绍页面")
    @RequestMapping(value = "/")
     public String index(){
       return "showme";
     }
}
