package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Input component — text field with label, icon, and suffix support.
 * Supports password type with PasswordField.
 * Mirrors the React Input component.
 */
public final class AcInput extends VBox {

    private final TextField textField;

    public AcInput(String label, String type, String value, String placeholder,
                   Node leftIcon, String suffix, String size, boolean required) {
        setSpacing(6);

        // Label
        if (label != null && !label.isEmpty()) {
            Label lbl = new Label(label);
            lbl.getStyleClass().add("ac-field-label");
            if (required) {
                Label req = new Label("*");
                req.setStyle("-fx-text-fill: #9b2c2c; -fx-padding: 0 0 0 2;");
                HBox labelBox = new HBox(lbl, req);
                labelBox.setAlignment(Pos.CENTER_LEFT);
                getChildren().add(labelBox);
            } else {
                getChildren().add(lbl);
            }
        }

        // Input box
        HBox inputBox = new HBox(9);
        inputBox.getStyleClass().add("ac-input-box");
        if ("sm".equals(size)) inputBox.getStyleClass().add("ac-input-sm");
        inputBox.setAlignment(Pos.CENTER_LEFT);

        if (leftIcon != null) {
            StackPane iconWrap = new StackPane(leftIcon);
            iconWrap.getStyleClass().add("ac-input-icon");
            inputBox.getChildren().add(iconWrap);
        }

        // Use PasswordField for password type
        if ("password".equals(type)) {
            PasswordField pf = new PasswordField();
            if (value != null) pf.setText(value);
            if (placeholder != null) pf.setPromptText(placeholder);
            pf.setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0; " +
                "-fx-font-size: " + ("sm".equals(size) ? "13.5" : "15") + "px; " +
                "-fx-text-fill: #1b1d25;"
            );
            HBox.setHgrow(pf, Priority.ALWAYS);
            inputBox.getChildren().add(pf);
            textField = pf; // PasswordField extends TextField
        } else {
            textField = new TextField(value != null ? value : "");
            if (placeholder != null) textField.setPromptText(placeholder);
            textField.setStyle(
                "-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0; " +
                "-fx-font-size: " + ("sm".equals(size) ? "13.5" : "15") + "px; " +
                "-fx-text-fill: #1b1d25;"
            );
            HBox.setHgrow(textField, Priority.ALWAYS);
            inputBox.getChildren().add(textField);
        }

        if (suffix != null && !suffix.isEmpty()) {
            Label sfx = new Label(suffix);
            sfx.getStyleClass().add("ac-input-suffix");
            inputBox.getChildren().add(sfx);
        }

        getChildren().add(inputBox);
    }

    public AcInput(String label, String value) {
        this(label, "text", value, null, null, null, "md", false);
    }

    public AcInput(String placeholder, Node leftIcon, String size) {
        this(null, "text", null, placeholder, leftIcon, null, size, false);
    }

    public AcInput() {
        this(null, "text", null, null, null, null, "md", false);
    }

    public TextField getTextField() {
        return textField;
    }

    public String getValue() {
        return textField.getText();
    }

    public void setValue(String val) {
        textField.setText(val);
    }
}
