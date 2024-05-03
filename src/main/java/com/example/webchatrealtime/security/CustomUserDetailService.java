package com.example.webchatrealtime.security;

import com.example.webchatrealtime.entities.User;
import com.example.webchatrealtime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        CustomUserDetail customUserDetail = new CustomUserDetail();
        customUserDetail.setUser(user);
        return customUserDetail;
    }
}
