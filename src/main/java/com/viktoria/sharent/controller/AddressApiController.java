package com.viktoria.sharent.controller;

import com.viktoria.sharent.pojo.Address;
import com.viktoria.sharent.pojo.User;
import com.viktoria.sharent.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor

public class AddressApiController {
    private final AddressService addressService;
    @PostMapping("/api/address/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAddress(@RequestBody Address address, User userId){
        addressService.addAddress(address, userId);
    }

}
