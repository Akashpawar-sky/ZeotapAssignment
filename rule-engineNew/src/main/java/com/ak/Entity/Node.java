package com.ak.Entity;


import jakarta.persistence.*;

@Entity
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Ensure ID is auto-generated
    private Long id;

    private String type;  // "operator" or "operand"
    private String value; // Operand value, if applicable

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "left_id")
    private Node left;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "right_id")
    private Node right;

    public Node(String string, String token, Node left2, Node right2) {
    	this.type = token;
        this.left = left2;
        this.right = right2;
    }

    // Constructor for operand
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Constructor for operator (AND/OR)
    public Node(String type, Node left, Node right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    // Getters and setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
