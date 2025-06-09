package com.focus.springboottest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件发送工具类
 */
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param text    内容
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("1106288595@qq.com");

        javaMailSender.send(message);
    }

    /**
     * 发送 HTML 格式邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param html    HTML 内容
     * @throws MessagingException
     */
    public void sendHtmlEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // 第二个参数为 true 表示支持 HTML
        message.setFrom("1106288595@qq.com");
        javaMailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to           收件人
     * @param subject      主题
     * @param text         文本内容
     * @param attachment   附件文件路径
     * @throws MessagingException
     */
    public void sendEmailWithAttachment(String to, String subject, String text, String attachment) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.setFrom("1106288595@qq.com");

        FileSystemResource file = new FileSystemResource(new File(attachment));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);

        javaMailSender.send(message);
    }

}
