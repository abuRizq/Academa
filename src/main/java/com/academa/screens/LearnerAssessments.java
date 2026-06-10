package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * LearnerAssessments — assessment schedule with sidebar and grouped list.
 * Mirrors React LearnerAssessments.jsx.
 */
public final class LearnerAssessments extends VBox {

    private record Assessment(String d, String m, String course, String title,
                              int items, int mins, String status, int progress,
                              String due, String kind, int score) {}

    private record Group(String title, List<Assessment> items) {}

    private static final List<Group> GROUPS = List.of(
        new Group("This week", List.of(
            new Assessment("14", "Oct", "MATH 204", "Linear Algebra — Midterm", 20, 90, "available", 0, "Due today · 17:00", "Summative", 0),
            new Assessment("16", "Oct", "BIO 110", "Cellular Respiration", 15, 45, "in-progress", 60, "Closes in 2d", "Formative", 0),
            new Assessment("17", "Oct", "HIST 230", "The Interwar Period", 24, 60, "available", 0, "Opens now", "Summative", 0)
        )),
        new Group("Upcoming", List.of(
            new Assessment("21", "Oct", "CHEM 101", "Stoichiometry Drill", 12, 30, "locked", 0, "Opens Mon 09:00", "Diagnostic", 0),
            new Assessment("24", "Oct", "PHYS 140", "Kinematics Unit Test", 22, 75, "locked", 0, "Opens Thu", "Summative", 0)
        )),
        new Group("Resolved", List.of(
            new Assessment("09", "Oct", "ENG 215", "Rhetoric & Argument", 18, 50, "completed", 0, "Resolved · 2d ago", "Summative", 91),
            new Assessment("04", "Oct", "CS 150", "Recursion & Trees", 16, 40, "completed", 0, "Resolved · 5d ago", "Formative", 97)
        ))
    );

    private final Runnable onBegin;

    public LearnerAssessments(Runnable onBegin) {
        this.onBegin = onBegin;
        setSpacing(22);

        // Left sidebar
        VBox sidebar = new VBox(16);
        sidebar.setMinWidth(260);
        sidebar.setPrefWidth(280);

        // Up next
        VBox upNext = new VBox(10);
        upNext.setStyle("-fx-background-color: #eaedfa; -fx-background-radius: 13; -fx-padding: 20;");
        Label cap = new Label("UP NEXT");
        cap.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #2c40a0;");
        Label nextTitle = new Label("Linear Algebra — Midterm");
        nextTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        nextTitle.setWrapText(true);
        Label nextMeta = new Label("MATH 204 · 20 items · 90 min");
        nextMeta.setStyle("-fx-font-size: 12px; -fx-text-fill: #6f7484;");
        HBox dueRow = new HBox(6);
        dueRow.setAlignment(Pos.CENTER_LEFT);
        dueRow.getChildren().addAll(AcIcon.create("alarm-clock", 14, Color.web("#9b2c2c")),
            new Label("Due today · 17:00") {{ setStyle("-fx-font-size: 13px; -fx-text-fill: #9b2c2c;"); }});
        AcButton beginBtn = new AcButton("Begin assessment", AcButton.Variant.PRIMARY, AcButton.Size.MD,
            null, AcIcon.create("arrow-right", 13, Color.WHITE));
        beginBtn.setFullWidth(true);
        beginBtn.setMaxWidth(Double.MAX_VALUE);
        beginBtn.setOnAction(e -> { if (onBegin != null) onBegin.run(); });
        upNext.getChildren().addAll(cap, nextTitle, nextMeta, dueRow, beginBtn);

        // Tallies
        AcCard tallyCard = new AcCard(null, null, "sm", false);
        VBox tallies = new VBox(8);
        tallies.getChildren().addAll(
            tally("circle-play", "brand", "Available now", "2"),
            tally("loader", "amber", "In progress", "1"),
            tally("circle-check-big", "success", "Resolved", "14"),
            tally("target", "gold", "Term average", "94.6")
        );
        tallyCard.getBody().getChildren().add(tallies);

        sidebar.getChildren().addAll(upNext, tallyCard);

        // Main area
        VBox main = new VBox(22);
        HBox.setHgrow(main, Priority.ALWAYS);

        for (Group g : GROUPS) {
            VBox group = new VBox(8);

            // Group header
            HBox head = new HBox(10);
            head.setAlignment(Pos.CENTER_LEFT);
            Label gTitle = new Label(g.title);
            gTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
            Region line = new Region();
            HBox.setHgrow(line, Priority.ALWAYS);
            line.setStyle("-fx-background-color: #e3e2db; -fx-min-height: 1; -fx-pref-height: 1;");
            Label count = new Label(String.valueOf(g.items.size()));
            count.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e; -fx-background-color: #f2f1ea; -fx-background-radius: 999; -fx-padding: 2 8 2 8;");
            head.getChildren().addAll(gTitle, line, count);

            VBox rows = new VBox();
            for (Assessment a : g.items) {
                rows.getChildren().add(createRow(a));
            }
            group.getChildren().addAll(head, rows);
            main.getChildren().add(group);
        }

        getChildren().addAll(sidebar, main);
    }

    private HBox createRow(Assessment a) {
        HBox row = new HBox(14);
        row.setPadding(new Insets(14, 12, 14, 12));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;" +
            ("completed".equals(a.status) ? " -fx-opacity: 0.65;" : ""));

        // Date
        VBox dateBox = new VBox();
        dateBox.setAlignment(Pos.CENTER);
        dateBox.setMinWidth(36);
        Label dd = new Label(a.d);
        dd.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label mm = new Label(a.m);
        mm.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");
        dateBox.getChildren().addAll(dd, mm);

        // Main
        VBox mainCol = new VBox(3);
        HBox.setHgrow(mainCol, Priority.ALWAYS);
        Label course = new Label(a.course + " · " + a.kind);
        course.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #2c40a0;");
        Label title = new Label(a.title);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        mainCol.getChildren().addAll(course, title);

        if ("in-progress".equals(a.status)) {
            AcProgressBar pb = new AcProgressBar(null, a.progress, 100,
                AcProgressBar.Variant.BRAND, AcProgressBar.Size.SM, true, (v, m) -> (int)(double)v + "% done");
            pb.setMaxWidth(280);
            mainCol.getChildren().add(pb);
        } else {
            HBox meta = new HBox(14);
            meta.setAlignment(Pos.CENTER_LEFT);
            meta.setPadding(new Insets(4, 0, 0, 0));
            meta.getChildren().addAll(
                metaItem("list-checks", a.items + " items"),
                metaItem("timer", a.mins + " min"),
                metaItem("calendar", a.due)
            );
            mainCol.getChildren().add(meta);
        }

        // Right
        VBox right = new VBox(6);
        right.setAlignment(Pos.CENTER_RIGHT);
        if ("completed".equals(a.status)) {
            Label score = new Label(a.score + "%");
            score.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e6b4f;");
            right.getChildren().add(score);
        } else {
            right.getChildren().add(statusBadge(a.status));
        }
        right.getChildren().add(actionButton(a));

        row.getChildren().addAll(dateBox, mainCol, right);
        return row;
    }

    private HBox metaItem(String icon, String text) {
        HBox h = new HBox(5);
        h.setAlignment(Pos.CENTER_LEFT);
        h.getChildren().addAll(AcIcon.create(icon, 13, Color.web("#8c909e")),
            new Label(text) {{ setStyle("-fx-font-size: 12.5px; -fx-text-fill: #6f7484;"); }});
        return h;
    }

    private Node statusBadge(String status) {
        return switch (status) {
            case "available" -> new AcBadge("Available", AcBadge.Variant.BRAND, true);
            case "in-progress" -> new AcBadge("In progress", AcBadge.Variant.WARNING, true);
            case "locked" -> new AcBadge("Locked", AcBadge.Variant.NEUTRAL);
            default -> new AcBadge("Resolved", AcBadge.Variant.SUCCESS, true);
        };
    }

    private AcButton actionButton(Assessment a) {
        return switch (a.status) {
            case "available" -> {
                AcButton b = new AcButton("Begin", AcButton.Variant.PRIMARY, AcButton.Size.SM,
                    null, AcIcon.create("arrow-right", 13, Color.WHITE));
                b.setOnAction(e -> { if (onBegin != null) onBegin.run(); });
                yield b;
            }
            case "in-progress" -> {
                AcButton b = new AcButton("Resume", AcButton.Variant.PRIMARY, AcButton.Size.SM);
                b.setOnAction(e -> { if (onBegin != null) onBegin.run(); });
                yield b;
            }
            case "completed" -> new AcButton("Review", AcButton.Variant.SECONDARY, AcButton.Size.SM);
            default -> {
                AcButton b = new AcButton("Locked", AcButton.Variant.GHOST, AcButton.Size.SM,
                    AcIcon.create("lock", 13, Color.web("#8c909e")), null);
                b.setDisable(true);
                yield b;
            }
        };
    }

    private HBox tally(String icon, String tone, String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));

        String bgColor = switch (tone) {
            case "success" -> "#e6f4ed";
            case "amber" -> "#fef6e6";
            case "gold" -> "#fef6e6";
            default -> "#eaedfa";
        };
        String fgColor = switch (tone) {
            case "success" -> "#2e6b4f";
            case "amber" -> "#b8762a";
            case "gold" -> "#985f1f";
            default -> "#2c40a0";
        };
        StackPane iconBox = new StackPane();
        iconBox.setMinSize(28, 28); iconBox.setPrefSize(28, 28); iconBox.setMaxSize(28, 28);
        iconBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 7;");
        iconBox.setAlignment(Pos.CENTER);
        iconBox.getChildren().add(AcIcon.create(icon, 16, Color.web(fgColor)));

        Label l = new Label(label);
        l.setStyle("-fx-font-size: 13px; -fx-text-fill: #6f7484;");
        HBox.setHgrow(l, Priority.ALWAYS);

        Label v = new Label(value);
        v.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");

        row.getChildren().addAll(iconBox, l, v);
        return row;
    }
}
