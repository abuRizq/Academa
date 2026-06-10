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
 * RankingsScreen — podium + full ranking table.
 * Works for both Learner and Educator (cohort) roles.
 * Mirrors React Rankings.jsx.
 */
public final class RankingsScreen extends VBox {

    private record Entry(int r, String name, String sub, double score, int move, boolean you, String done) {}

    private static final List<Entry> LEARNER_BOARD = List.of(
        new Entry(1, "Ada Okafor", "Grade 11 · B", 96.2, 0, false, "11/11"),
        new Entry(2, "Leo Park", "Grade 11 · A", 95.1, 2, false, "11/11"),
        new Entry(3, "Mara Quinn", "Grade 11 · A", 94.6, 1, true, "8/11"),
        new Entry(4, "Sun Yi", "Grade 10 · C", 92.0, -2, false, "10/11"),
        new Entry(5, "Iris Bauer", "Grade 11 · B", 90.8, 0, false, "11/11"),
        new Entry(6, "Tomas Reyes", "Grade 11 · A", 89.3, -1, false, "9/11"),
        new Entry(7, "Nadia Haaf", "Grade 11 · B", 88.1, 3, false, "10/11"),
        new Entry(8, "Owen Cole", "Grade 10 · C", 86.7, -1, false, "11/11")
    );

    private static final List<Entry> COHORT_BOARD = List.of(
        new Entry(1, "Grade 12 · Honors", "Computer Science · 24", 92.1, 1, false, "96%"),
        new Entry(2, "Grade 11 · A", "Mathematics · 32", 88.4, 0, false, "91%"),
        new Entry(3, "Grade 11 · B", "Mathematics · 30", 81.2, 2, false, "84%"),
        new Entry(4, "Grade 10 · C", "Sciences · 28", 76.8, -2, false, "79%"),
        new Entry(5, "Grade 11 · D", "Humanities · 31", 73.5, 0, false, "72%"),
        new Entry(6, "Grade 10 · A", "Languages · 29", 69.3, -1, false, "68%")
    );

    public RankingsScreen(String role) {
        setSpacing(22);
        boolean isLearner = "learner".equals(role);
        List<Entry> board = isLearner ? LEARNER_BOARD : COHORT_BOARD;

        // Toolbar
        HBox toolbar = PageKit.toolbar(
            new AcSelect(isLearner ? new String[]{"All cohorts", "Grade 11 · A", "Grade 11 · B"}
                : new String[]{"All subjects", "Mathematics", "Sciences"},
                isLearner ? "All cohorts" : "All subjects", "sm"),
            new AcSelect(new String[]{"Aggregate score", "Latest assessment", "Most improved"}, "Aggregate score", "sm"),
            PageKit.spacer(),
            isLearner
                ? new AcBadge("You · 3rd of 142", AcBadge.Variant.GOLD, AcBadge.Appearance.SOLID)
                : new AcButton("Export", AcButton.Variant.SECONDARY, AcButton.Size.SM,
                    AcIcon.create("download", 14, Color.web("#1b1d25")), null)
        );
        getChildren().add(toolbar);

        // Podium
        HBox podium = new HBox(20);
        podium.setAlignment(Pos.BOTTOM_CENTER);
        podium.setPadding(new Insets(20, 0, 20, 0));

        int[] podOrder = {1, 0, 2}; // 2nd, 1st, 3rd
        int[] podMedal = {2, 1, 3};
        for (int i = 0; i < 3; i++) {
            Entry b = board.get(podOrder[i]);
            int medal = podMedal[i];
            podium.getChildren().add(createPodiumEntry(b, medal, isLearner));
        }
        getChildren().add(podium);

        // Table
        AcCard tableCard = new AcCard(isLearner ? "Full ranking" : "Cohort standings",
            new Label(isLearner ? "142 learners" : "41 cohorts") {{
                setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");
            }}, "sm", false);

        VBox tableBody = tableCard.getBody();

        // Header
        HBox thead = new HBox();
        thead.setStyle("-fx-background-color: #f2f1ea; -fx-padding: 10 16 10 16; -fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        thead.setAlignment(Pos.CENTER_LEFT);
        for (String h : new String[]{"Rank", isLearner ? "Learner" : "Cohort", "Completion", "Movement", "Score"}) {
            Label hl = new Label(h);
            hl.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
            if ("Learner".equals(h) || "Cohort".equals(h)) HBox.setHgrow(hl, Priority.ALWAYS);
            else hl.setMinWidth(80);
            thead.getChildren().add(hl);
        }
        tableBody.getChildren().add(thead);

        // Rows
        for (Entry b : board) {
            HBox row = new HBox();
            row.setPadding(new Insets(10, 16, 10, 16));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;" +
                (b.you ? " -fx-background-color: #eaedfa;" : ""));

            // Rank
            HBox rankBox = new HBox(4);
            rankBox.setMinWidth(80);
            rankBox.setAlignment(Pos.CENTER_LEFT);
            Label rl = new Label(String.valueOf(b.r));
            rl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + (b.r <= 3 ? "#985f1f" : "#8c909e") + ";");
            rankBox.getChildren().add(rl);
            if (b.r == 1) rankBox.getChildren().add(AcIcon.create("crown", 14, Color.web("#d9b566")));

            // Entity
            HBox ent = new HBox(10);
            ent.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(ent, Priority.ALWAYS);
            ent.getChildren().add(new AcAvatar(b.name, AcAvatar.Size.SM, b.r == 1, null));
            VBox entTxt = new VBox();
            Label entN = new Label(b.name + (b.you ? " (you)" : ""));
            entN.setStyle("-fx-font-size: 14px; -fx-font-weight: " + (b.you ? "bold" : "500") +
                "; -fx-text-fill: " + (b.you ? "#2c40a0" : "#1b1d25") + ";");
            Label entS = new Label(b.sub);
            entS.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #8c909e;");
            entTxt.getChildren().addAll(entN, entS);
            ent.getChildren().add(entTxt);

            // Completion
            Label comp = new Label(b.done);
            comp.setMinWidth(80);
            comp.setStyle("-fx-font-size: 13px; -fx-text-fill: #6f7484;");

            // Movement
            HBox moveBox = new HBox();
            moveBox.setMinWidth(80);
            String dir = b.move == 0 ? "flat" : b.move > 0 ? "up" : "down";
            moveBox.getChildren().add(PageKit.trend(dir, String.valueOf(b.move)));

            // Score
            Label scoreL = new Label(b.score + "%");
            scoreL.setMinWidth(80);
            scoreL.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");

            row.getChildren().addAll(rankBox, ent, comp, moveBox, scoreL);
            tableBody.getChildren().add(row);
        }

        getChildren().add(tableCard);
    }

    private Node createPodiumEntry(Entry b, int medal, boolean isLearner) {
        StackPane root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);
        
        boolean isFirst = medal == 1;
        
        // Card Body
        VBox card = new VBox(isFirst ? 14 : 10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(isFirst ? 36 : 30, isFirst ? 40 : 30, isFirst ? 24 : 20, isFirst ? 40 : 30));
        card.setMinWidth(isFirst ? 260 : 220);
        
        if (isFirst) {
            card.setStyle("-fx-background-color: #14151b; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(20,21,27,0.15), 12, 0, 0, 6);");
        } else {
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e3e2db; -fx-border-width: 1; -fx-background-radius: 12; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(20,21,27,0.06), 6, 0, 0, 2);");
        }
        
        StackPane.setMargin(card, new Insets(14, 0, 0, 0));
        
        // Avatar
        AcAvatar avatar = new AcAvatar(b.name, isFirst ? AcAvatar.Size.LG : AcAvatar.Size.MD, isFirst, null);
        
        // Name
        Label name = new Label(b.name + (b.you ? " (you)" : ""));
        name.setStyle("-fx-font-size: " + (isFirst ? "15px" : "14px") + "; -fx-font-weight: 600; -fx-text-fill: " + (isFirst ? "white" : "#1b1d25") + ";");
        name.setWrapText(true);
        name.setMaxWidth(160);
        name.setAlignment(Pos.CENTER);

        // Subject
        Label sub = new Label(b.sub);
        sub.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #8c909e;");

        // Score
        Label score = new Label(b.score + "%");
        score.setStyle("-fx-font-size: " + (isFirst ? "24px" : "20px") + "; -fx-font-weight: bold; -fx-text-fill: " + (isFirst ? "#d9b566" : "#1b1d25") + ";");
        VBox.setMargin(score, new Insets(6, 0, 0, 0));
        
        // Crown for #1
        if (isFirst) {
            StackPane crownWrap = new StackPane();
            crownWrap.getChildren().add(AcIcon.create("crown", 18, Color.web("#d9b566")));
            card.getChildren().addAll(avatar, crownWrap, name, sub, score);
        } else {
            card.getChildren().addAll(avatar, name, sub, score);
        }
        
        // Medal Badge
        Label medalL = new Label(String.valueOf(medal));
        String mBgColor = switch (medal) {
            case 1 -> "#d9b566";
            case 2 -> "#8c909e";
            default -> "#7a5722";
        };
        medalL.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: white; " +
            "-fx-background-color: " + mBgColor + "; -fx-background-radius: 14;");
        medalL.setAlignment(Pos.CENTER);
        medalL.setMinSize(28, 28);
        medalL.setMaxSize(28, 28);
        
        root.getChildren().addAll(card, medalL);
        return root;
    }
}
