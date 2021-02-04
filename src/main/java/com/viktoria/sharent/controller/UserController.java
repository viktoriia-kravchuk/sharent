package com.viktoria.sharent.controller;

import com.viktoria.sharent.pojo.User;
import com.viktoria.sharent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController {
    UserRepository userRepository;

    @GetMapping("/user/profile")
    public ModelAndView profilePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        return modelAndView;
    }

    @GetMapping("/api/user/profile")
    @ResponseBody
    public User getUserProfile(Integer id){
        Optional<User> user =  userRepository.findById(id);
        return user.orElse(new User());
    }
}
