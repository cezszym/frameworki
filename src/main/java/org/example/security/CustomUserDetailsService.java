package org.example.security;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByNickOrEmail(nickOrEmail, nickOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with nick or email:" + nickOrEmail));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), getAuthorities());
    }

    private Collection< ? extends GrantedAuthority> getAuthorities(){
        var auth = new ArrayList<SimpleGrantedAuthority>();
        auth.add(new SimpleGrantedAuthority("USER"));
        return auth;
    }
}
