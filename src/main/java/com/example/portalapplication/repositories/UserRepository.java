package com.example.portalapplication.repositories;

import com.example.portalapplication.models.User;
import com.example.portalapplication.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    // search by first or last name
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String first, String last);
    //filter by role
    List<User> findByRole(Role role);
    // combine search and filter
    List<User> findByRoleAndFirstNameContainingIgnoreCaseOrRoleAndLastNameContainingIgnoreCase
            (Role role1, String first, Role role2, String last);
    List<User> findByIdIn(List<Integer> ids);
}
