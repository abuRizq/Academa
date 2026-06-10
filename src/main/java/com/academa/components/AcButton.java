package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Button component — primary, secondary, ghost, gold, danger variants.
 * Mirrors the React Button component.
 */
public final class AcButton extends Button {

    public enum Variant { PRIMARY, SECONDARY, GHOST, GOLD, DANGER }
    public enum Size { SM, MD, LG }

    public AcButton(String text, Variant variant, Size size, Node leftIcon, Node rightIcon) {
        super(text);
        getStyleClass().addAll("ac-btn", "ac-btn-" + variant.name().toLowerCase());

        if (size != Size.MD) {
            getStyleClass().add("ac-btn-" + size.name().toLowerCase());
        }

        if (leftIcon != null || rightIcon != null) {
            HBox content = new HBox(8);
            content.setAlignment(Pos.CENTER);
            if (leftIcon != null) content.getChildren().add(leftIcon);
            if (text != null && !text.isEmpty()) {
                javafx.scene.control.Label lbl = new javafx.scene.control.Label(text);
                lbl.setStyle("-fx-text-fill: inherit; -fx-font-weight: bold;");
                content.getChildren().add(lbl);
            }
            if (rightIcon != null) content.getChildren().add(rightIcon);
            setGraphic(content);
            setText("");
        }
    }

    public AcButton(String text, Variant variant, Size size) {
        this(text, variant, size, null, null);
    }

    public AcButton(String text, Variant variant) {
        this(text, variant, Size.MD, null, null);
    }

    public AcButton(String text) {
        this(text, Variant.PRIMARY, Size.MD, null, null);
    }

    /** Factory to create icon from AcIcon system */
    public static Node icon(String name, double size, Color color) {
        return AcIcon.create(name, size, color);
    }

    public void setFullWidth(boolean full) {
        if (full) {
            setMaxWidth(Double.MAX_VALUE);
        }
    }
}
