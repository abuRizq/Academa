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
 * EducatorAssessments — tabbed assessment table with filtering.
 * Mirrors React EducatorAssessments.jsx.
 */
public final class EducatorAssessments extends VBox {

    private record Row(String title, String meta, String cohort, String status,
                       int subs, int total, Double avg, String opens) {}

    private static final List<Row> ROWS = List.of(
        new Row("Linear Algebra — Midterm", "MATH 204 · 20 items · 32 pts", "Grade 11 · A", "grading", 30, 32, 81.6, null),
        new Row("Vector Spaces — Quiz", "MATH 204 · 12 items · 24 pts", "Grade 11 · A", "scheduled", 0, 32, null, "Oct 21"),
        new Row("Cellular Respiration", "BIO 110 · 15 items · 30 pts", "Grade 10 · C", "live", 18, 28, null, null),
        new Row("Stoichiometry Drill", "CHEM 101 · 12 items · 18 pts", "Grade 11 · D", "live", 9, 31, null, null),
        new Row("Diagnostic — Number Theory", "MATH 204 · 16 items · 28 pts", "Grade 11 · A", "draft", 0, 32, null, null),
        new Row("Recursion & Trees", "CS 150 · 16 items · 32 pts", "Grade 11 · B", "closed", 30, 30, 88.9, null),
        new Row("Rhetoric & Argument", "ENG 215 · 18 items · 36 pts", "Grade 11 · B", "closed", 29, 30, 84.2, null)
    );

    private String activeTab = "all";
    private final Runnable onOpen;

    public EducatorAssessments(Runnable onOpen) {
        this.onOpen = onOpen;
        setSpacing(16);
        buildUI();
    }

    private void buildUI() {
        getChildren().clear();

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 8, 0));

        // Tabs
        HBox tabs = new HBox(4);
        tabs.setStyle("-fx-background-color: #f2f1ea; -fx-background-radius: 9; -fx-padding: 3;");
        for (String[] tab : new String[][]{{"all","All","7"},{"live","Live","2"},{"grading","Grading","1"},{"draft","Drafts","1"}}) {
            HBox pill = new HBox(6);
            pill.setAlignment(Pos.CENTER);
            pill.setPadding(new Insets(6, 12, 6, 12));
            pill.setCursor(javafx.scene.Cursor.HAND);
            boolean active = tab[0].equals(activeTab);
            pill.setStyle("-fx-background-radius: 7;" + (active ? " -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 2, 0, 0, 1);" : ""));
            Label l = new Label(tab[1]);
            l.setStyle("-fx-font-size: 12px; -fx-font-weight: " + (active ? "bold" : "500") +
                "; -fx-text-fill: " + (active ? "#2c40a0" : "#565a68") + ";");
            Label c = new Label(tab[2]);
            c.setStyle("-fx-font-size: 10px; -fx-text-fill: " + (active ? "#2c40a0" : "#8c909e") +
                "; -fx-background-color: " + (active ? "#eaedfa" : "#e8e7df") + "; -fx-background-radius: 999; -fx-padding: 1 5 1 5;");
            pill.getChildren().addAll(l, c);
            pill.setOnMouseClicked(e -> { activeTab = tab[0]; buildUI(); });
            tabs.getChildren().add(pill);
        }

        Region spacer = PageKit.spacer();
        AcInput search = new AcInput(null, "text", null, "Search assessments…",
            AcIcon.create("search", 14, Color.web("#8c909e")), null, "sm", false);
        search.setMaxWidth(220);
        AcButton newBtn = new AcButton("New assessment", AcButton.Variant.PRIMARY, AcButton.Size.SM,
            AcIcon.create("plus", 14, Color.WHITE), null);
        toolbar.getChildren().addAll(tabs, spacer, search, newBtn);
        getChildren().add(toolbar);

        // Table
        AcCard tableCard = new AcCard(null, null, "sm", false);
        VBox body = tableCard.getBody();

        // Header
        HBox thead = createTableHeader();
        body.getChildren().add(thead);

        // Filter rows
        List<Row> visible = ROWS.stream()
            .filter(r -> "all".equals(activeTab) || r.status.equals(activeTab))
            .toList();

        for (Row r : visible) {
            body.getChildren().add(createTableRow(r));
        }
        getChildren().add(tableCard);
    }

    private HBox createTableHeader() {
        HBox thead = new HBox();
        thead.setStyle("-fx-background-color: #f2f1ea; -fx-padding: 10 16 10 16; -fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        thead.setAlignment(Pos.CENTER_LEFT);
        String[] headers = {"Assessment", "Cohort", "Status", "Submissions", "Mean", ""};
        double[] widths = {0, 120, 90, 140, 70, 40};
        for (int i = 0; i < headers.length; i++) {
            Label h = new Label(headers[i]);
            h.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
            if (i == 0) HBox.setHgrow(h, Priority.ALWAYS);
            else h.setMinWidth(widths[i]);
            thead.getChildren().add(h);
        }
        return thead;
    }

    private HBox createTableRow(Row r) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0; -fx-cursor: hand;");
        row.setCursor(javafx.scene.Cursor.HAND);
        row.setOnMouseClicked(e -> { if (onOpen != null) onOpen.run(); });

        // Assessment
        VBox assCol = new VBox();
        HBox.setHgrow(assCol, Priority.ALWAYS);
        Label t = new Label(r.title);
        t.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label m = new Label(r.meta);
        m.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
        assCol.getChildren().addAll(t, m);

        // Cohort
        Label cohort = new Label(r.cohort);
        cohort.setMinWidth(120);
        cohort.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #6f7484;");

        // Status
        Node badge = switch (r.status) {
            case "live" -> new AcBadge("Live", AcBadge.Variant.SUCCESS, true);
            case "grading" -> new AcBadge("Grading", AcBadge.Variant.WARNING, true);
            case "scheduled" -> new AcBadge("Scheduled", AcBadge.Variant.BRAND, true);
            case "draft" -> new AcBadge("Draft", AcBadge.Variant.NEUTRAL);
            default -> new AcBadge("Closed", AcBadge.Variant.NEUTRAL);
        };
        HBox statusBox = new HBox(badge);
        statusBox.setMinWidth(90);

        // Submissions
        HBox subsCol = new HBox(6);
        subsCol.setMinWidth(140);
        subsCol.setAlignment(Pos.CENTER_LEFT);
        if ("draft".equals(r.status) || "scheduled".equals(r.status)) {
            Label noSub = new Label(r.opens != null ? "Opens " + r.opens : "Not opened");
            noSub.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
            subsCol.getChildren().add(noSub);
        } else {
            AcProgressBar pb = new AcProgressBar(null, r.subs, r.total,
                "live".equals(r.status) ? AcProgressBar.Variant.SUCCESS : AcProgressBar.Variant.BRAND,
                AcProgressBar.Size.SM, false, null);
            pb.setMaxWidth(80);
            Label subN = new Label(r.subs + "/" + r.total);
            subN.setStyle("-fx-font-size: 12px; -fx-text-fill: #6f7484;");
            subsCol.getChildren().addAll(pb, subN);
        }

        // Mean
        Label avg = new Label(r.avg != null ? r.avg + "%" : "—");
        avg.setMinWidth(70);
        String avgColor = r.avg != null ? (r.avg >= 85 ? "#2e6b4f" : r.avg >= 75 ? "#985f1f" : "#9b2c2c") : "#8c909e";
        avg.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + avgColor + ";");

        // Edit
        AcButton editBtn = new AcButton("", AcButton.Variant.GHOST, AcButton.Size.SM,
            AcIcon.create("pencil", 14, Color.web("#565a68")), null);
        editBtn.setMinWidth(40);

        row.getChildren().addAll(assCol, cohort, statusBox, subsCol, avg, editBtn);
        return row;
    }
}
