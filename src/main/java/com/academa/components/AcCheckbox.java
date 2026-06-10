package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Checkbox component — styled checkbox with optional label.
 * Mirrors the React Checkbox component.
 */
public final class AcCheckbox extends HBox {

    private final CheckBox checkBox;

    public AcCheckbox(String label, boolean defaultChecked) {
        getStyleClass().add("ac-check");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);

        checkBox = new CheckBox();
        checkBox.setSelected(defaultChecked);
        getChildren().add(checkBox);

        if (label != null && !label.isEmpty()) {
            Label lbl = new Label(label);
            lbl.getStyleClass().add("ac-check-label");
            getChildren().add(lbl);
        }
    }

    public AcCheckbox(String label) {
        this(label, false);
    }

    public AcCheckbox() {
        this(null, false);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public void setSelected(boolean selected) {
        checkBox.setSelected(selected);
    }
}
