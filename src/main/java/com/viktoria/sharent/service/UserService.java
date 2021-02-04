package com.viktoria.sharent.service;

import com.viktoria.sharent.pojo.Role;
import com.viktoria.sharent.pojo.User;
import com.viktoria.sharent.repository.RoleRepository;
import com.viktoria.sharent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public User saveUser(User user, String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        user.setPhone(user.getPhone());
        Role userRole = roleRepository.findByRole(role);
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public void addAdmin(User admin) {
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        admin.setActive(true);
        admin.setRegistrationDate(LocalDateTime.now());
        admin.setPhone(admin.getPhone());
        Role userRole = roleRepository.findByRole("ADMIN");
        admin.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(admin);
    }

    public List<User> getAllAdmin() {
        Role userRole = roleRepository.findByRole("ADMIN");
        return userRepository.findAllByRoles(userRole);
    }

    @Transactional
    public boolean verifyUser(int userId){
        //find user than set true than save user
        User user = userRepository.findById(userId).get();
        user.setVerified(true);
        user =  userRepository.save(user);
        return user.getVerified();
    }

}
