package com.ak.Service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Stack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ak.Entity.Node;
import com.ak.Repository.NodeRepository;


@Service
public class RuleEngineService {

    @Autowired
    private NodeRepository nodeRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(RuleEngineService.class);
    // Create AST from rule string
    @Transactional
    public Node createRule(String ruleString) {
        Stack<Node> stack = new Stack<>();
        String[] tokens = ruleString.split(" ");  // Split by spaces, assuming the format is like 'age > 30 AND department = Sales'

        for (String token : tokens) {
            if (token.equalsIgnoreCase("AND") || token.equalsIgnoreCase("OR")) {
                // If the token is an operator, pop two operands from the stack and create an operator node
                Node right = stack.pop();
                Node left = stack.pop();
                stack.push(new Node("operator", token, left, right));
            } else {
                // If it's an operand (e.g., 'age > 30'), push it onto the stack
                stack.push(new Node("operand", token));
            }
        }

        // Root of the AST will be the last item in the stack
        Node root = stack.pop();

        // Save the root node, which will also save the entire tree due to CascadeType.ALL
        return nodeRepository.save(root);
    }



    
    // Combine multiple rules into one AST
    public Node combineRules(Node[] rules) {
        if (rules.length == 0) return null;
        Node combinedRule = rules[0];

        for (int i = 1; i < rules.length; i++) {
            combinedRule = new Node("operator", combinedRule, rules[i]);
        }

        return nodeRepository.save(combinedRule); // Save combined AST
    }

    // Evaluate the rule
    public boolean evaluateRule(Node node, Map<String, Object> data) {
        if (node == null) return true;

        if (node.getType().equals("operand")) {
            String[] parts = node.getValue().split(" ");
            String attribute = parts[0];
            String operator = parts[1];
            String value = parts[2];
            return evaluateCondition(data.get(attribute), operator, value);
        }

        // Evaluate left and right operands recursively
        boolean leftEval = evaluateRule(node.getLeft(), data);
        boolean rightEval = evaluateRule(node.getRight(), data);

        // Combine the results based on the operator
        if (node.getType().equalsIgnoreCase("AND")) {
            return leftEval && rightEval;
        } else if (node.getType().equalsIgnoreCase("OR")) {
            return leftEval || rightEval;
        }

        return false;
    }


    private boolean evaluateCondition(Object attributeValue, String operator, String value) {
        if (attributeValue instanceof Integer) {
            int intVal = Integer.parseInt(value);
            int attr = (Integer) attributeValue;

            switch (operator) {
                case ">": return attr > intVal;
                case "<": return attr < intVal;
                case "=": return attr == intVal;
                default: return false;
            }
        }

        if (attributeValue instanceof String) {
            return attributeValue.equals(value);
        }

        return false;
    }
    public Node getRuleById(Long id) {
        Optional<Node> rule = nodeRepository.findById(id);
        if (rule.isPresent()) {
            return rule.get();
        } else {
            throw new RuntimeException("Rule not found with ID: " + id);
        }
      }
    
}
