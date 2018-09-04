package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import util.PhoneFormatCheckUtils;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @RequestMapping("/add")
    public Result add(@RequestBody TbUser tbUser,String smscode){
        boolean checkSmsCode = userService.checkSmsCode(tbUser.getPhone(), smscode);
        if(checkSmsCode==false){
            return new Result(false, "验证码输入错误！");
        }

        try {
            userService.add(tbUser);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }
    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String phone){
        //判断手机号格式
        if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
            return new Result(false, "手机号格式不正确");
        }
        try {
            userService.createSmsCode(phone);//生成验证码
            return new Result(true, "验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "验证码发送失败");
        }
    }

}
