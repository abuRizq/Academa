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
 * AnalyticsScreen — analytics with score distribution, mean trajectory, performance bars, most-missed items.
 * Works for both institution (admin) and educator scope.
 * Mirrors React Analytics.jsx.
 */
public final class AnalyticsScreen extends VBox {

    private record DistItem(String label, int value, String tone, boolean hi) {}

    public AnalyticsScreen(String scope) {
        boolean isInst = "institution".equals(scope);
        setSpacing(22);

        // Toolbar
        getChildren().add(PageKit.toolbar(
            new AcSelect(isInst ? new String[]{"All departments", "Mathematics", "Sciences", "Humanities"}
                : new String[]{"All cohorts", "Grade 11 · A", "Grade 11 · B"},
                isInst ? "All departments" : "All cohorts", "sm"),
            new AcSelect(new String[]{"Autumn Term 2025", "Spring Term 2025", "Full year"}, "Autumn Term 2025", "sm"),
            PageKit.spacer(),
            new AcButton("Export report", AcButton.Variant.SECONDARY, AcButton.Size.SM,
                AcIcon.create("download", 14, Color.web("#1b1d25")), null)
        ));

        // Stats
        if (isInst) {
            getChildren().add(PageKit.statRow(
                PageKit.stat("target", "brand", "78.4%", "Institution mean", "+2.1 vs last term", "up", false),
                PageKit.stat("trending-up", "success", "91.2%", "Completion rate", "+0.8%", "up", false),
                PageKit.stat("gauge", "gold", "0.42", "Discrimination", "healthy band"),
                PageKit.stat("timer", "amber", "41m", "Median time/task", "−3m", "up", false)
            ));
        } else {
            getChildren().add(PageKit.statRow(
                PageKit.stat("target", "brand", "81.6%", "Class mean", "+4.0 vs cohort avg", "up", false),
                PageKit.stat("trending-up", "success", "94%", "Submission rate", "30 of 32"),
                PageKit.stat("gauge", "gold", "0.38", "Discrimination", "review 3 items", "down", false),
                PageKit.stat("timer", "amber", "37m", "Median time/task", "of 45m allotted")
            ));
        }

        // Charts row 1: Score distribution + Mean trajectory
        HBox row1 = new HBox(22);
        row1.getChildren().addAll(buildDistribution(isInst), buildTrajectory(isInst));
        getChildren().add(row1);

        // Charts row 2: Performance by department + Most-missed
        HBox row2 = new HBox(22);
        row2.getChildren().addAll(buildPerformance(isInst), buildMissed(isInst));
        getChildren().add(row2);
    }

    private AcCard buildDistribution(boolean isInst) {
        AcCard card = new AcCard(null, null, "lg", false);
        HBox.setHgrow(card, Priority.ALWAYS);

        VBox body = card.getBody();
        HBox head = chartHead("Score distribution",
            isInst ? "All graded submissions · this term" : "Linear Algebra — Midterm · 30 learners",
            new AcBadge(isInst ? "% of learners" : "learners", AcBadge.Variant.BRAND));
        body.getChildren().add(head);

        List<DistItem> dist = isInst ? List.of(
            new DistItem("<50", 4, "danger", false), new DistItem("50–59", 9, "amber", false),
            new DistItem("60–69", 18, "gold", false), new DistItem("70–79", 31, "", true),
            new DistItem("80–89", 26, "success", false), new DistItem("90+", 12, "success", false)
        ) : List.of(
            new DistItem("<50", 1, "danger", false), new DistItem("50–59", 2, "amber", false),
            new DistItem("60–69", 4, "gold", false), new DistItem("70–79", 8, "", true),
            new DistItem("80–89", 11, "success", false), new DistItem("90+", 6, "success", false)
        );

        double[][] data = dist.stream().map(d -> new double[]{d.value, d.hi ? 1 : 0}).toArray(double[][]::new);
        String[] labels = dist.stream().map(d -> d.label).toArray(String[]::new);
        body.getChildren().add(PageKit.columns(data, labels, 170));
        return card;
    }

    private AcCard buildTrajectory(boolean isInst) {
        AcCard card = new AcCard(null, null, "lg", false);
        HBox.setHgrow(card, Priority.ALWAYS);

        VBox body = card.getBody();
        HBox head = new HBox();
        head.setAlignment(Pos.CENTER_LEFT);
        VBox titles = new VBox();
        HBox.setHgrow(titles, Priority.ALWAYS);
        Label t = new Label("Mean trajectory");
        t.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label s = new Label("8-week rolling mean");
        s.setStyle("-fx-font-size: 13px; -fx-text-fill: #6f7484;");
        titles.getChildren().addAll(t, s);
        Label big = new Label(isInst ? "78.4%" : "81.6%");
        big.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        head.getChildren().addAll(titles, big);
        body.getChildren().add(head);

        // Sparkline as simple bar representation
        double[] trend = isInst ? new double[]{71,73,72,75,74,76,78,78.4} : new double[]{74,76,75,79,80,78,81,81.6};
        double[][] trendData = new double[trend.length][];
        String[] trendLabels = new String[trend.length];
        for (int i = 0; i < trend.length; i++) {
            trendData[i] = new double[]{trend[i], i == trend.length - 1 ? 1 : 0};
            trendLabels[i] = "W" + (i + 1);
        }
        body.getChildren().add(PageKit.columns(trendData, trendLabels, 120));

        // Legend
        HBox legend = new HBox(6);
        legend.setPadding(new Insets(8, 0, 0, 0));
        legend.getChildren().add(PageKit.trend("up", (isInst ? "+2.1 pts" : "+4.0 pts") + " vs " + (isInst ? "last term" : "cohort average")));
        body.getChildren().add(legend);
        return card;
    }

    private AcCard buildPerformance(boolean isInst) {
        AcCard card = new AcCard(null, null, "lg", false);
        HBox.setHgrow(card, Priority.ALWAYS);

        VBox body = card.getBody();
        body.getChildren().add(chartHead(isInst ? "Performance by department" : "Performance by unit", "Mean score, sorted", null));

        record SubjectRow(String label, int value) {}
        List<SubjectRow> subjects = isInst ? List.of(
            new SubjectRow("Mathematics", 88), new SubjectRow("Computer Science", 85),
            new SubjectRow("Sciences", 82), new SubjectRow("Humanities", 76),
            new SubjectRow("Languages", 69), new SubjectRow("Arts", 73)
        ) : List.of(
            new SubjectRow("Linear Algebra", 84), new SubjectRow("Calculus II", 79),
            new SubjectRow("Discrete Math", 88), new SubjectRow("Statistics", 72),
            new SubjectRow("Number Theory", 81)
        );

        VBox bars = new VBox(12);
        for (SubjectRow s : subjects) {
            String tone = s.value >= 85 ? "success" : s.value >= 75 ? "gold" : "amber";
            bars.getChildren().add(PageKit.barRow(s.label, s.value, 100, tone, s.value + "%", null));
        }
        body.getChildren().add(bars);
        return card;
    }

    private AcCard buildMissed(boolean isInst) {
        AcCard card = new AcCard(null, null, "lg", false);
        HBox.setHgrow(card, Priority.ALWAYS);

        VBox body = card.getBody();
        HBox head = chartHead("Most-missed items",
            isInst ? "Across all assessments" : "This assessment",
            new AcBadge(isInst ? "needs attention" : "reteach", AcBadge.Variant.DANGER));
        body.getChildren().add(head);

        record MissedRow(String title, int pct) {}
        List<MissedRow> missed = isInst ? List.of(
            new MissedRow("Bayesian inference setup", 61),
            new MissedRow("Vector space axioms", 54),
            new MissedRow("Thermodynamic cycles", 49),
            new MissedRow("Rhetorical fallacy ID", 44),
            new MissedRow("Stoichiometric ratios", 38)
        ) : List.of(
            new MissedRow("Eigenvalue / eigenvector pairing", 58),
            new MissedRow("Span of a vector set", 47),
            new MissedRow("Gram–Schmidt ordering", 39),
            new MissedRow("Determinant cofactor expansion", 31),
            new MissedRow("Matrix rank definition", 22)
        );

        VBox rows = new VBox(10);
        for (int i = 0; i < missed.size(); i++) {
            MissedRow m = missed.get(i);
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            Label rank = new Label(String.valueOf(i + 1));
            rank.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
            rank.setMinWidth(18);
            Label title = new Label(m.title);
            title.setStyle("-fx-font-size: 13px; -fx-text-fill: #3b3f4c;");
            title.setMinWidth(180);
            VBox barBox = new VBox();
            HBox.setHgrow(barBox, Priority.ALWAYS);
            barBox.getChildren().add(PageKit.barRow(null, m.pct, 100, "danger", null, null));
            Label pct = new Label(m.pct + "%");
            pct.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #9b2c2c;");
            pct.setMinWidth(40);
            row.getChildren().addAll(rank, title, barBox, pct);
            rows.getChildren().add(row);
        }
        body.getChildren().add(rows);
        return card;
    }

    private HBox chartHead(String title, String sub, Node right) {
        HBox head = new HBox();
        head.setAlignment(Pos.CENTER_LEFT);
        head.setPadding(new Insets(0, 0, 12, 0));
        VBox titles = new VBox();
        HBox.setHgrow(titles, Priority.ALWAYS);
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label s = new Label(sub);
        s.setStyle("-fx-font-size: 13px; -fx-text-fill: #6f7484;");
        titles.getChildren().addAll(t, s);
        head.getChildren().add(titles);
        if (right != null) head.getChildren().add(right);
        return head;
    }
}
