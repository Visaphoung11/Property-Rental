package com.properties.property.seeder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.properties.property.enums.enums;
import com.properties.property.model.Role;
import com.properties.property.repository.RoleRepository;

import java.util.*;
@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }
    private void loadRoles() {
        enums[] roleNames = new enums[] { enums.ADMIN,
                enums.User };
        Map<enums, String> roleDescriptionMap = Map.of(
                enums.ADMIN, "Administrator role",
                enums.User, "user role");
        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = Optional.ofNullable(roleRepository.findByRole(roleName));
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();
                roleToCreate.setRole(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(roleToCreate);
            });
        });
    }
}
