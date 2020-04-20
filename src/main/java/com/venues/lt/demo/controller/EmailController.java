package com.venues.lt.demo.controller;


import com.venues.lt.demo.util.JmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;

@RestController
@RequestMapping("/email")
@ApiIgnore
public class EmailController {
    @Autowired
    private JmsUtil jmsUtil;

    /**
     * 发送简单的邮件
     *
     * @return 发送结果
     */
    @PostMapping("/sendSimpleEmail")
    public String sendSimpleEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        // 接收地址
        message.setTo("1342436308@qq.com");
        // 标题
        message.setSubject("一封简单的邮件");
        // 内容
        message.setText("使用Spring Boot发送简单邮件。");
        return jmsUtil.sendSimpleEmail(message);
    }
    /**
     * 发送HTML格式内容的邮件
     * @return 发送结果
     */
    @PostMapping("/sendHtmlEmail")
    public String sendHtmlEmail() {
        // 接收地址
        String toAddress = "1342436308@qq.com";
        // 标题
        String subject = "一封HTML格式内容的邮件";
        // 带HTML格式的内容
        StringBuilder sb = new StringBuilder("<p style='color:#42b983'>我要发送一份HTML格式内容的邮件。</p>");
        return jmsUtil.sendHtmlEmail(toAddress,subject,sb.toString());
    }

    /**
     *发送带附件的邮件
     * @return 发送结果
     */
    @PostMapping("/sendAttachmentsMail")
    public String sendAttachmentsMail() {
        // 接收地址
        String toAddress = "1342436308@qq.com";
        // 标题
        String subject = "一封带附件的邮件";
        // 内容
        String text = "详情参见附件内容！";
        // 传入附件
        FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/file/xxxx.doc"));
        return jmsUtil.sendAttachmentsMail(toAddress,subject,text,file);
    }

    /**
     * 发送带静态资源的邮件
     * @return 发送结果
     */
    @PostMapping("/sendInlineMail")
    public String sendInlineMail() {
        // 接收地址
        String toAddress = "1342436308@qq.com";
        // 标题
        String subject = "一封带静态资源的邮件";
        String contentId = "img";
        // 内容
        String text = "<html><body>一份美图：<img src='cid:" + contentId + "'/></body></html>";
        // 传入附件
        FileSystemResource file = new FileSystemResource(new File("src/main/resources/image/test.jpg"));
        return jmsUtil.sendInlineMail(toAddress,subject,text,file,contentId);
    }

    /**
     * 发送模板邮件
     *
     * @param code 验证码
     * @return 发送结果
     */
    @ResponseBody
    @PostMapping("/sendTemplateEmail")
    public String sendTemplateEmail(String code) {
        //接收地址
        String toAddress = "1342436308@qq.com";
        //标题
        String subject = "发送一份模板邮件";
        //邮件模板
        String templateName = "emailTemplate";
        Context context = new Context();
        context.setVariable("code", code);
        return jmsUtil.sendInlineMail(toAddress, subject, templateName, context);

    }
}
