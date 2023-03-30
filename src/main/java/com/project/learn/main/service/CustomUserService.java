package com.project.learn.main.service;

import com.project.learn.main.entity.UserEntity;
import com.project.learn.main.repository.UserEntityRepo;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserEntityRepo userEntityRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserEntity> userEntity = userEntityRepo.findByUserName(username);
        if(!userEntity.isEmpty()){
            return new User(userEntity.get(0).getUserName(),userEntity.get(0).getPassword(),
                    get(userEntity.get(0)));
        }else{
            throw new UsernameNotFoundException("Username is not valid");
        }

    }

    private Set get(UserEntity userEntity){
        Set authorities = new HashSet();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRoleType().getValue()));
        return authorities;

    }
}
