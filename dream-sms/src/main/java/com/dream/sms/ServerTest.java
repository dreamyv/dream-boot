package com.dream.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ServerTest {

    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);

    @Autowired
    JavaMailSender jms;

    public boolean sendMessage(String str){
        logger.info("发送邮箱到"+str);
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom("hnlyjmy@163.com");
        //接收者
        mainMessage.setTo("809391761@qq.com");
        //发送的标题
        mainMessage.setSubject("嗨喽");
        //发送的内容
        mainMessage.setText("hello world ! 这个是中文!");
        jms.send(mainMessage);
        return true;
    }
}
