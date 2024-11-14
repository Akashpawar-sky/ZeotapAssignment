package com.ak;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ak.Entity.*;
import com.ak.Repository.*;
@SpringBootTest
public class NodeRepositoryTest {

    @Autowired
    private NodeRepository nodeRepository;

    @Test
    public void testNodeSave() {
        Node node = new Node("operand", "age > 30");
        Node savedNode = nodeRepository.save(node);

        assertNotNull(savedNode);
        assertNotNull(savedNode.getId());
        System.out.println("Saved node ID: " + savedNode.getId());
    }
}
