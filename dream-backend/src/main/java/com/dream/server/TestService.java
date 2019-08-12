package com.dream.server;

import com.dream.sms.ServerTest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

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


    //https://blog.csdn.net/m0_37125796/article/details/79069053


    final Semaphore permit = new Semaphore(10, true);// 允许最大并发数为10


    static AtomicInteger i = new AtomicInteger(0);
    static int is = 0;
    @ApiOperation("调用业务数据")
    @GetMapping("test1")
    @ResponseBody
    public String test1(int t) {
        // 得到当前秒
        long currentSeconds = System.currentTimeMillis() / 1000;
        try {
            permit.acquire();
            // 业务处理
            i.getAndIncrement();
            is ++;
            Thread.sleep(1000L);
            return currentSeconds+" i="+i.get()+" is="+is+" t="+t;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return currentSeconds+"访问速率过快i="+i+";t="+t;
        }finally {
            permit.release();
        }
    }

    public static void main(String[] args) {
        for (int i = 0 ;i<100;i++){
            final int t = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(new TestService().test1(t));
                }
            }).start();
        }
    }

}
