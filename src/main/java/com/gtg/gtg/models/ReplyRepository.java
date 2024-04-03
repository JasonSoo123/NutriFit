package com.gtg.gtg.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
    
    List<Reply> findByPostTitleAndPostUsername(String postTitle, String postUsername);
}
