package com.pinyougou.shop.authentication;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

public class UserDetailServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbSeller user = sellerService.findOne(username);
        if(user==null){
            return null;
        }
        if(!"1".equals(user.getStatus())){
            return null;
        }
        //判断如果存在 需要判断状态是否是已经审核
        System.out.println("有这个用户而且状态值是1");

        User role_seller = new User(username, user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_SELLER"));
        return role_seller;
    }
}
