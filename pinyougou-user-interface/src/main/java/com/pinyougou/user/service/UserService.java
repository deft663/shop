package com.pinyougou.user.service;

import com.pinyougou.pojo.TbUser;
import entity.Result;

public interface UserService {
    //注册
    public void add(TbUser user);
    /**
     * 生成短信验证码
     * @return
     */
    public void createSmsCode(String phone);
    /**
     * 判断短信验证码是否存在
     * @param phone
     * @return
     */
    public boolean  checkSmsCode(String phone,String code);

}
