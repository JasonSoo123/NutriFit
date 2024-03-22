package com.gtg.gtg.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    List<Users> findByUsernameAndPassword(String username, String password);
    List<Users> findByUsertype(int usertype);
    List<Users> findByUsername(String username);
    void deleteByUsername(String username);
}