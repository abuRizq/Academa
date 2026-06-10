package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Avatar component — displays user initials in a colored circle.
 * Mirrors the React Avatar component.
 */
public final class AcAvatar extends StackPane {

    public enum Size { XS, SM, MD, LG }

    public AcAvatar(String name, Size size, boolean honors, String status) {
        getStyleClass().add("ac-avatar");
        setAlignment(Pos.CENTER);

        double dim = switch (size) {
            case XS -> 24;
            case SM -> 32;
            case MD -> 40;
            case LG -> 56;
        };

        setMinSize(dim, dim);
        setPrefSize(dim, dim);
        setMaxSize(dim, dim);

        String styleSize = switch (size) {
            case XS -> "ac-avatar-xs";
            case SM -> "ac-avatar-sm";
            case MD -> "";
            case LG -> "ac-avatar-lg";
        };
        if (!styleSize.isEmpty()) getStyleClass().add(styleSize);
        if (honors) getStyleClass().add("ac-avatar-honors");

        // Initials
        Label initialsLabel = new Label(initials(name));
        initialsLabel.setStyle("-fx-font-size: " + (dim * 0.4) + "px; -fx-font-weight: bold; -fx-text-fill: #233685;");
        getChildren().add(initialsLabel);

        // Status dot
        if (status != null && !status.isEmpty()) {
            double dotSize = Math.max(9, dim * 0.3);
            Circle dot = new Circle(dotSize / 2);
            dot.setStroke(Color.WHITE);
            dot.setStrokeWidth(2);
            dot.setFill(switch (status) {
                case "online" -> Color.web("#2e6b4f");
                case "busy" -> Color.web("#9b2c2c");
                default -> Color.web("#b2b6c0");
            });
            StackPane.setAlignment(dot, Pos.BOTTOM_RIGHT);
            getChildren().add(dot);
        }
    }

    public AcAvatar(String name, Size size) {
        this(name, size, false, null);
    }

    public AcAvatar(String name) {
        this(name, Size.MD, false, null);
    }

    private static String initials(String name) {
        if (name == null || name.isBlank()) return "";
        String[] parts = name.trim().split("\\s+");
        String first = parts[0].isEmpty() ? "" : String.valueOf(parts[0].charAt(0));
        String last = parts.length > 1 ? String.valueOf(parts[parts.length - 1].charAt(0)) : "";
        return (first + last).toUpperCase();
    }
}
