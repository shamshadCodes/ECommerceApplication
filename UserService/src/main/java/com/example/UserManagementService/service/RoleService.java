package com.example.UserManagementService.service;

import com.example.UserManagementService.model.Role;
import com.example.UserManagementService.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name){
        Role role = new Role();
        role.setName(name);

        roleRepository.save(role);
        return role;
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

}
