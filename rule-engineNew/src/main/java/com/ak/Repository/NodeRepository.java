package com.ak.Repository;


import com.ak.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
}
