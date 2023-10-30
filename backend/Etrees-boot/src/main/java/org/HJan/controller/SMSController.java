package org.HJan.controller;

import com.aliyuncs.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Result;
import org.HJan.entity.User;
import org.HJan.utils.SMSUtils;
import org.HJan.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
@Slf4j
@RequestMapping("/sendMsg")

public class SMSController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送短信
     */
    @PostMapping
    public Result<String> sendMsg(@RequestBody User user) {
        System.out.println("前台传来的Phone为: "+user.getPhone());
        if (!StringUtils.isEmpty(user.getPhone())) {
            //生成随机的6位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            log.info("code={}", code);
            //调用阿里云提供的短信服务API完成发送短信
            SMSUtils.sendMessage("ETREES","SMS_461180011",user.getPhone(),code.toString());
            log.info("发送成功");
            //将手机号当做key,验证码当成value保存在redis中
            stringRedisTemplate.opsForValue().set("verifi:"+user.getPhone(),code.toString(),5, TimeUnit.MINUTES);
            return Result.success("手机验证码短信发送成功");
        }
        return Result.error("短信发送失败");
    }


}
