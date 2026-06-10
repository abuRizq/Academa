package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * PageKit — shared layout primitives used across multiple screens.
 * Ports the React PageKit helpers (Stat, BarRow, Columns, Trend, Toolbar, Spark).
 */
public final class PageKit {

    private PageKit() {}

    /* ============== TONE COLORS ============== */
    private static String toneColor(String tone) {
        return switch (tone) {
            case "success" -> "#2e6b4f";
            case "danger" -> "#9b2c2c";
            case "gold" -> "#985f1f";
            case "amber" -> "#b8762a";
            default -> "#2c40a0";
        };
    }

    private static String toneBg(String tone) {
        return switch (tone) {
            case "success" -> "#e6f4ed";
            case "danger" -> "#fce8e8";
            case "gold" -> "#fef6e6";
            case "amber" -> "#fef6e6";
            default -> "#eaedfa";
        };
    }

    /* ============== STAT ============== */
    public static HBox stat(String icon, String tone, String value, String label,
                            String delta, String deltaTone, boolean live) {
        HBox card = new HBox(13);
        card.getStyleClass().add("pk-stat");
        card.setAlignment(Pos.CENTER_LEFT);

        if (icon != null) {
            StackPane iconBox = new StackPane();
            iconBox.setMinSize(40, 40);
            iconBox.setPrefSize(40, 40);
            iconBox.setMaxSize(40, 40);
            iconBox.setStyle("-fx-background-color: " + toneBg(tone) + "; -fx-background-radius: 9;");
            iconBox.setAlignment(Pos.CENTER);
            iconBox.getChildren().add(AcIcon.create(icon, 19, Color.web(toneColor(tone))));
            card.getChildren().add(iconBox);
        }

        VBox body = new VBox(1);
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #6f7484;");
        body.getChildren().addAll(v, l);

        if (delta != null) {
            HBox deltaRow = new HBox(5);
            deltaRow.setAlignment(Pos.CENTER_LEFT);
            if (live) {
                Region dot = new Region();
                dot.setMinSize(7, 7);
                dot.setPrefSize(7, 7);
                dot.setMaxSize(7, 7);
                dot.setStyle("-fx-background-color: #2e6b4f; -fx-background-radius: 999;");
                deltaRow.getChildren().add(dot);
            }
            Label d = new Label(delta);
            String dColor = switch (deltaTone != null ? deltaTone : "") {
                case "up" -> "#2e6b4f";
                case "down" -> "#9b2c2c";
                default -> live ? "#2e6b4f" : "#8c909e";
            };
            d.setStyle("-fx-font-size: 11px; -fx-text-fill: " + dColor + ";");
            deltaRow.getChildren().add(d);
            body.getChildren().add(deltaRow);
        }

        card.getChildren().add(body);
        return card;
    }

    public static HBox stat(String icon, String tone, String value, String label, String delta) {
        return stat(icon, tone, value, label, delta, null, false);
    }

    /** Creates a horizontal row of stat cards */
    public static HBox statRow(HBox... stats) {
        HBox row = new HBox(16);
        for (HBox s : stats) {
            HBox.setHgrow(s, Priority.ALWAYS);
            row.getChildren().add(s);
        }
        return row;
    }

    /* ============== BAR ROW ============== */
    public static VBox barRow(String label, double value, double max, String tone,
                              String valueText, String meta) {
        VBox row = new VBox(4);
        double pct = Math.max(2, Math.min(100, (value / max) * 100));

        boolean showHead = label != null || meta != null || valueText != null;
        if (showHead) {
            HBox head = new HBox();
            head.setAlignment(Pos.CENTER_LEFT);
            if (label != null) {
                Label l = new Label(label);
                l.setStyle("-fx-font-size: 13px; -fx-text-fill: #3b3f4c;");
                HBox.setHgrow(l, Priority.ALWAYS);
                head.getChildren().add(l);
            }
            if (meta != null) {
                Label m = new Label(meta);
                m.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e; -fx-padding: 0 8 0 0;");
                head.getChildren().add(m);
            }
            if (valueText != null) {
                Label vt = new Label(valueText);
                vt.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
                head.getChildren().add(vt);
            }
            row.getChildren().add(head);
        }

        StackPane track = new StackPane();
        track.setMinHeight(6);
        track.setPrefHeight(6);
        track.setMaxHeight(6);
        track.setStyle("-fx-background-color: #e8e7df; -fx-background-radius: 999;");
        track.setAlignment(Pos.CENTER_LEFT);

        Region fill = new Region();
        fill.setMinHeight(6);
        fill.setPrefHeight(6);
        fill.setMaxHeight(6);
        fill.setMaxWidth(Region.USE_PREF_SIZE);
        fill.setStyle("-fx-background-color: " + toneColor(tone) + "; -fx-background-radius: 999;");

        track.getChildren().add(fill);
        row.getChildren().add(track);

        // Bind fill width to track width
        track.widthProperty().addListener((obs, o, n) -> {
            fill.setPrefWidth(n.doubleValue() * pct / 100.0);
        });

        return row;
    }

    public static VBox barRow(String label, double value, String tone, String valueText) {
        return barRow(label, value, 100, tone, valueText, null);
    }

    /* ============== COLUMNS (bar chart) ============== */
    public static HBox columns(double[][] data, String[] labels, double height) {
        HBox cols = new HBox(4);
        cols.setAlignment(Pos.BOTTOM_CENTER);
        cols.setMinHeight(height);
        cols.setPrefHeight(height);

        double max = 0;
        for (double[] d : data) max = Math.max(max, d[0]);
        if (max == 0) max = 1;

        for (int i = 0; i < data.length; i++) {
            VBox col = new VBox(4);
            col.setAlignment(Pos.BOTTOM_CENTER);
            HBox.setHgrow(col, Priority.ALWAYS);

            Label cap = new Label(String.valueOf((int) data[i][0]));
            cap.setStyle("-fx-font-size: 10px; -fx-text-fill: #6f7484;");

            double h = Math.max(3, (data[i][0] / max) * 100);
            Region bar = new Region();
            bar.setMinHeight(h * height / 130);
            bar.setPrefHeight(h * height / 130);
            String barTone = data[i].length > 1 && data[i][1] == 1 ? "#2c40a0" : "#c2c1b8";
            bar.setStyle("-fx-background-color: " + barTone + "; -fx-background-radius: 4 4 0 0;");

            Label lbl = new Label(labels != null && i < labels.length ? labels[i] : "");
            lbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #8c909e;");

            col.getChildren().addAll(cap, bar, lbl);
            cols.getChildren().add(col);
        }
        return cols;
    }

    /* ============== TREND ============== */
    public static HBox trend(String dir, String text) {
        HBox t = new HBox(4);
        t.setAlignment(Pos.CENTER_LEFT);
        String iconName = switch (dir) {
            case "up" -> "trending-up";
            case "down" -> "trending-down";
            default -> "minus";
        };
        String color = switch (dir) {
            case "up" -> "#2e6b4f";
            case "down" -> "#9b2c2c";
            default -> "#8c909e";
        };
        t.getChildren().add(AcIcon.create(iconName, 13, Color.web(color)));
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: " + color + ";");
        t.getChildren().add(l);
        return t;
    }

    /* ============== TOOLBAR ============== */
    public static HBox toolbar(Node... children) {
        HBox tb = new HBox(10);
        tb.getStyleClass().add("pk-toolbar");
        tb.setAlignment(Pos.CENTER_LEFT);
        tb.setPadding(new Insets(0, 0, 16, 0));
        for (Node c : children) tb.getChildren().add(c);
        return tb;
    }

    /** Spacer region for toolbar */
    public static Region spacer() {
        Region s = new Region();
        HBox.setHgrow(s, Priority.ALWAYS);
        return s;
    }
}
