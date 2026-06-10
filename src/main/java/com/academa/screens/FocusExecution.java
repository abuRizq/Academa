package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * FocusExecution — full-screen assessment-taking environment.
 * Mirrors the React FocusExecution component exactly.
 */
public final class FocusExecution extends VBox {

    private static final int TOTAL = 20;

    /** Per-question data */
    private record QuestionData(String prompt, String type, String[] options) {}

    private static final QuestionData[] QUESTIONS = new QuestionData[TOTAL];
    static {
        // Question 8 (index 7) is the displayed default
        QUESTIONS[7] = new QuestionData(
            "In linear algebra, the rank of an m × n matrix is defined as which of the following?",
            "Single selection",
            new String[]{
                "The set of all linear combinations of the column vectors",
                "The dimension of the null space of the matrix",
                "The maximum number of linearly independent column vectors",
                "The product of the matrix and its transpose",
            }
        );
        // Fill remaining with realistic questions
        String[][] otherQs = {
            {"What is the determinant of the identity matrix I₃?", "Single selection",
             "0", "1", "3", "Undefined"},
            {"Which of the following is NOT a property of matrix multiplication?", "Single selection",
             "Associative", "Commutative", "Distributive", "Compatible dimensions required"},
            {"A matrix A is invertible if and only if:", "Single selection",
             "det(A) = 0", "det(A) ≠ 0", "A is symmetric", "A has more rows than columns"},
            {"The null space of a matrix A consists of:", "Single selection",
             "All vectors x such that Ax = 0", "All column vectors of A",
             "The pivot columns of A", "The eigenvalues of A"},
            {"If λ is an eigenvalue of A, then:", "Single selection",
             "A − λI is invertible", "det(A − λI) = 0",
             "A + λI = 0", "λ must be positive"},
            {"The trace of a matrix is:", "Single selection",
             "The product of diagonal entries", "The sum of diagonal entries",
             "The determinant", "The rank"},
            {"Two matrices are similar if:", "Single selection",
             "They have the same size", "B = P⁻¹AP for some invertible P",
             "They commute", "They have the same entries"},
            {"The dimension of the column space of a matrix equals:", "Single selection",
             "The number of columns", "The number of rows",
             "The rank", "The nullity"},
            {"An orthogonal matrix Q satisfies:", "Single selection",
             "Q² = I", "QᵀQ = I",
             "Q = Qᵀ", "det(Q) = 0"},
            {"The Gram–Schmidt process produces:", "Single selection",
             "Eigenvalues", "An orthonormal basis",
             "The determinant", "A diagonal matrix"},
            {"A symmetric matrix always has:", "Single selection",
             "Complex eigenvalues", "Real eigenvalues",
             "Zero eigenvalues", "Negative eigenvalues"},
            {"The row echelon form of a matrix is unique:", "Single selection",
             "Always", "Never",
             "Only for the reduced form", "Only for square matrices"},
            {"Cramer's rule applies when:", "Single selection",
             "The system has infinitely many solutions", "The coefficient matrix is singular",
             "The coefficient matrix is invertible", "The system is homogeneous"},
            {"The span of a set of vectors is:", "Single selection",
             "The largest vector", "The set of all linear combinations",
             "The determinant", "The null space"},
            {"A basis for ℝ³ must have exactly:", "Single selection",
             "1 vector", "2 vectors",
             "3 vectors", "4 vectors"},
            {"Diagonalization of A is possible when:", "Single selection",
             "A has n linearly independent eigenvectors", "A is symmetric",
             "A is upper triangular", "All of the above"},
            {"The characteristic polynomial of A is:", "Single selection",
             "det(A)", "det(A − λI)",
             "trace(A)", "rank(A)"},
            {"Two vectors are orthogonal if:", "Single selection",
             "Their cross product is zero", "Their dot product is zero",
             "They are parallel", "They have the same magnitude"},
            {"A positive definite matrix has:", "Single selection",
             "All negative eigenvalues", "All zero eigenvalues",
             "All positive eigenvalues", "Mixed sign eigenvalues"},
        };
        int qi = 0;
        for (int i = 0; i < TOTAL; i++) {
            if (i == 7) continue; // already set
            String[] q = otherQs[qi++];
            QUESTIONS[i] = new QuestionData(q[0], q[1],
                new String[]{q[2], q[3], q[4], q[5]});
        }
    }

    private int currentIdx = 8; // 1-indexed, starts on item 8
    private final Map<Integer, Integer> choices = new HashMap<>(); // question# -> selected option index
    private final Set<Integer> flagged = new HashSet<>(Set.of(4, 11));
    private final Set<Integer> answered = new HashSet<>();
    private final Runnable onExit;
    private final Runnable onSubmit;

    // UI refs
    private final VBox optionsContainer;
    private final FlowPane navDots;
    private final Label questionLabel;
    private final Label itemProgress;
    private final AcProgressBar progressBar;
    private final HBox flagBtn;
    private final AcBadge typeBadge;
    private final HBox footerContainer;
    private AcButton prevBtn;

    public FocusExecution(Runnable onExit, Runnable onSubmit) {
        this.onExit = onExit;
        this.onSubmit = onSubmit;

        // Pre-answer items 1-9
        for (int i = 1; i <= 9; i++) answered.add(i);
        // Give pre-answered items a random choice
        for (int i = 1; i <= 7; i++) choices.put(i, 2);
        choices.put(8, 2); // question 8 (index 7) has C selected
        choices.put(9, 0);

        getStyleClass().add("focus-root");

        // === TOP BAR ===
        HBox topBar = new HBox(24);
        topBar.getStyleClass().add("focus-top");
        topBar.setAlignment(Pos.CENTER);

        // Left: exit + titles
        HBox left = new HBox(10);
        left.setAlignment(Pos.CENTER_LEFT);
        AcIconButton exitBtn = new AcIconButton(
            AcIcon.create("x", 18, Color.web("#565a68")), "Exit",
            AcIconButton.Variant.GHOST, AcIconButton.Size.MD
        );
        exitBtn.setOnAction(e -> { if (onExit != null) onExit.run(); });

        VBox titles = new VBox(2);
        Label eyebrow = new Label("MATH 204 · SUMMATIVE");
        eyebrow.getStyleClass().add("focus-top-eyebrow");
        Label name = new Label("Linear Algebra — Midterm");
        name.getStyleClass().add("focus-top-name");
        titles.getChildren().addAll(eyebrow, name);
        left.getChildren().addAll(exitBtn, titles);
        HBox.setHgrow(left, Priority.ALWAYS);

        // Center: timer
        HBox timer = new HBox(8);
        timer.getStyleClass().add("focus-timer");
        timer.setAlignment(Pos.CENTER);
        timer.getChildren().addAll(
            AcIcon.create("timer", 17, Color.web("#2c40a0")),
            createLabel("12:48", "focus-timer-val"),
            createLabel("remaining", "focus-timer-lbl")
        );

        // Right: progress
        VBox right = new VBox(6);
        right.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(right, Priority.ALWAYS);
        itemProgress = new Label("Item " + currentIdx + " of " + TOTAL);
        itemProgress.getStyleClass().add("focus-prog-text");
        progressBar = new AcProgressBar(null, currentIdx, TOTAL,
            AcProgressBar.Variant.BRAND, AcProgressBar.Size.SM, false, null);
        progressBar.setMaxWidth(180);
        right.getChildren().addAll(itemProgress, progressBar);

        topBar.getChildren().addAll(left, timer, right);

        // === QUESTION STAGE ===
        VBox stage = new VBox();
        stage.setAlignment(Pos.TOP_CENTER);
        stage.setPadding(new Insets(48, 24, 24, 24));
        VBox.setVgrow(stage, Priority.ALWAYS);

        VBox questionArea = new VBox();
        questionArea.setMaxWidth(720);

        // Head: badge + flag
        HBox qHead = new HBox();
        qHead.setAlignment(Pos.CENTER_LEFT);
        typeBadge = new AcBadge("Single selection", AcBadge.Variant.NEUTRAL);
        HBox.setHgrow(typeBadge, Priority.ALWAYS);

        flagBtn = new HBox(6);
        flagBtn.getStyleClass().add("focus-flag");
        flagBtn.setAlignment(Pos.CENTER);
        flagBtn.setCursor(javafx.scene.Cursor.HAND);
        updateFlagButton();
        flagBtn.setOnMouseClicked(e -> {
            if (flagged.contains(currentIdx)) flagged.remove(currentIdx);
            else flagged.add(currentIdx);
            updateFlagButton();
            refreshDots();
        });

        qHead.getChildren().addAll(typeBadge, flagBtn);

        // Question text
        questionLabel = new Label(QUESTIONS[currentIdx - 1].prompt());
        questionLabel.getStyleClass().add("focus-question");
        questionLabel.setWrapText(true);
        VBox.setMargin(questionLabel, new Insets(20, 0, 28, 0));

        // Options
        optionsContainer = new VBox(12);
        refreshOptions();

        questionArea.getChildren().addAll(qHead, questionLabel, optionsContainer);
        stage.getChildren().add(questionArea);

        // === FOOTER ===
        footerContainer = new HBox(20);
        footerContainer.getStyleClass().add("focus-foot");
        footerContainer.setAlignment(Pos.CENTER);

        // Prev button
        prevBtn = new AcButton("Previous", AcButton.Variant.SECONDARY, AcButton.Size.MD,
            AcIcon.create("arrow-left", 15, Color.web("#1b1d25")), null);
        prevBtn.setOnAction(e -> navigateTo(Math.max(1, currentIdx - 1)));

        // Nav dots
        navDots = new FlowPane(6, 6);
        navDots.setAlignment(Pos.CENTER);
        navDots.setMaxWidth(540);
        HBox.setHgrow(navDots, Priority.ALWAYS);
        refreshDots();

        refreshFooter();

        getChildren().addAll(topBar, stage, footerContainer);
    }

    private void navigateTo(int idx) {
        currentIdx = idx;
        itemProgress.setText("Item " + currentIdx + " of " + TOTAL);
        questionLabel.setText(QUESTIONS[currentIdx - 1].prompt());
        updateFlagButton();
        refreshDots();
        refreshOptions();
        refreshFooter();
    }

    private void refreshOptions() {
        optionsContainer.getChildren().clear();
        QuestionData q = QUESTIONS[currentIdx - 1];
        Integer currentChoice = choices.get(currentIdx);

        for (int i = 0; i < q.options().length; i++) {
            final int idx = i;
            HBox opt = new HBox(16);
            opt.getStyleClass().add("focus-opt");
            boolean selected = currentChoice != null && currentChoice == i;
            if (selected) opt.getStyleClass().add("focus-opt-selected");
            opt.setAlignment(Pos.CENTER_LEFT);
            opt.setCursor(javafx.scene.Cursor.HAND);
            opt.setPadding(new Insets(18));

            Label key = new Label(String.valueOf((char)('A' + i)));
            key.getStyleClass().add("focus-opt-key");

            Label text = new Label(q.options()[i]);
            text.getStyleClass().add("focus-opt-text");
            text.setWrapText(true);
            HBox.setHgrow(text, Priority.ALWAYS);

            StackPane checkCircle = new StackPane();
            checkCircle.getStyleClass().add("focus-opt-check");
            if (selected) {
                checkCircle.getChildren().add(AcIcon.create("check", 13, Color.WHITE));
            }

            opt.getChildren().addAll(key, text, checkCircle);
            opt.setOnMouseClicked(e -> {
                choices.put(currentIdx, idx);
                answered.add(currentIdx);
                refreshOptions();
                refreshDots();
            });

            optionsContainer.getChildren().add(opt);
        }
    }

    private void refreshDots() {
        navDots.getChildren().clear();
        for (int n = 1; n <= TOTAL; n++) {
            final int num = n;
            Label dot = new Label(String.valueOf(n));
            dot.getStyleClass().add("focus-dot");
            dot.setAlignment(Pos.CENTER);
            dot.setMinSize(30, 30);
            dot.setPrefSize(30, 30);
            dot.setCursor(javafx.scene.Cursor.HAND);

            if (answered.contains(n)) dot.getStyleClass().add("focus-dot-answered");
            if (n == currentIdx) dot.getStyleClass().add("focus-dot-current");
            if (flagged.contains(n)) {
                dot.setStyle(dot.getStyle() + "-fx-border-color: #b8762a;");
            }

            dot.setOnMouseClicked(e -> navigateTo(num));
            navDots.getChildren().add(dot);
        }
    }

    private void refreshFooter() {
        footerContainer.getChildren().clear();

        // Rebuild prev button
        prevBtn = new AcButton("Previous", AcButton.Variant.SECONDARY, AcButton.Size.MD,
            AcIcon.create("arrow-left", 15, Color.web("#1b1d25")), null);
        prevBtn.setOnAction(e -> navigateTo(Math.max(1, currentIdx - 1)));
        prevBtn.setDisable(currentIdx <= 1);

        footerContainer.getChildren().add(prevBtn);
        footerContainer.getChildren().add(navDots);

        if (currentIdx < TOTAL) {
            AcButton nextBtn = new AcButton("Next", AcButton.Variant.PRIMARY, AcButton.Size.MD,
                null, AcIcon.create("arrow-right", 15, Color.WHITE));
            nextBtn.setOnAction(e -> navigateTo(Math.min(TOTAL, currentIdx + 1)));
            footerContainer.getChildren().add(nextBtn);
        } else {
            AcButton submitBtn = new AcButton("Review & submit", AcButton.Variant.PRIMARY, AcButton.Size.MD,
                null, AcIcon.create("send", 15, Color.WHITE));
            submitBtn.setOnAction(e -> { if (onSubmit != null) onSubmit.run(); });
            footerContainer.getChildren().add(submitBtn);
        }
    }

    private void updateFlagButton() {
        flagBtn.getChildren().clear();
        boolean isFlagged = flagged.contains(currentIdx);
        flagBtn.getChildren().add(AcIcon.create("flag", 15,
            isFlagged ? Color.web("#985f1f") : Color.web("#6f7484")));
        flagBtn.getChildren().add(new Label(isFlagged ? "Flagged" : "Flag for review"));
        if (isFlagged) {
            if (!flagBtn.getStyleClass().contains("focus-flag-on"))
                flagBtn.getStyleClass().add("focus-flag-on");
        } else {
            flagBtn.getStyleClass().remove("focus-flag-on");
        }
    }

    private Label createLabel(String text, String styleClass) {
        Label l = new Label(text);
        l.getStyleClass().add(styleClass);
        return l;
    }
}
