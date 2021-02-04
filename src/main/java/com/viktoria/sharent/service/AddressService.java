package com.viktoria.sharent.service;

import com.viktoria.sharent.pojo.Address;
import com.viktoria.sharent.pojo.User;
import com.viktoria.sharent.repository.AddressRepository;
import com.viktoria.sharent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public void addAddress(Address address, User user){
        address.setUser(userRepository.getOne(user.getId()));
        addressRepository.save(address);


    }


}
