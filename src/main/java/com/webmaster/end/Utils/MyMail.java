package com.webmaster.end.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MyMail {

    @Value("${spring.mail.username}")
    private String myEmail;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Bean
    public MyMail createMymail(){
        return new MyMail();
    }

    /**
     * 发送简单邮件
     * @param targetEmail 目标邮箱
     * @param subject 邮件的主题
     * @param text 邮件的主要内容
     * @return 返回是否发送成功
     */
    public boolean sendSimpleEmail(String targetEmail,String subject,String text){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(myEmail);
        simpleMailMessage.setTo(targetEmail);
        simpleMailMessage.setText(text);
        simpleMailMessage.setSubject(subject);
        Properties properties = new Properties();
        //设置超时时间
        properties.setProperty("mail.smtp.timeout", "1000");
        //设置通过ssl协议使用465端口发送、使用默认端口（25）时下面三行不需要
        properties.setProperty("mail.smtp.auth", "true");//开启认证
        properties.setProperty("mail.smtp.socketFactory.port", "465");//设置ssl端口
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailSender.setJavaMailProperties(properties);
        try {
            mailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送验证码
     * @param targetEmail 目标邮件
     * @return 返回一个Map包含验证码和发送时间
     */
    public Map<String,Object> sendVerificationCode(String targetEmail){
        String verificationCode = RandomUtil.getRandomNumber(6);
        String text="你的验证码为："+verificationCode+",请不要告诉他人，该验证码3分钟有效";
        if(this.sendSimpleEmail(targetEmail,"图书管理系统验证码",text))
            return new HashMap<String,Object>(){
                {put("veriCode",verificationCode);put("sendTime",MyDateUtil.getCurrentTime());}
            };
        else
            return null;
    }
}
