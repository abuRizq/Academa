package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * EducatorDashboard — educator workspace with grading queue, live sessions, upcoming, and class pulse.
 * Mirrors React EducatorDashboard.jsx.
 */
public final class EducatorDashboard extends VBox {

    private record GradeItem(String title, String meta, String count, String icon) {}
    private static final List<GradeItem> GRADE = List.of(
        new GradeItem("Linear Algebra — Midterm", "MATH 204 · Grade 11 · A", "8 essays", "file-pen-line"),
        new GradeItem("Rhetoric & Argument", "ENG co-grade · Grade 11 · B", "3 free-text", "pen-line"),
        new GradeItem("Kinematics Unit Test", "PHYS 140 · flagged review", "2 flags", "flag")
    );

    private record LiveItem(String name, String meta, int pct) {}
    private static final List<LiveItem> LIVE = List.of(
        new LiveItem("Cellular Respiration", "Grade 10 · C · 18 active", 64),
        new LiveItem("Stoichiometry Drill", "Grade 11 · D · 9 active", 41)
    );

    private record UpcomingItem(String d, String m, String title, String meta) {}
    private static final List<UpcomingItem> UPCOMING = List.of(
        new UpcomingItem("17", "Oct", "The Interwar Period", "HIST 230 · opens 09:00"),
        new UpcomingItem("21", "Oct", "Vector Spaces — Quiz", "MATH 204 · scheduled"),
        new UpcomingItem("24", "Oct", "Diagnostic — Number Theory", "MATH 204 · draft")
    );

    public EducatorDashboard() {
        setSpacing(22);

        // Stats row
        getChildren().add(PageKit.statRow(
            PageKit.stat("inbox", "amber", "13", "Items to grade", "across 3 assessments"),
            PageKit.stat("radio", "success", "27", "Learners testing now", "2 live windows", null, true),
            PageKit.stat("target", "brand", "81.6%", "Class mean", "+4.0 vs cohort", "up", false),
            PageKit.stat("users", "gold", "62", "Learners taught", "2 cohorts")
        ));

        // Two-column
        HBox cols = new HBox(22);

        // Grading queue
        AcCard gradeCard = new AcCard("To grade",
            new AcButton("Open queue", AcButton.Variant.GHOST, AcButton.Size.SM,
                null, AcIcon.create("arrow-right", 14, Color.web("#3b3f4c"))), "sm", false);
        HBox.setHgrow(gradeCard, Priority.ALWAYS);

        VBox gradeList = new VBox();
        for (GradeItem g : GRADE) {
            HBox row = new HBox(12);
            row.setPadding(new Insets(12, 0, 12, 0));
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
            row.setAlignment(Pos.CENTER_LEFT);

            StackPane iconBox = new StackPane();
            iconBox.setMinSize(36, 36); iconBox.setPrefSize(36, 36); iconBox.setMaxSize(36, 36);
            iconBox.setStyle("-fx-background-color: #fef6e6; -fx-background-radius: 9;");
            iconBox.setAlignment(Pos.CENTER);
            iconBox.getChildren().add(AcIcon.create(g.icon, 17, Color.web("#b8762a")));

            VBox txt = new VBox();
            HBox.setHgrow(txt, Priority.ALWAYS);
            Label t = new Label(g.title);
            t.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
            Label m = new Label(g.meta);
            m.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
            txt.getChildren().addAll(t, m);

            Label cnt = new Label(g.count);
            cnt.setStyle("-fx-font-size: 12px; -fx-text-fill: #6f7484;");

            AcIconButton btn = new AcIconButton(AcIcon.create("arrow-right", 15, Color.web("#565a68")),
                "Grade", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM);

            row.getChildren().addAll(iconBox, txt, cnt, btn);
            gradeList.getChildren().add(row);
        }
        gradeCard.getBody().getChildren().add(gradeList);

        // Side: Live + Upcoming
        VBox side = new VBox(16);
        side.setMinWidth(300);
        side.setPrefWidth(320);

        // Live
        AcCard liveCard = new AcCard("Live now",
            new AcBadge("2 windows", AcBadge.Variant.SUCCESS, true), "sm", false);
        VBox liveList = new VBox(10);
        for (LiveItem l : LIVE) {
            VBox item = new VBox(4);
            item.setPadding(new Insets(8, 0, 8, 0));
            Label n = new Label(l.name);
            n.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
            Label meta = new Label(l.meta);
            meta.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
            AcProgressBar pb = new AcProgressBar(null, l.pct, 100,
                AcProgressBar.Variant.SUCCESS, AcProgressBar.Size.SM, false, null);
            item.getChildren().addAll(n, meta, pb);
            liveList.getChildren().add(item);
        }
        liveCard.getBody().getChildren().add(liveList);

        // Upcoming
        AcCard upCard = new AcCard("Upcoming", null, "sm", false);
        VBox upList = new VBox();
        for (UpcomingItem u : UPCOMING) {
            HBox row = new HBox(12);
            row.setPadding(new Insets(10, 0, 10, 0));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");

            VBox dateBox = new VBox();
            dateBox.setAlignment(Pos.CENTER);
            dateBox.setMinWidth(32);
            Label dd = new Label(u.d);
            dd.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
            Label mm = new Label(u.m);
            mm.setStyle("-fx-font-size: 10px; -fx-text-fill: #8c909e;");
            dateBox.getChildren().addAll(dd, mm);

            VBox txt = new VBox();
            HBox.setHgrow(txt, Priority.ALWAYS);
            Label t = new Label(u.title);
            t.setStyle("-fx-font-size: 13.5px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
            Label m = new Label(u.meta);
            m.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
            txt.getChildren().addAll(t, m);

            row.getChildren().addAll(dateBox, txt);
            upList.getChildren().add(row);
        }
        upCard.getBody().getChildren().add(upList);

        side.getChildren().addAll(liveCard, upCard);
        cols.getChildren().addAll(gradeCard, side);
        getChildren().add(cols);

        // Class pulse chart
        AcCard pulseCard = new AcCard(null, null, "lg", false);
        VBox pulseBody = pulseCard.getBody();

        HBox pulseHead = new HBox();
        pulseHead.setAlignment(Pos.CENTER_LEFT);
        VBox pTitles = new VBox();
        HBox.setHgrow(pTitles, Priority.ALWAYS);
        Label pTitle = new Label("Class pulse");
        pTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label pSub = new Label("Mean score on the last 6 graded assessments · MATH 204");
        pSub.setStyle("-fx-font-size: 13px; -fx-text-fill: #6f7484;");
        pTitles.getChildren().addAll(pTitle, pSub);

        Label big = new Label("85%");
        big.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        pulseHead.getChildren().addAll(pTitles, big);

        // Column chart
        double[][] pulseData = {{74,0},{78,0},{71,0},{83,0},{80,0},{85,1}};
        String[] pulseLabels = {"Mon","Tue","Wed","Thu","Fri","Mon"};
        HBox chart = PageKit.columns(pulseData, pulseLabels, 150);

        pulseBody.getChildren().addAll(pulseHead, chart);
        getChildren().add(pulseCard);
    }
}
