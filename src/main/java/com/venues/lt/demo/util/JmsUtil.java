package com.venues.lt.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

/**
 * @Description: 发送邮件工具类
 * @Author: RabbitsInTheGrass_xj
 * @Date: 2019/10/2 09:45
 */
@Component
public class JmsUtil {

    private static final Logger logger = LoggerFactory.getLogger(JmsUtil.class);

    @Autowired
    private JavaMailSender jms;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public String sendSimpleEmail(SimpleMailMessage message) {
        try {
            message.setFrom(from);
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "发送失败";
        }
    }

    public String sendHtmlEmail(String toAddress, String subject, String text) {
        try {
            MimeMessage message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            //true表示发送HTML格式邮件
            helper.setText(text, true);
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "发送失败";
        }
    }
    public String sendAttachmentsMail(String toAddress, String subject, String text, FileSystemResource file) {
        try {
            MimeMessage message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment(file.getFilename(), file);
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "发送失败";
        }
    }

    public String sendInlineMail(String toAddress, String subject, String text, FileSystemResource file, String contentId) {
        MimeMessage message;
        try {
            message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(text,true);
            //contentId 必须与图片标签里cid后的名称相对应，例如：img
            helper.addInline(contentId, file);
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "发送失败";
        }
    }

    public String sendInlineMail(String toAddress, String subject, String templateName, Context context) {
        try {
            MimeMessage message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            // 接收地址
            helper.setTo(toAddress);
            // 标题
            helper.setSubject(subject);
            // 处理邮件模板
            String template = templateEngine.process(templateName, context);
            helper.setText(template, true);
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "发送失败";
        }
    }
}
