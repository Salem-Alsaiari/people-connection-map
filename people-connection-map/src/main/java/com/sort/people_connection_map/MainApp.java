package com.sort.people_connection_map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    private Pane canvas;
    private List<PersonNode> nodes;

    @Override
    public void start(Stage primaryStage) {
        // Initialize layout and data
        canvas = new Pane();
        nodes = new ArrayList<>();

        // Add some nodes and connections
        createSampleData();

        // Draw nodes and connections
        drawConnections();
        drawNodes();

        // Set up the scene
        Scene scene = new Scene(canvas, 800, 600, Color.WHITE);
        primaryStage.setTitle("People Connection Map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Example data: Create sample nodes and connections
    private void createSampleData() {
        PersonNode alice = new PersonNode("Alice", "Friend", 200, 150);
        PersonNode bob = new PersonNode("Bob", "Colleague", 400, 300);
        PersonNode charlie = new PersonNode("Charlie", "Classmate", 600, 150);

        nodes.add(alice);
        nodes.add(bob);
        nodes.add(charlie);

        // Add connections (drawn later)
        addConnection(alice, bob, "Works with");
        addConnection(bob, charlie, "Studies with");
    }

    // Add a connection as a line
    private void addConnection(PersonNode node1, PersonNode node2, String relationship) {
        Line connection = new Line(node1.getX(), node1.getY(), node2.getX(), node2.getY());
        connection.setStroke(Color.GRAY);
        connection.setStrokeWidth(2);

        // Optional: Add a label for the relationship
        javafx.scene.text.Text relationshipLabel = new javafx.scene.text.Text(
            (node1.getX() + node2.getX()) / 2, 
            (node1.getY() + node2.getY()) / 2, 
            relationship
        );
        relationshipLabel.setFill(Color.BLACK);

        // Add to canvas
        canvas.getChildren().addAll(connection, relationshipLabel);
    }

    // Draw all nodes on the canvas
    private void drawNodes() {
        for (PersonNode node : nodes) {
            canvas.getChildren().addAll(node.getCircle(), node.getLabel());
        }
    }

    // Draw connections between nodes
    private void drawConnections() {
        // Connections are already drawn in `addConnection`
    }

    public static void main(String[] args) {
        launch(args);
    }
}
