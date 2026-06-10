package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Tag component — small label chip with optional hash prefix.
 * Mirrors the React Tag component.
 */
public final class AcTag extends HBox {

    public AcTag(String text, boolean hash, boolean active) {
        getStyleClass().add("ac-tag");
        if (active) getStyleClass().add("ac-tag-active");
        setAlignment(Pos.CENTER);
        setSpacing(6);

        if (hash) {
            Label hashLabel = new Label("#");
            hashLabel.getStyleClass().add("ac-tag-hash");
            getChildren().add(hashLabel);
        }

        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 12.5px; -fx-font-weight: 500;");
        getChildren().add(lbl);
    }

    public AcTag(String text, boolean hash) {
        this(text, hash, false);
    }

    public AcTag(String text) {
        this(text, false, false);
    }
}
