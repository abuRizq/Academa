package com.academa.components;

import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * IconButton component — square button containing only an icon.
 * Mirrors the React IconButton component.
 */
public final class AcIconButton extends Button {

    public enum Variant { GHOST, SOLID, OUTLINE }
    public enum Size { SM, MD, LG }

    public AcIconButton(Node icon, String ariaLabel, Variant variant, Size size) {
        getStyleClass().add("ac-iconbtn");

        if (variant != Variant.GHOST) {
            getStyleClass().add("ac-iconbtn-" + variant.name().toLowerCase());
        }
        if (size != Size.MD) {
            getStyleClass().add("ac-iconbtn-" + size.name().toLowerCase());
        }

        if (icon != null) setGraphic(icon);
        if (ariaLabel != null) {
            setAccessibleText(ariaLabel);
            // Tooltip
            javafx.scene.control.Tooltip tip = new javafx.scene.control.Tooltip(ariaLabel);
            setTooltip(tip);
        }
    }

    public AcIconButton(Node icon, String ariaLabel) {
        this(icon, ariaLabel, Variant.GHOST, Size.MD);
    }

    public AcIconButton(Node icon) {
        this(icon, null, Variant.GHOST, Size.MD);
    }
}
