package com.ak;

import com.ak.Entity.Node;
import com.ak.Repository.NodeRepository;
import com.ak.Service.RuleEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest  // This tells Spring Boot to load the application context
class RuleEngineServiceTest {

    @Autowired
    private RuleEngineService ruleEngineService;  // Service under test

    @MockBean
    private NodeRepository nodeRepository;  // Mock the repository

    @BeforeEach
    void setUp() {
        // Mock the behavior of nodeRepository.save() to return the node with an ID
        Node mockNode = new Node("operator", new Node("operand", "age > 30"), new Node("operand", "department = 'Sales'"));
        mockNode.setId(1L);  // Set mock ID
        Mockito.when(nodeRepository.save(any(Node.class))).thenReturn(mockNode);
        
        // Mock findById
        Mockito.when(nodeRepository.findById(1L)).thenReturn(Optional.of(mockNode));
    }

    @Test
    public void testCreateRule() {
        String ruleString = "age > 30 AND department = 'Sales'";
        Node createdNode = ruleEngineService.createRule(ruleString);

        assertNotNull(createdNode);  // Check that the node is not null
        assertEquals(1L, createdNode.getId());  // Ensure the node has the expected ID
        assertEquals("operator", createdNode.getType());  // Root node type should be "operator"
    }

    @Test
    public void testCombineRules() {
        String rule1 = "age > 30 AND department = 'Sales'";
        String rule2 = "salary > 50000";

        Node ast1 = ruleEngineService.createRule(rule1);
        Node ast2 = ruleEngineService.createRule(rule2);

        Node combinedRule = ruleEngineService.combineRules(new Node[]{ast1, ast2});

        assertNotNull(combinedRule);  // Ensure combined AST is not null
        assertEquals("operator", combinedRule.getType());  // Root node should be "operator"
    }

    @Test
    public void testEvaluateRule() {
        String ruleString = "age > 30 AND department = 'Sales'";
        Node rule = ruleEngineService.createRule(ruleString);

        // Test data that should pass the rule
        Map<String, Object> data = Map.of("age", 35, "department", "Sales");
        boolean result = ruleEngineService.evaluateRule(rule, data);
        assertTrue(result);  // The rule should evaluate to true

        // Test data that should fail the rule
        Map<String, Object> dataFail = Map.of("age", 25, "department", "Sales");
        boolean resultFail = ruleEngineService.evaluateRule(rule, dataFail);
        assertFalse(resultFail);  // The rule should evaluate to false
    }
}
