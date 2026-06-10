package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Badge component — small status/category pill.
 * Mirrors the React Badge component.
 */
public final class AcBadge extends HBox {

    public enum Variant { NEUTRAL, BRAND, SUCCESS, WARNING, DANGER, GOLD }
    public enum Appearance { SOFT, SOLID, OUTLINE }

    public AcBadge(String text, Variant variant, Appearance appearance, boolean dot) {
        getStyleClass().add("ac-badge");
        setAlignment(Pos.CENTER);
        setSpacing(5);
        setMinHeight(22);
        setPrefHeight(22);

        // Apply variant + appearance style
        String styleClass = switch (appearance) {
            case SOLID -> "ac-badge-solid-" + variant.name().toLowerCase();
            case OUTLINE -> "ac-badge-outline";
            default -> "ac-badge-" + variant.name().toLowerCase();
        };
        getStyleClass().add(styleClass);

        if (dot) {
            String dotColor = switch (variant) {
                case SUCCESS -> "#2e6b4f";
                case WARNING -> "#b8762a";
                case DANGER -> "#9b2c2c";
                case GOLD -> "#985f1f";
                case BRAND -> "#2c40a0";
                default -> "#6f7484";
            };
            Region dotNode = new Region();
            dotNode.setMinSize(6, 6);
            dotNode.setPrefSize(6, 6);
            dotNode.setMaxSize(6, 6);
            dotNode.setStyle("-fx-background-radius: 999; -fx-background-color: " + dotColor + ";");
            getChildren().add(dotNode);
        }

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px; -fx-font-weight: 500;");
        getChildren().add(label);
    }

    public AcBadge(String text, Variant variant) {
        this(text, variant, Appearance.SOFT, false);
    }

    public AcBadge(String text, Variant variant, boolean dot) {
        this(text, variant, Appearance.SOFT, dot);
    }

    public AcBadge(String text, Variant variant, Appearance appearance) {
        this(text, variant, appearance, false);
    }
}
