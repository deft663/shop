package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private RedisTemplate<String , Object> redisTemplate;

    /**
     * 增加
     */
    @Override
    public  void add(TbUser user) {
        user.setCreated(new Date());//创建日期
        user.setUpdated(new Date());//修改日期
        String password = DigestUtils.md5Hex(user.getPassword());//对密码加密
        user.setPassword(password);
        userMapper.insert(user);
    }

    /**
     * 生成短信验证码
     */
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsDestination;

    @Value("${template_code}")
    private String template_code;

    @Value("${sign_name}")
    private String sign_name;


    /**
     * 生成短信验证码
     */
    public void createSmsCode(final String phone){
        System.out.println(sign_name);
        System.out.println(template_code);
        //生成6位随机数
        final String code =  (long) (Math.random()*1000000)+"";
        System.out.println("验证码："+code);
        //存入缓存
        redisTemplate.boundHashOps("smscode").put(phone, code);
        //发送到activeMQ
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("mobile", phone);//手机号
                mapMessage.setString("template_code", template_code);//模板编号
                mapMessage.setString("sign_name", sign_name);//签名
                Map m=new HashMap<>();
                m.put("code", code);
                mapMessage.setString("param", JSON.toJSONString(m));//参数
                return mapMessage;
            }
        });
    }


    /**
     * 判断短信验证码是否存在
     * @param phone
     * @return
     */
    /**
     * 判断验证码是否正确
     */
    public boolean  checkSmsCode(String phone,String code){
        //得到缓存中存储的验证码
        String sysCode = (String) redisTemplate.boundHashOps("smscode").get(phone);
        if(sysCode==null){
            return false;
        }
        if(!sysCode.equals(code)){
            return false;
        }
        return true;
    }




}
