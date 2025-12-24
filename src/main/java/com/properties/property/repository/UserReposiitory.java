package com.properties.property.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.properties.property.model.UserModel;



@Repository
public interface UserReposiitory extends JpaRepository<UserModel, Integer> {
    UserModel findByEmail(String email);

}
