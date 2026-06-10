package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * ProgressBar component — horizontal progress indicator.
 * Mirrors the React ProgressBar component.
 */
public final class AcProgressBar extends VBox {

    public enum Variant { BRAND, GOLD, SUCCESS, DANGER }
    public enum Size { SM, MD, LG }

    public AcProgressBar(String label, double value, double max,
                         Variant variant, Size size, boolean showValue,
                         java.util.function.BiFunction<Double, Double, String> valueFormat) {
        setSpacing(7);
        double pct = Math.max(0, Math.min(100, (value / max) * 100));

        if (size == Size.SM) getStyleClass().add("ac-progress-sm");

        // Top row: label + value
        if (label != null || showValue) {
            HBox top = new HBox(12);
            top.setAlignment(Pos.CENTER_LEFT);

            Label lblNode = new Label(label != null ? label : "");
            lblNode.getStyleClass().add("ac-progress-label");
            HBox.setHgrow(lblNode, Priority.ALWAYS);
            top.getChildren().add(lblNode);

            if (showValue) {
                String valText;
                if (valueFormat != null) {
                    valText = valueFormat.apply(value, max);
                } else {
                    valText = Math.round(pct) + "%";
                }
                Label valNode = new Label(valText);
                valNode.getStyleClass().add("ac-progress-value");
                top.getChildren().add(valNode);
            }
            getChildren().add(top);
        }

        // Track
        StackPane track = new StackPane();
        track.getStyleClass().add("ac-progress-track");
        track.setAlignment(Pos.CENTER_LEFT);

        double trackH = switch (size) {
            case SM -> 5;
            case MD -> 8;
            case LG -> 12;
        };

        track.setMinHeight(trackH);
        track.setPrefHeight(trackH);
        track.setMaxHeight(trackH);

        // Fill
        Region fill = new Region();
        fill.getStyleClass().add("ac-progress-fill");
        String variantClass = switch (variant) {
            case GOLD -> "ac-progress-fill-gold";
            case SUCCESS -> "ac-progress-fill-success";
            case DANGER -> "ac-progress-fill-danger";
            default -> "";
        };
        if (!variantClass.isEmpty()) fill.getStyleClass().add(variantClass);

        fill.setMinHeight(trackH);
        fill.setPrefHeight(trackH);
        fill.setMaxHeight(trackH);

        // Bind fill width to percentage of track width
        fill.maxWidthProperty().bind(track.widthProperty().multiply(pct / 100.0));
        fill.prefWidthProperty().bind(track.widthProperty().multiply(pct / 100.0));

        track.getChildren().add(fill);
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        getChildren().add(track);
        HBox.setHgrow(this, Priority.ALWAYS);
    }

    public AcProgressBar(String label, double value, double max, Variant variant) {
        this(label, value, max, variant, Size.MD, false, null);
    }

    public AcProgressBar(double value, double max) {
        this(null, value, max, Variant.BRAND, Size.MD, false, null);
    }
}
