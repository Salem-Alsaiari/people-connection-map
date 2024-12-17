package com.sort.people_connection_map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class PersonNode {
    private String name;
    private String description;
    private double x, y; // Coordinates for the node
    private Circle circle;
    private Text label;

    public PersonNode(String name, String description, double x, double y) {
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;

        // Create a circle for the node
        this.circle = new Circle(x, y, 30, Color.LIGHTBLUE);
        this.circle.setStroke(Color.BLACK);

        // Create a label for the node
        this.label = new Text(x - 20, y + 5, name); // Center text roughly
    }

    public Circle getCircle() {
        return circle;
    }

    public Text getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
