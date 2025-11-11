package com.tushar.biztrack.features.AppUser;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tushar.biztrack.common.exception.UserAlreadyExistsException;
import com.tushar.biztrack.common.security.UserPrincipal;
import com.tushar.biztrack.features.auth.AuthRequest;

@Service
public class AppUserServiceImpl implements AppUserService {
    
    @Autowired
    private AppUserRepository userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public void register(AuthRequest authRequest) {
        AppUser user = userRepo.findByEmail(authRequest.getEmail());
        if(user != null) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }
        user = new AppUser();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        Role role = roleRepo.findByName("ROLE_ADMIN");
        user.setRoles(Set.of(role));
        userRepo.save(user);
    }

    @Override
    public UserPrincipal authenticate(AuthRequest authRequest) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal;
    }
    
}
