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
 * EducatorAuthoring — assessment authoring workspace with item rail, editor, and item bank.
 * Mirrors the React EducatorAuthoring component exactly.
 */
public final class EducatorAuthoring extends HBox {

    private record ItemDef(int n, String title, String type, int pts) {}
    private static final List<ItemDef> ITEM_LIST = List.of(
        new ItemDef(1, "Definition of matrix rank", "Single", 5),
        new ItemDef(2, "Determinant of a 2×2 matrix", "Completion", 5),
        new ItemDef(3, "Eigenvalue / eigenvector pairing", "Relational", 10),
        new ItemDef(4, "Order the Gram–Schmidt steps", "Sequencing", 8),
        new ItemDef(5, "Is the matrix invertible?", "Binary", 4)
    );

    private static final String[][] ITEM_QUESTIONS = {
        {"In linear algebra, the rank of an m × n matrix is defined as which of the following?",
         "The set of all linear combinations of the column vectors",
         "The dimension of the null space of the matrix",
         "The maximum number of linearly independent column vectors",
         "The product of the matrix and its transpose"},
        {"Calculate the determinant of the matrix [[3, 7], [1, -4]].",
         "-19", "-5", "5", "19"},
        {"Match each eigenvalue with its corresponding eigenvector.",
         "λ = 2 → v₁", "λ = -1 → v₂", "λ = 3 → v₃", "λ = 0 → v₄"},
        {"Place the Gram–Schmidt steps in the correct order.",
         "Start with the first vector", "Subtract projections",
         "Normalize the result", "Repeat for remaining vectors"},
        {"Determine if the following matrix is invertible: [[1,2],[3,6]].",
         "Yes — determinant is non-zero", "No — determinant is zero",
         "Yes — it is square", "Cannot be determined"}
    };

    private static final int[] CORRECT_ANSWERS = {2, 0, 0, 0, 1};

    private record BankItem(String title, String[] tags, String type) {}
    private static final List<BankItem> BANK = List.of(
        new BankItem("Null space dimension", new String[]{"algebra", "nullity"}, "Single"),
        new BankItem("Orthogonal projection formula", new String[]{"projection"}, "Completion"),
        new BankItem("Match transforms to matrices", new String[]{"transforms"}, "Relational"),
        new BankItem("Diagonalizability conditions", new String[]{"eigen"}, "Multiple")
    );

    private int activeItem = 1;
    private int correctOption;
    private final VBox railContainer;
    private final VBox mainContainer;

    public EducatorAuthoring() {
        setSpacing(18);
        setPadding(new Insets(0));
        setMaxWidth(1320);

        correctOption = CORRECT_ANSWERS[activeItem - 1];

        // === Left Rail ===
        railContainer = new VBox();

        // === Main Editor ===
        mainContainer = new VBox();
        HBox.setHgrow(mainContainer, Priority.ALWAYS);

        // === Item Bank ===
        VBox bank = createBank();

        refreshRail();
        refreshEditor();

        getChildren().addAll(railContainer, mainContainer, bank);
    }

    private void switchToItem(int itemNum) {
        activeItem = itemNum;
        correctOption = CORRECT_ANSWERS[activeItem - 1];
        refreshRail();
        refreshEditor();
    }

    private void refreshRail() {
        railContainer.getChildren().clear();

        VBox rail = new VBox(10);
        rail.getStyleClass().add("author-rail");

        // Head
        HBox head = new HBox();
        head.setAlignment(Pos.CENTER_LEFT);
        Label cap = new Label("ITEMS · " + ITEM_LIST.size());
        cap.getStyleClass().add("author-rail-cap");
        HBox.setHgrow(cap, Priority.ALWAYS);
        AcIconButton addBtn = new AcIconButton(
            AcIcon.create("plus", 15, Color.web("#565a68")),
            "Add item", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM
        );
        head.getChildren().addAll(cap, addBtn);

        // Item list
        VBox itemList = new VBox(3);
        for (ItemDef it : ITEM_LIST) {
            HBox row = new HBox(8);
            row.getStyleClass().add("author-itemrow");
            if (activeItem == it.n) row.getStyleClass().add("author-itemrow-active");
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(9, 8, 9, 8));
            row.setCursor(javafx.scene.Cursor.HAND);

            Node grip = AcIcon.create("grip-vertical", 13, Color.web("#b2b6c0"));
            Label num = new Label(String.valueOf(it.n));
            num.getStyleClass().add("author-itemrow-num");
            Label title = new Label(it.title);
            title.getStyleClass().add("author-itemrow-title");
            title.setMaxWidth(140);
            title.setEllipsisString("…");
            HBox.setHgrow(title, Priority.ALWAYS);
            Label pts = new Label(it.pts + "pt");
            pts.getStyleClass().add("author-itemrow-pts");

            row.getChildren().addAll(grip, num, title, pts);

            // Click to switch
            row.setOnMouseClicked(e -> switchToItem(it.n));

            itemList.getChildren().add(row);
        }

        // Total
        HBox total = new HBox();
        total.getStyleClass().add("author-rail-total");
        total.setAlignment(Pos.CENTER_LEFT);
        total.setPadding(new Insets(12, 0, 0, 0));
        Label totalLabel = new Label("Total weight");
        totalLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #565a68;");
        HBox.setHgrow(totalLabel, Priority.ALWAYS);
        int totalPts = ITEM_LIST.stream().mapToInt(ItemDef::pts).sum();
        Label totalVal = new Label(totalPts + " pts");
        totalVal.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        total.getChildren().addAll(totalLabel, totalVal);

        rail.getChildren().addAll(head, itemList, total);
        railContainer.getChildren().add(rail);
    }

    private void refreshEditor() {
        mainContainer.getChildren().clear();

        VBox main = new VBox(18);
        main.getStyleClass().add("author-main-panel");

        ItemDef item = ITEM_LIST.get(activeItem - 1);
        String[] qData = ITEM_QUESTIONS[activeItem - 1];

        // Meta section
        VBox meta = new VBox(12);
        meta.setPadding(new Insets(0, 0, 18, 0));
        meta.setStyle("-fx-border-color: #e3e2db; -fx-border-width: 0 0 1 0;");

        AcInput titleInput = new AcInput(null, "text", item.title,
            null, null, null, "md", false);

        HBox metaRow = new HBox(10);
        AcSelect typeSelect = new AcSelect(
            new String[]{"True / False", "Single selection", "Multiple selection",
                "Relational mapping", "Sequencing", "Completion"},
            item.type.equals("Single") ? "Single selection" :
            item.type.equals("Relational") ? "Relational mapping" :
            item.type.equals("Binary") ? "True / False" : item.type,
            "sm"
        );
        AcInput ptsInput = new AcInput(null, "text", String.valueOf(item.pts),
            null, null, "pts", "sm", false);
        ptsInput.setMaxWidth(96);
        AcInput timeInput = new AcInput(null, "text", "2:00", null,
            AcIcon.create("timer", 15, Color.web("#8c909e")), null, "sm", false);
        timeInput.setMaxWidth(96);
        metaRow.getChildren().addAll(typeSelect, ptsInput, timeInput);

        meta.getChildren().addAll(titleInput, metaRow);

        // Type tabs
        AcTabs typeTabs = new AcTabs(List.of(
            new AcTabs.TabDef("binary", "Binary", AcIcon.create("toggle-left", 15, Color.web("#6f7484"))),
            new AcTabs.TabDef("single", "Single", AcIcon.create("circle-dot", 15, Color.web("#6f7484"))),
            new AcTabs.TabDef("multiple", "Multiple", AcIcon.create("list-checks", 15, Color.web("#6f7484"))),
            new AcTabs.TabDef("relational", "Relational", AcIcon.create("waypoints", 15, Color.web("#6f7484"))),
            new AcTabs.TabDef("sequence", "Sequence", AcIcon.create("arrow-down-up", 15, Color.web("#6f7484"))),
            new AcTabs.TabDef("completion", "Completion", AcIcon.create("text-cursor-input", 15, Color.web("#6f7484")))
        ), item.type.toLowerCase().equals("single") ? "single" :
           item.type.toLowerCase().equals("binary") ? "binary" :
           item.type.toLowerCase().equals("relational") ? "relational" :
           item.type.toLowerCase().equals("sequencing") ? "sequence" :
           item.type.toLowerCase().equals("completion") ? "completion" : "single",
        id -> {}, AcTabs.Variant.UNDERLINE);

        // Question prompt
        VBox prompt = new VBox(10);
        Label promptLabel = new Label("Question prompt");
        promptLabel.getStyleClass().add("author-label");
        Label promptText = new Label(qData[0]);
        promptText.getStyleClass().add("author-textarea");
        promptText.setWrapText(true);
        prompt.getChildren().addAll(promptLabel, promptText);

        // Answer options
        VBox answers = new VBox(10);
        Label ansLabel = new Label("Answer options · select the correct one");
        ansLabel.getStyleClass().add("author-label");
        answers.getChildren().add(ansLabel);

        for (int i = 0; i < qData.length - 1; i++) {
            final int optIdx = i;
            HBox opt = new HBox(12);
            opt.getStyleClass().add("author-opt");
            if (correctOption == i) opt.getStyleClass().add("author-opt-correct");
            opt.setAlignment(Pos.CENTER_LEFT);
            opt.setPadding(new Insets(10, 12, 10, 12));

            // Radio circle
            StackPane radio = new StackPane();
            radio.getStyleClass().add("author-opt-radio");
            radio.setCursor(javafx.scene.Cursor.HAND);
            radio.setMinSize(24, 24);
            radio.setPrefSize(24, 24);
            radio.setMaxSize(24, 24);
            if (correctOption == i) {
                radio.getChildren().add(AcIcon.create("check", 13, Color.WHITE));
                radio.setStyle("-fx-background-color: #2e6b4f; -fx-background-radius: 999;");
            } else {
                radio.setStyle("-fx-background-color: white; -fx-border-color: #c2c1b8; -fx-border-width: 1.5; -fx-border-radius: 999; -fx-background-radius: 999;");
            }

            // Click radio to set correct
            radio.setOnMouseClicked(e -> {
                correctOption = optIdx;
                CORRECT_ANSWERS[activeItem - 1] = optIdx;
                refreshEditor();
            });

            Label key = new Label(String.valueOf((char)('A' + i)));
            key.getStyleClass().add("author-opt-key");

            Label text = new Label(qData[i + 1]);
            text.getStyleClass().add("author-opt-text");
            text.setWrapText(true);
            HBox.setHgrow(text, Priority.ALWAYS);

            opt.getChildren().addAll(radio, key, text);

            if (correctOption == i) {
                opt.getChildren().add(new AcBadge("Correct", AcBadge.Variant.SUCCESS, true));
            }

            AcIconButton trashBtn = new AcIconButton(
                AcIcon.create("trash-2", 15, Color.web("#565a68")),
                "Remove", AcIconButton.Variant.GHOST, AcIconButton.Size.SM
            );
            opt.getChildren().add(trashBtn);

            answers.getChildren().add(opt);
        }

        AcButton addOptionBtn = new AcButton("Add option", AcButton.Variant.GHOST, AcButton.Size.SM,
            AcIcon.create("plus", 14, Color.web("#3b3f4c")), null);
        answers.getChildren().add(addOptionBtn);

        main.getChildren().addAll(meta, typeTabs, prompt, answers);
        mainContainer.getChildren().add(main);
    }

    private VBox createBank() {
        VBox bank = new VBox(12);
        bank.getStyleClass().add("author-bank");

        // Head
        HBox head = new HBox();
        head.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Item bank");
        title.getStyleClass().add("author-bank-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        AcBadge countBadge = new AcBadge("2,481", AcBadge.Variant.NEUTRAL);
        head.getChildren().addAll(title, countBadge);

        // Search
        AcInput searchInput = new AcInput("Search catalog…",
            AcIcon.create("search", 15, Color.web("#8c909e")), "sm");

        // Tags
        FlowPane tags = new FlowPane(6, 6);
        tags.getChildren().addAll(
            new AcTag("algebra", true, true),
            new AcTag("eigen", true),
            new AcTag("vectors", true)
        );

        // Items
        VBox list = new VBox(10);
        for (BankItem b : BANK) {
            VBox card = new VBox(6);
            card.getStyleClass().add("bankcard");
            card.setPadding(new Insets(12));

            HBox cardTop = new HBox();
            cardTop.setAlignment(Pos.CENTER_LEFT);
            Label type = new Label(b.type.toUpperCase());
            type.getStyleClass().add("bankcard-type");
            HBox.setHgrow(type, Priority.ALWAYS);
            AcIconButton insertBtn = new AcIconButton(
                AcIcon.create("plus", 14, Color.web("#565a68")),
                "Insert", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM
            );
            cardTop.getChildren().addAll(type, insertBtn);

            Label cardTitle = new Label(b.title);
            cardTitle.getStyleClass().add("bankcard-title");
            cardTitle.setWrapText(true);

            FlowPane cardTags = new FlowPane(5, 5);
            for (String t : b.tags) {
                cardTags.getChildren().add(new AcTag(t, true));
            }

            card.getChildren().addAll(cardTop, cardTitle, cardTags);
            list.getChildren().add(card);
        }

        bank.getChildren().addAll(head, searchInput, tags, list);
        return bank;
    }
}
