package com.viktoria.sharent.controller;

import com.viktoria.sharent.pojo.User;
import com.viktoria.sharent.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AdminApiController {
    private final UserService userService;

    @PostMapping("/api/admin/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAdmin( @RequestBody User admin){
        userService.addAdmin(admin);
    }

    @GetMapping("/api/admins")
    public List<User> getAllAdmin(){
        return userService.getAllAdmin();
    }
}
