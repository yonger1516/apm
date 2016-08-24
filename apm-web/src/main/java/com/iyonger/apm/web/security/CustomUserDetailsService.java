package com.iyonger.apm.web.security;

import com.iyonger.apm.web.service.UserService;
import org.ngrinder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/8/11 0011.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user=userService.getUserByName(userName);
        if (null!=user){
            return new SecuredUser(user,user.getAuthProviderClass());
        }
        throw new UsernameNotFoundException("user cannot found with name:"+userName);
    }
}
