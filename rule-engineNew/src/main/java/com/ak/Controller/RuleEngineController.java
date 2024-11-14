package com.ak.Controller;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ak.Entity.Node;
import com.ak.Entity.RuleRequest;
import com.ak.Service.RuleEngineService;

@Controller  // Change to RestController for automatic @ResponseBody
@RequestMapping("/api/rules")
public class RuleEngineController {

    @Autowired
    private RuleEngineService ruleEngineService;
    
    private static final Logger logger = LoggerFactory.getLogger(RuleEngineService.class);
    @PostMapping("/create")
    public ResponseEntity<?> createRule(@RequestBody RuleRequest ruleRequest) {
        String ruleString = ruleRequest.getRuleString();
        logger.info("Received rule creation request: {}", ruleString);
        try {
            Node createdRule = ruleEngineService.createRule(ruleString);
            if (createdRule != null && createdRule.getId() != null) {
                logger.info("Rule created with ID: {}", createdRule.getId());
                return ResponseEntity.ok(createdRule);  // Return the Node with the ID
            } else {
                logger.error("Failed to create rule, ID is missing.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rule creation failed.");
            }
        } catch (Exception e) {
            logger.error("Error creating rule: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rule: " + e.getMessage());
        }
    }


    @PostMapping("/evaluate/{id}")
    public ResponseEntity<Boolean> evaluateRule(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        try {
            Node rule = ruleEngineService.getRuleById(id);
            boolean result = ruleEngineService.evaluateRule(rule, data);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error evaluating rule with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // Combine multiple rules (using Java Stream API)
    @PostMapping("/combine")
    public ResponseEntity<?> combineRules(@RequestBody Long[] ruleIds) {
        try {
            Node[] rules = Arrays.stream(ruleIds)
                    .map(ruleEngineService::getRuleById)
                    .toArray(Node[]::new);
            Node combinedRule = ruleEngineService.combineRules(rules);
            return ResponseEntity.ok(combinedRule);
        } catch (Exception e) {
            logger.error("Error combining rules: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error combining rules: " + e.getMessage());
        }
    }

    // Serve the "create_rule.html" page
    @GetMapping("/create-rule")
    public String createRulePage() {
        return "create_rule";  // Serves create_rule.html template
    }
 
    // Serve the "evaluate_rule.html" page
    @GetMapping("/evaluate-rule")
    public String evaluateRulePage() {
        return "evaluate_rule";  // Serves evaluate_rule.html template
    }
}
