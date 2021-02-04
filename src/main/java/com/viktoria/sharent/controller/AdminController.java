package com.viktoria.sharent.controller;

import com.viktoria.sharent.pojo.User;
import com.viktoria.sharent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    private final UserRepository userRepository;

    @GetMapping("/admin")
    public ModelAndView adminHome(){
        ModelAndView modelAndView  = new ModelAndView();
        modelAndView.setViewName("admin-home");
        return modelAndView;
    }

    @GetMapping("/admin/all-users")
    public ModelAndView allUsers(){
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userRepository.findAll();
        modelAndView.setViewName("admin-all-users");
        modelAndView.addObject("users",users);
        return modelAndView;
    }

    @GetMapping("/admin/edit-user/{id}")
    public ModelAndView editUser(@PathVariable Integer id){
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findById(id).get();
        modelAndView.addObject("user",user);
        modelAndView.setViewName("admin-edit-user");
        return modelAndView;
    }

    @PostMapping("/admin/edit-user")
    public ModelAndView updateUser(User user){
        ModelAndView modelAndView = new ModelAndView();
        User updatedUser = userRepository.save(user);
        modelAndView.addObject("user",updatedUser);
        modelAndView.setViewName("admin-edit-user");
        return modelAndView;
    }

    @GetMapping("/admin/all-admin")
    public ModelAndView allAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-all-admin");
        return modelAndView;
    }

}
