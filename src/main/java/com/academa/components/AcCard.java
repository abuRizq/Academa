package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Card component — surface container with optional header.
 * Mirrors the React Card component.
 */
public final class AcCard extends VBox {

    private final VBox body;

    public AcCard(String title, Node headerAction, String padding, boolean interactive, Node... children) {
        getStyleClass().add("ac-card");
        if (interactive) getStyleClass().add("ac-card-interactive");

        // Header
        if (title != null && !title.isEmpty()) {
            HBox header = new HBox(12);
            header.getStyleClass().add("ac-card-header");
            header.setAlignment(Pos.CENTER_LEFT);

            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("ac-card-title");
            HBox.setHgrow(titleLabel, Priority.ALWAYS);
            header.getChildren().add(titleLabel);

            if (headerAction != null) {
                header.getChildren().add(headerAction);
            }
            getChildren().add(header);
        }

        // Body
        body = new VBox();
        String padClass = "sm".equals(padding) ? "ac-card-body-sm" : "ac-card-body";
        body.getStyleClass().add(padClass);
        body.getChildren().addAll(children);
        getChildren().add(body);
        VBox.setVgrow(body, Priority.ALWAYS);
    }

    public AcCard(String title, Node... children) {
        this(title, null, "md", false, children);
    }

    public AcCard(Node... children) {
        this(null, null, "md", false, children);
    }

    public VBox getBody() {
        return body;
    }
}
