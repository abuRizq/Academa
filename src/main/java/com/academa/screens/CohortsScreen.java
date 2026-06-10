package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * CohortsScreen — cohort card grid with stats and toolbar.
 * Mirrors React Cohorts.jsx.
 */
public final class CohortsScreen extends VBox {

    private record Cohort(String name, String subject, String lead, double avg, int learners,
                          int completion, String trend, String delta, String[] roster) {}

    private static final List<Cohort> COHORTS = List.of(
        new Cohort("Grade 11 · A", "Mathematics", "Dr. Elaine Voss", 88.4, 32, 91, "up", "+2.4",
            new String[]{"Ada Okafor", "Leo Park", "Mara Quinn", "Sun Yi", "Iris Bauer"}),
        new Cohort("Grade 11 · B", "Mathematics", "Dr. Elaine Voss", 81.2, 30, 84, "up", "+1.1",
            new String[]{"Tomas Reyes", "Nadia Haaf", "Owen Cole", "Priya Rao"}),
        new Cohort("Grade 10 · C", "Sciences", "Prof. Idris Khan", 76.8, 28, 79, "down", "−0.6",
            new String[]{"Jonah Beck", "Lila Mert", "Sam Park"}),
        new Cohort("Grade 12 · Honors", "Computer Science", "Dr. R. Mensah", 92.1, 24, 96, "up", "+3.0",
            new String[]{"Ava Lin", "Mira Sol", "Kade Roe", "Yuki Tan", "Bram Ott"}),
        new Cohort("Grade 11 · D", "Humanities", "M. Webb", 73.5, 31, 72, "flat", "0.0",
            new String[]{"Cleo Vance", "Remy Diaz", "Tariq Bel"}),
        new Cohort("Grade 10 · A", "Languages", "S. Iqbal", 69.3, 29, 68, "down", "−1.8",
            new String[]{"Hana Kim", "Luca Bianchi", "Zoe Frei"})
    );

    public CohortsScreen() {
        setSpacing(22);

        // Toolbar
        getChildren().add(PageKit.toolbar(
            new AcSelect(new String[]{"All subjects", "Mathematics", "Sciences", "Humanities", "Languages", "Computer Science"}, "All subjects", "sm"),
            new AcSelect(new String[]{"Sort: Avg score", "Sort: Completion", "Sort: Size", "Sort: Trend"}, "Sort: Avg score", "sm"),
            PageKit.spacer(),
            new AcButton("Compare", AcButton.Variant.SECONDARY, AcButton.Size.SM,
                AcIcon.create("git-compare", 14, Color.web("#1b1d25")), null),
            new AcButton("New cohort", AcButton.Variant.PRIMARY, AcButton.Size.SM,
                AcIcon.create("plus", 14, Color.WHITE), null)
        ));

        // Stats
        getChildren().add(PageKit.statRow(
            PageKit.stat("layers", "brand", "41", "Active cohorts", "6 subjects"),
            PageKit.stat("target", "gold", "80.2%", "Cross-cohort mean", "+1.3 this term", "up", false),
            PageKit.stat("trophy", "success", "Gr 12 · Honors", "Top cohort", "92.1% avg"),
            PageKit.stat("triangle-alert", "danger", "2", "Below threshold", "< 70% avg", "down", false)
        ));

        // Card grid
        FlowPane grid = new FlowPane(16, 16);
        for (Cohort c : COHORTS) {
            grid.getChildren().add(createCohortCard(c));
        }
        getChildren().add(grid);
    }

    private VBox createCohortCard(Cohort c) {
        VBox card = new VBox(12);
        card.getStyleClass().add("acard");
        card.setStyle("-fx-background-color: white; -fx-border-color: #e3e2db; -fx-border-width: 1; -fx-background-radius: 13; -fx-border-radius: 13; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(20,21,27,0.04), 2, 0, 0, 1);");
        card.setMinWidth(300);
        card.setPrefWidth(320);
        card.setMaxWidth(360);

        // Top: name + badge
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        VBox nameBox = new VBox();
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        Label name = new Label(c.name);
        name.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label sub = new Label(c.subject);
        sub.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #6f7484;");
        nameBox.getChildren().addAll(name, sub);

        AcBadge badge = c.avg >= 85
            ? new AcBadge("Strong", AcBadge.Variant.SUCCESS, true)
            : c.avg >= 75 ? new AcBadge("Steady", AcBadge.Variant.GOLD, true)
            : new AcBadge("At risk", AcBadge.Variant.WARNING, true);
        top.getChildren().addAll(nameBox, badge);

        // Avg score + trend
        HBox avgRow = new HBox(8);
        avgRow.setAlignment(Pos.CENTER_LEFT);
        Label avgLabel = new Label(c.avg + "%");
        String avgColor = c.avg >= 85 ? "#2e6b4f" : c.avg >= 75 ? "#985f1f" : "#9b2c2c";
        avgLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + avgColor + ";");
        Label avgSub = new Label("avg score");
        avgSub.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
        HBox.setHgrow(avgSub, Priority.ALWAYS);
        avgRow.getChildren().addAll(avgLabel, avgSub, PageKit.trend(c.trend, c.delta));

        // Completion progress
        AcProgressBar completion = new AcProgressBar("Term completion", c.completion, 100,
            c.completion >= 85 ? AcProgressBar.Variant.SUCCESS : c.completion >= 75 ? AcProgressBar.Variant.BRAND : AcProgressBar.Variant.GOLD,
            AcProgressBar.Size.SM, true, (v, m) -> (int)(double)v + "%");

        // Meta
        HBox meta = new HBox(12);
        meta.setAlignment(Pos.CENTER_LEFT);
        meta.setStyle("-fx-padding: 10 0 0 0; -fx-border-color: #e3e2db transparent transparent transparent; -fx-border-width: 1 0 0 0;");
        VBox learnersBox = new VBox();
        Label learnersV = new Label(String.valueOf(c.learners));
        learnersV.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label learnersK = new Label("Learners");
        learnersK.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");
        learnersBox.getChildren().addAll(learnersV, learnersK);

        VBox leadBox = new VBox();
        HBox.setHgrow(leadBox, Priority.ALWAYS);
        Label leadV = new Label(c.lead);
        leadV.setStyle("-fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
        Label leadK = new Label("Lead educator");
        leadK.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");
        leadBox.getChildren().addAll(leadV, leadK);

        AcIconButton arrowBtn = new AcIconButton(
            AcIcon.create("arrow-right", 15, Color.web("#565a68")),
            "Manage", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM);
        meta.getChildren().addAll(learnersBox, leadBox, arrowBtn);

        // Roster avatars
        HBox roster = new HBox(-6);
        roster.setAlignment(Pos.CENTER_LEFT);
        roster.setPadding(new Insets(6, 0, 0, 0));
        int shown = Math.min(4, c.roster.length);
        for (int i = 0; i < shown; i++) {
            roster.getChildren().add(new AcAvatar(c.roster[i], AcAvatar.Size.SM));
        }
        if (c.learners > 4) {
            Label more = new Label("+" + (c.learners - 4));
            more.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e; -fx-padding: 0 0 0 10;");
            roster.getChildren().add(more);
        }

        card.getChildren().addAll(top, avgRow, completion, meta, roster);
        return card;
    }
}
