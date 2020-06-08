package com.venues.lt.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.demo.util.JmsUtil;
import com.zhenzi.sms.ZhenziSmsClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/venues/code")
@Api(value = "验证码",description = "验证码")
public class CodeController {
    private static final long serialVersionUID = 1L;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //短信平台相关参数
    @Value("${sms.apiUrl}")
    private String apiUrl;
    @Value("${sms.appId}")
    private String appId;
    @Value("${sms.appSecret}")
    private String appSecret;

    @Autowired
    UserService userService;

    @Autowired
    private JmsUtil jmsUtil;

    @ResponseBody
    @GetMapping("/phone")
    @ApiOperation(value = "获取手机验证码", notes = "获取手机验证码")
    public boolean getCode(@RequestParam("phone") String phone, @RequestParam("userId") String userId){
        if (phone != null){
            try {
                JSONObject json = null;
                String code = String.valueOf(new Random().nextInt(999999));
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("number", phone);
                params.put("templateId", 593);
                String[] templateParams = new String[2];
                templateParams[0] = code;
                templateParams[1] = "5分钟";
                params.put("templateParams", templateParams);
                String result = client.send(params);
                json = JSONObject.parseObject(result);
                if (json.getIntValue("code")!=0){//发送短信失败
                    return  false;
                }
                //将验证码存到session中,同时存入创建时间
                //以json存放，这里使用的是阿里的fastjson
                json = new JSONObject();
                json.put("phone",phone);
                json.put("code",code);
                json.put("createTime",System.currentTimeMillis());
                stringRedisTemplate.opsForValue().set(userId + "-" + phone,json.toJSONString(), 7, TimeUnit.DAYS);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @PostMapping("/email")
    public String sendTemplateEmail(@RequestParam("email") String email, @RequestParam("userId") String userId) {
        //接收地址
        String toAddress = email;
        //标题
        String subject = "邮箱验证码";
        //邮件模板
        String templateName = "emailTemplate";
        Context context = new Context();
        String code = String.valueOf(new Random().nextInt(999999));
        context.setVariable("code", code);
        return jmsUtil.sendInlineMail(toAddress, subject, templateName, context);

    }

}
