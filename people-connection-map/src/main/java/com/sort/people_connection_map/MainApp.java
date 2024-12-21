package com.sort.people_connection_map;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainApp extends Application {
    private Pane canvas;
    private List<PersonNode> nodes;
    private List<Connection> connections;
    private PersonNode selectedNodeForConnection = null;

    // Inner class to track connections and their labels
    private static class Connection {
        Line line;
        Text label;
        PersonNode node1;
        PersonNode node2;

        Connection(Line line, Text label, PersonNode node1, PersonNode node2) {
            this.line = line;
            this.label = label;
            this.node1 = node1;
            this.node2 = node2;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        canvas = new Pane();
        nodes = new ArrayList<>();
        connections = new ArrayList<>();

        canvas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && selectedNodeForConnection == null) {
                showNewNodeDialog(event.getX(), event.getY());
            }
        });

        createSampleData();

        Scene scene = new Scene(canvas, 800, 600, Color.WHITE);
        primaryStage.setTitle("People Connection Map");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showNewNodeDialog(double x, double y) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Create New Person Node");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        GridPane.setConstraints(firstNameLabel, 0, 0);
        GridPane.setConstraints(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        GridPane.setConstraints(lastNameLabel, 0, 1);
        GridPane.setConstraints(lastNameField, 1, 1);

        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        GridPane.setConstraints(descriptionLabel, 0, 2);
        GridPane.setConstraints(descriptionField, 1, 2);

        Button createButton = new Button("Create Node");
        Button cancelButton = new Button("Cancel");
        GridPane.setConstraints(createButton, 0, 3);
        GridPane.setConstraints(cancelButton, 1, 3);

        grid.getChildren().addAll(
            firstNameLabel, firstNameField, 
            lastNameLabel, lastNameField, 
            descriptionLabel, descriptionField, 
            createButton, cancelButton
        );

        createButton.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                String fullName = firstName + " " + lastName;
                PersonNode newNode = new PersonNode(fullName, description, x, y);
                addNode(newNode);
                dialogStage.close();
            } else {
                showAlert("Please enter both first and last names.");
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        Scene dialogScene = new Scene(grid, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void addNode(PersonNode node) {
        nodes.add(node);
        canvas.getChildren().addAll(node.getCircle(), node.getLabel());

        node.getCircle().setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleNodeConnection(node);
            }
            event.consume();
        });
    }

    private void handleNodeConnection(PersonNode clickedNode) {
        if (selectedNodeForConnection == null) {
            selectedNodeForConnection = clickedNode;
            highlightNode(clickedNode);
            showAlert("Select the second node to connect.");
        } else {
            if (selectedNodeForConnection != clickedNode) {
                // Check if connection already exists
                Connection existingConnection = findConnection(selectedNodeForConnection, clickedNode);
                
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Create Connection");
                if (existingConnection != null) {
                    dialog.setHeaderText("Update existing relationship");
                    dialog.setContentText("New relationship:");
                } else {
                    dialog.setHeaderText("Describe the relationship");
                    dialog.setContentText("Relationship:");
                }

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(relationship -> {
                    if (existingConnection != null) {
                        // Remove old connection
                        removeConnection(existingConnection);
                    }
                    createConnection(selectedNodeForConnection, clickedNode, relationship);
                });
            }
            
            unhighlightNode(selectedNodeForConnection);
            selectedNodeForConnection = null;
        }
    }

    private Connection findConnection(PersonNode node1, PersonNode node2) {
        for (Connection conn : connections) {
            if ((conn.node1 == node1 && conn.node2 == node2) ||
                (conn.node1 == node2 && conn.node2 == node1)) {
                return conn;
            }
        }
        return null;
    }

    private void removeConnection(Connection connection) {
        canvas.getChildren().remove(connection.line);
        canvas.getChildren().remove(connection.label);
        connections.remove(connection);
    }

    private void highlightNode(PersonNode node) {
        node.getCircle().setStroke(Color.RED);
        node.getCircle().setStrokeWidth(3);
    }

    private void unhighlightNode(PersonNode node) {
        node.getCircle().setStroke(Color.BLACK);
        node.getCircle().setStrokeWidth(1);
    }

    private void createConnection(PersonNode node1, PersonNode node2, String relationship) {
        Line connection = new Line(node1.getX(), node1.getY(), node2.getX(), node2.getY());
        connection.setStroke(Color.GRAY);
        connection.setStrokeWidth(2);

        Text relationshipLabel = new Text(
            (node1.getX() + node2.getX()) / 2,
            (node1.getY() + node2.getY()) / 2,
            relationship
        );
        relationshipLabel.setFill(Color.BLACK);

        canvas.getChildren().addAll(connection, relationshipLabel);
        connections.add(new Connection(connection, relationshipLabel, node1, node2));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void createSampleData() {
        PersonNode alice = new PersonNode("Alice Johnson", "Friend", 200, 150);
        PersonNode bob = new PersonNode("Bob Smith", "Colleague", 400, 300);
        PersonNode charlie = new PersonNode("Charlie Brown", "Classmate", 600, 150);

        addNode(alice);
        addNode(bob);
        addNode(charlie);

        createConnection(alice, bob, "Works with");
        createConnection(bob, charlie, "Studies with");
    }

    public static void main(String[] args) {
        launch(args);
    }
}