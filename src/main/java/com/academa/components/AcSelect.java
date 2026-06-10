package com.academa.components;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;

/**
 * Select component — dropdown selector.
 * Mirrors the React Select component.
 */
public final class AcSelect extends StackPane {

    private final ComboBox<String> comboBox;

    public AcSelect(String[] options, String defaultValue, String size) {
        getStyleClass().add("ac-select");

        comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options);
        if (defaultValue != null) {
            comboBox.setValue(defaultValue);
        }

        String fontSize = "sm".equals(size) ? "13.5" : "15";
        comboBox.setStyle(
            "-fx-font-size: " + fontSize + "px; " +
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #d8d7cf; " +
            "-fx-border-width: 1; " +
            "-fx-background-radius: 9; " +
            "-fx-border-radius: 9; " +
            "-fx-cursor: hand;"
        );

        if ("sm".equals(size)) {
            comboBox.setPrefHeight(32);
        } else {
            comboBox.setPrefHeight(40);
        }

        getChildren().add(comboBox);
    }

    public AcSelect(String[] options, String defaultValue) {
        this(options, defaultValue, "md");
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public String getValue() {
        return comboBox.getValue();
    }
}
