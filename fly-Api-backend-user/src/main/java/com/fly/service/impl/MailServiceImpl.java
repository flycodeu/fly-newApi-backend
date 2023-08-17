package com.fly.service.impl;

import com.fly.annotation.AuthCheck;
import com.fly.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    private String from = "1785613048@qq.com";
    private String to = "1785613048@qq.com";
    private String subject = "测试";
    private String context = "<a href='https://www.baidu.com'>点击获取</a>";

    /**
     * 发送人
     * 接收人
     * 标题
     * 正文
     */
    @Override
    public void sendEmail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from + "(FlyCodeU)");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(context);
        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendMimeEmail() {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // 可以设置发送附件
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(to);
            // 发送链接
            helper.setText(context, true);
            // 发送附件
            File file = new File("D:\\fly\\project\\fly-newApi\\fly-newApi-backend\\fly-Api-backend-user\\src\\main\\java\\com\\fly\\service\\impl\\MailServiceImpl.java");
            helper.addAttachment(file.getName(), file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
