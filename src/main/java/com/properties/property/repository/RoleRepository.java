package com.properties.property.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.properties.property.enums.enums;
import com.properties.property.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByRole(enums role);
}
