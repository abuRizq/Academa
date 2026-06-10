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
 * Results screen — score hero + item-by-item review + cohort leaderboard.
 * Mirrors the React Results component exactly.
 */
public final class ResultsScreen extends VBox {

    private record ReviewItem(int n, String q, boolean correct, String type,
                              String your, String expected, String note) {}

    private record BoardEntry(int rank, String name, double score, boolean you) {}

    private static final List<ReviewItem> ITEMS = List.of(
        new ReviewItem(1, "Definition of matrix rank", true, "Single", null, null, null),
        new ReviewItem(2, "Determinant of a 2×2 matrix", true, "Completion", null, null, null),
        new ReviewItem(3, "Eigenvalue / eigenvector pairing", false, "Relational",
            "λ = 2 → v₂", "λ = 2 → v₁",
            "Check the characteristic polynomial — you transposed the vector indices."),
        new ReviewItem(4, "Order the Gram–Schmidt steps", true, "Sequencing", null, null, null),
        new ReviewItem(5, "Span of a vector set (multi)", false, "Multiple",
            "B, C", "B, C, D",
            "D is a linear combination of B and C — it belongs in the span.")
    );

    private static final List<BoardEntry> BOARD = List.of(
        new BoardEntry(1, "Ada Okafor", 96.2, false),
        new BoardEntry(2, "Leo Park", 95.1, false),
        new BoardEntry(3, "Mara Quinn", 94.6, true),
        new BoardEntry(4, "Sun Yi", 92.0, false),
        new BoardEntry(5, "Iris Bauer", 90.8, false),
        new BoardEntry(6, "Tomas Reyes", 89.3, false)
    );

    public ResultsScreen() {
        setSpacing(22);
        setPadding(new Insets(0));

        // === Hero ===
        getChildren().add(createHero());

        // === Body: review + leaderboard ===
        HBox body = new HBox(22);

        // Review
        VBox review = createReview();
        HBox.setHgrow(review, Priority.ALWAYS);

        // Leaderboard
        VBox board = createLeaderboard();
        board.setMinWidth(320);
        board.setPrefWidth(360);

        body.getChildren().addAll(review, board);
        getChildren().add(body);
    }

    private Node createHero() {
        HBox hero = new HBox(36);
        hero.getStyleClass().add("results-hero");
        hero.setAlignment(Pos.CENTER_LEFT);

        // Score section
        VBox scoreSection = new VBox(6);
        HBox.setHgrow(scoreSection, Priority.ALWAYS);

        Label eyebrow = new Label("RESOLVED · MATH 204 MIDTERM");
        eyebrow.getStyleClass().add("results-hero-eyebrow");

        HBox bigRow = new HBox(16);
        bigRow.setAlignment(Pos.CENTER_LEFT);
        Label pct = new Label("94.6%");
        pct.getStyleClass().add("results-hero-pct");
        AcBadge resolvedBadge = new AcBadge("A · Resolved", AcBadge.Variant.SUCCESS, AcBadge.Appearance.SOLID);
        bigRow.getChildren().addAll(pct, resolvedBadge);

        Label sub = new Label("17 of 20 items correct · evaluated in 0.4s");
        sub.getStyleClass().add("results-hero-sub");

        scoreSection.getChildren().addAll(eyebrow, bigRow, sub);

        // Breakdown
        VBox breakdown = new VBox(12);
        breakdown.setMinWidth(300);
        HBox.setHgrow(breakdown, Priority.ALWAYS);

        breakdown.getChildren().addAll(
            new AcProgressBar("Correct", 17, 20, AcProgressBar.Variant.SUCCESS,
                AcProgressBar.Size.MD, true, (v, m) -> (int)(double)v + "/" + (int)(double)m),
            new AcProgressBar("Partial credit", 1, 20, AcProgressBar.Variant.GOLD,
                AcProgressBar.Size.MD, true, (v, m) -> (int)(double)v + "/" + (int)(double)m),
            new AcProgressBar("Incorrect", 2, 20, AcProgressBar.Variant.DANGER,
                AcProgressBar.Size.MD, true, (v, m) -> (int)(double)v + "/" + (int)(double)m)
        );

        hero.getChildren().addAll(scoreSection, breakdown);
        return hero;
    }

    private VBox createReview() {
        // Legend
        HBox legend = new HBox(8);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setStyle("-fx-font-size: 11.5px;");

        javafx.scene.layout.Region dotOk = new javafx.scene.layout.Region();
        dotOk.getStyleClass().add("dot-ok");
        dotOk.setMinSize(8, 8); dotOk.setPrefSize(8, 8); dotOk.setMaxSize(8, 8);

        javafx.scene.layout.Region dotNo = new javafx.scene.layout.Region();
        dotNo.getStyleClass().add("dot-no");
        dotNo.setMinSize(8, 8); dotNo.setPrefSize(8, 8); dotNo.setMaxSize(8, 8);

        legend.getChildren().addAll(dotOk, new Label("Correct"), dotNo, new Label("Incorrect"));

        AcCard reviewCard = new AcCard("Item-by-item review", legend, "sm", false);
        VBox itemsBox = new VBox();

        for (ReviewItem it : ITEMS) {
            HBox row = new HBox(14);
            row.getStyleClass().add("ritem");
            row.setPadding(new Insets(16, 4, 16, 4));

            // Number
            Label num = new Label(String.valueOf(it.n));
            num.getStyleClass().addAll("ritem-num", it.correct ? "ritem-num-ok" : "ritem-num-no");
            num.setAlignment(Pos.CENTER);

            // Body
            VBox body = new VBox(6);
            HBox.setHgrow(body, Priority.ALWAYS);

            HBox mainRow = new HBox(10);
            mainRow.setAlignment(Pos.CENTER_LEFT);
            Label q = new Label(it.q);
            q.getStyleClass().add("ritem-q");
            HBox.setHgrow(q, Priority.ALWAYS);
            Label type = new Label(it.type.toUpperCase());
            type.getStyleClass().add("ritem-type");
            AcBadge badge = it.correct
                ? new AcBadge("Correct", AcBadge.Variant.SUCCESS, true)
                : new AcBadge("Incorrect", AcBadge.Variant.DANGER, true);
            mainRow.getChildren().addAll(q, type, badge);
            body.getChildren().add(mainRow);

            if (!it.correct && it.your != null) {
                HBox diff = new HBox(22);
                diff.setPadding(new Insets(8, 0, 0, 0));

                VBox yoursBox = new VBox(2);
                Label yoursLabel = new Label("YOUR ANSWER");
                yoursLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #9b2c2c;");
                Label yoursVal = new Label(it.your);
                yoursVal.getStyleClass().add("ritem-yours");
                yoursBox.getChildren().addAll(yoursLabel, yoursVal);

                VBox expBox = new VBox(2);
                Label expLabel = new Label("EXPECTED");
                expLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #2e6b4f;");
                Label expVal = new Label(it.expected);
                expVal.getStyleClass().add("ritem-expected");
                expBox.getChildren().addAll(expLabel, expVal);

                diff.getChildren().addAll(yoursBox, expBox);
                body.getChildren().add(diff);
            }

            if (it.note != null) {
                HBox note = new HBox(8);
                note.getStyleClass().add("ritem-note");
                note.setPadding(new Insets(10, 12, 10, 12));

                AcAvatar noteAvatar = new AcAvatar("Elaine Voss", AcAvatar.Size.XS);
                VBox noteText = new VBox();
                HBox.setHgrow(noteText, Priority.ALWAYS);
                Label noteContent = new Label("Dr. Voss — " + it.note);
                noteContent.setWrapText(true);
                noteContent.setStyle("-fx-font-size: 13px; -fx-text-fill: #3b3f4c;");
                noteText.getChildren().add(noteContent);

                note.getChildren().addAll(noteAvatar, noteText);
                body.getChildren().add(note);
            }

            row.getChildren().addAll(num, body);
            itemsBox.getChildren().add(row);
        }

        reviewCard.getBody().getChildren().add(itemsBox);

        VBox wrapper = new VBox();
        wrapper.getChildren().add(reviewCard);
        return wrapper;
    }

    private VBox createLeaderboard() {
        Label cap = new Label("142 learners");
        cap.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");

        AcCard boardCard = new AcCard("Cohort leaderboard", cap, "sm", false);

        VBox entries = new VBox(2);
        for (BoardEntry b : BOARD) {
            HBox row = new HBox(12);
            row.getStyleClass().add("brow");
            if (b.you) row.getStyleClass().add("brow-you");
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(9, 10, 9, 10));

            Label rank = new Label(String.valueOf(b.rank));
            rank.getStyleClass().addAll("brow-rank");
            if (b.rank <= 3) rank.getStyleClass().add("brow-rank-top");
            rank.setMinWidth(18);
            rank.setAlignment(Pos.CENTER);

            AcAvatar avatar = new AcAvatar(b.name, AcAvatar.Size.SM, b.rank == 1, null);

            Label name = new Label(b.name + (b.you ? " (you)" : ""));
            name.getStyleClass().add("brow-name");
            HBox.setHgrow(name, Priority.ALWAYS);

            if (b.rank == 1) {
                Node crown = AcIcon.create("crown", 15, Color.web("#b8893a"));
                row.getChildren().addAll(rank, avatar, name, crown);
            } else {
                row.getChildren().addAll(rank, avatar, name);
            }

            Label score = new Label(b.score + "%");
            score.getStyleClass().add("brow-score");
            row.getChildren().add(score);

            entries.getChildren().add(row);
        }

        AcButton moreBtn = new AcButton("View full ranking", AcButton.Variant.GHOST, AcButton.Size.SM);
        moreBtn.setFullWidth(true);
        moreBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(moreBtn, new Insets(8, 0, 0, 0));

        boardCard.getBody().getChildren().addAll(entries, moreBtn);

        VBox wrapper = new VBox();
        wrapper.getChildren().add(boardCard);
        return wrapper;
    }
}
