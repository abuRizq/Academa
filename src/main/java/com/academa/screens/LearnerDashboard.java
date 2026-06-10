package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.function.Predicate;

/**
 * LearnerDashboard — cohort standing + assessment availability matrix.
 * Mirrors the React LearnerDashboard component exactly.
 */
public final class LearnerDashboard extends VBox {

    private record Assessment(String id, String course, String title, int items,
                              int mins, String status, double progress, String due,
                              String kind, int score) {}

    private static final List<Assessment> ASSESSMENTS = List.of(
        new Assessment("alg", "MATH 204", "Linear Algebra — Midterm", 20, 90, "available", 0, "Due today · 17:00", "Summative", 0),
        new Assessment("bio", "BIO 110", "Cellular Respiration", 15, 45, "in-progress", 60, "Closes in 2d", "Formative", 0),
        new Assessment("hist", "HIST 230", "The Interwar Period", 24, 60, "available", 0, "Opens · now", "Summative", 0),
        new Assessment("chem", "CHEM 101", "Stoichiometry Drill", 12, 30, "locked", 0, "Opens Mon 09:00", "Diagnostic", 0),
        new Assessment("eng", "ENG 215", "Rhetoric & Argument", 18, 50, "completed", 0, "Resolved · 2d ago", "Summative", 91),
        new Assessment("cs", "CS 150", "Recursion & Trees", 16, 40, "completed", 0, "Resolved · 5d ago", "Formative", 97)
    );

    private FlowPane cardsPane;
    private String currentTab = "available";
    private final Runnable onBegin;

    public LearnerDashboard(Runnable onBegin) {
        this.onBegin = onBegin;
        setSpacing(24);
        setPadding(new Insets(0));

        // === Cohort standing ===
        getChildren().add(createStanding());

        // === Availability matrix ===
        getChildren().add(createMatrix());
    }

    private Node createStanding() {
        HBox standing = new HBox();
        standing.getStyleClass().add("dash-standing");

        // Main stats
        VBox main = new VBox();
        main.setPadding(new Insets(26, 30, 26, 30));
        main.setSpacing(6);
        HBox.setHgrow(main, Priority.ALWAYS);

        Label eyebrow = new Label("COHORT STANDING · AUTUMN TERM");
        eyebrow.getStyleClass().add("dash-standing-eyebrow");
        eyebrow.setStyle("-fx-text-fill: #d9b566;");

        HBox rankRow = new HBox(16);
        rankRow.setAlignment(Pos.BASELINE_LEFT);
        rankRow.setPadding(new Insets(6, 0, 18, 0));

        Label rank = new Label("3");
        rank.getStyleClass().add("dash-standing-rank");
        rank.setStyle("-fx-text-fill: white; -fx-font-size: 58px; -fx-font-weight: bold;");
        Label rankSup = new Label("rd");
        rankSup.getStyleClass().add("dash-standing-rank-sup");
        rankSup.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        HBox rankCombo = new HBox(0);
        rankCombo.setAlignment(Pos.BASELINE_LEFT);
        rankCombo.getChildren().addAll(rank, rankSup);

        VBox ofBox = new VBox(8);
        Label ofLabel = new Label("of 142 learners");
        ofLabel.getStyleClass().add("dash-standing-of");
        ofLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.6);");
        AcBadge topBadge = new AcBadge("Top 5%", AcBadge.Variant.GOLD, AcBadge.Appearance.SOLID);
        ofBox.getChildren().addAll(ofLabel, topBadge);

        rankRow.getChildren().addAll(rankCombo, ofBox);

        // Progress bars
        VBox meters = new VBox(14);
        meters.getStyleClass().add("dark-progress");

        AcProgressBar aggScore = new AcProgressBar("Aggregate score", 94.6, 100,
            AcProgressBar.Variant.GOLD, AcProgressBar.Size.MD, true,
            (v, m) -> v + "%");

        AcProgressBar termComp = new AcProgressBar("Term completion", 8, 11,
            AcProgressBar.Variant.BRAND, AcProgressBar.Size.MD, true,
            (v, m) -> (int)(double)v + "/" + (int)(double)m);

        meters.getChildren().addAll(aggScore, termComp);
        main.getChildren().addAll(eyebrow, rankRow, meters);

        // Leaders panel
        VBox leaders = new VBox(8);
        leaders.getStyleClass().add("dash-standing-leaders");
        leaders.setMinWidth(240);
        leaders.setPrefWidth(280);

        Label leadCap = new Label("JUST AHEAD");
        leadCap.getStyleClass().add("dash-leadcap");
        leadCap.setStyle("-fx-text-fill: rgba(255,255,255,0.45);");
        leaders.getChildren().add(leadCap);

        leaders.getChildren().add(createLeadRow("1", "Ada Okafor", "96.2%", 1, false));
        leaders.getChildren().add(createLeadRow("2", "Leo Park", "95.1%", 2, false));

        HBox youRow = createLeadRow("3", "You", "94.6%", 3, true);
        youRow.getStyleClass().add("dash-leadrow-you");
        leaders.getChildren().add(youRow);

        standing.getChildren().addAll(main, leaders);
        return standing;
    }

    private HBox createLeadRow(String rankText, String name, String score, int rank, boolean isYou) {
        HBox row = new HBox(11);
        row.getStyleClass().add("dash-leadrow");
        row.setAlignment(Pos.CENTER_LEFT);

        Label r = new Label(rankText);
        r.getStyleClass().add("dash-leadrow-rank");
        r.setStyle("-fx-text-fill: rgba(255,255,255,0.5);");
        r.setMinWidth(14);

        AcAvatar avatar = new AcAvatar(isYou ? "Mara Quinn" : name, AcAvatar.Size.SM, rank == 1, null);

        Label n = new Label(name);
        n.getStyleClass().add("dash-leadrow-name");
        n.setStyle("-fx-text-fill: " + (isYou ? "#e8d199" : "white") + ";");
        HBox.setHgrow(n, Priority.ALWAYS);

        Label s = new Label(score);
        s.getStyleClass().add("dash-leadrow-score");
        s.setStyle("-fx-text-fill: " + (isYou ? "#e8d199" : "white") + ";");

        row.getChildren().addAll(r, avatar, n, s);
        return row;
    }

    private Node createMatrix() {
        VBox matrix = new VBox(16);

        // Head
        HBox head = new HBox();
        head.setAlignment(Pos.CENTER_LEFT);

        Label h2 = new Label("Availability matrix");
        h2.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        HBox.setHgrow(h2, Priority.ALWAYS);

        AcTabs tabs = new AcTabs(List.of(
            new AcTabs.TabDef("available", "Available", 3),
            new AcTabs.TabDef("completed", "Completed", 2),
            new AcTabs.TabDef("all", "All")
        ), "available", id -> {
            currentTab = id;
            refreshCards();
        }, AcTabs.Variant.PILL);

        head.getChildren().addAll(h2, tabs);
        matrix.getChildren().add(head);

        // Cards
        cardsPane = new FlowPane(16, 16);
        cardsPane.setPrefWrapLength(900);
        matrix.getChildren().add(cardsPane);

        refreshCards();
        return matrix;
    }

    private void refreshCards() {
        cardsPane.getChildren().clear();
        Predicate<Assessment> filter = switch (currentTab) {
            case "available" -> a -> "available".equals(a.status) || "in-progress".equals(a.status);
            case "completed" -> a -> "completed".equals(a.status);
            default -> a -> true;
        };

        ASSESSMENTS.stream().filter(filter).forEach(a -> {
            VBox card = new VBox();
            card.getStyleClass().addAll("ac-card", "acard");
            card.setMinWidth(280);
            card.setPrefWidth(320);
            card.setMaxWidth(380);
            card.setPadding(new Insets(20));
            card.setSpacing(12);

            // Top: course + badge
            HBox top = new HBox();
            top.setAlignment(Pos.CENTER_LEFT);
            Label course = new Label(a.course);
            course.getStyleClass().add("acard-course");
            HBox.setHgrow(course, Priority.ALWAYS);
            Node badge = statusBadge(a);
            top.getChildren().addAll(course, badge);
            card.getChildren().add(top);

            // Title
            Label title = new Label(a.title);
            title.getStyleClass().add("acard-title");
            title.setWrapText(true);
            card.getChildren().add(title);

            // Meta
            HBox meta = new HBox(14);
            meta.getStyleClass().add("acard-meta");
            meta.setAlignment(Pos.CENTER_LEFT);

            HBox itemsMeta = new HBox(5);
            itemsMeta.setAlignment(Pos.CENTER_LEFT);
            itemsMeta.getChildren().addAll(AcIcon.create("list-checks", 14, Color.web("#8c909e")),
                new Label(a.items + " items"));
            HBox minsMeta = new HBox(5);
            minsMeta.setAlignment(Pos.CENTER_LEFT);
            minsMeta.getChildren().addAll(AcIcon.create("timer", 14, Color.web("#8c909e")),
                new Label(a.mins + " min"));
            Label kind = new Label(a.kind.toUpperCase());
            kind.getStyleClass().add("acard-kind");
            HBox.setHgrow(kind, Priority.ALWAYS);
            kind.setAlignment(Pos.CENTER_RIGHT);

            meta.getChildren().addAll(itemsMeta, minsMeta, kind);
            card.getChildren().add(meta);

            // In-progress bar
            if ("in-progress".equals(a.status)) {
                AcProgressBar prog = new AcProgressBar(null, a.progress, 100,
                    AcProgressBar.Variant.BRAND, AcProgressBar.Size.SM, false, null);
                card.getChildren().add(prog);
            }

            // Completed score
            if ("completed".equals(a.status)) {
                HBox scoreBox = new HBox(8);
                scoreBox.setAlignment(Pos.BASELINE_LEFT);
                Label scoreVal = new Label(a.score + "%");
                scoreVal.getStyleClass().add("acard-score-val");
                Label scoreLbl = new Label("resolved score");
                scoreLbl.getStyleClass().add("acard-score-lbl");
                scoreBox.getChildren().addAll(scoreVal, scoreLbl);
                card.getChildren().add(scoreBox);
            }

            // Footer
            HBox foot = new HBox(10);
            foot.getStyleClass().add("acard-foot");
            foot.setAlignment(Pos.CENTER_LEFT);
            Label due = new Label(a.due);
            due.getStyleClass().add("acard-due");
            HBox.setHgrow(due, Priority.ALWAYS);

            AcButton actionBtn = switch (a.status) {
                case "available" -> {
                    AcButton btn = new AcButton("Begin", AcButton.Variant.PRIMARY, AcButton.Size.SM,
                        null, AcIcon.create("arrow-right", 13, Color.WHITE));
                    btn.setOnAction(e -> { if (onBegin != null) onBegin.run(); });
                    yield btn;
                }
                case "in-progress" -> {
                    AcButton btn = new AcButton("Resume", AcButton.Variant.PRIMARY, AcButton.Size.SM);
                    btn.setOnAction(e -> { if (onBegin != null) onBegin.run(); });
                    yield btn;
                }
                case "completed" -> new AcButton("Review", AcButton.Variant.SECONDARY, AcButton.Size.SM);
                default -> {
                    AcButton btn = new AcButton("Locked", AcButton.Variant.GHOST, AcButton.Size.SM,
                        AcIcon.create("lock", 13, Color.web("#8c909e")), null);
                    btn.setDisable(true);
                    yield btn;
                }
            };

            foot.getChildren().addAll(due, actionBtn);
            card.getChildren().add(foot);

            cardsPane.getChildren().add(card);
        });
    }

    private Node statusBadge(Assessment a) {
        return switch (a.status) {
            case "available" -> new AcBadge("Available now", AcBadge.Variant.BRAND, true);
            case "in-progress" -> new AcBadge("In progress", AcBadge.Variant.WARNING, true);
            case "locked" -> new AcBadge("Locked", AcBadge.Variant.NEUTRAL);
            default -> new AcBadge("Resolved", AcBadge.Variant.SUCCESS, true);
        };
    }
}
