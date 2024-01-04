package com.maharjanworks.cafe.repository;

import com.maharjanworks.cafe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {

   User  findByEmail(@Param("email")String email);
}
