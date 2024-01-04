package com.example.userManagement.service;

import com.example.userManagement.model.Role;
import com.example.userManagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name){
        Role role = new Role();
        role.setName(name);

        roleRepository.save(role);
        return role;
    }

}
