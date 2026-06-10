package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * ItemBankScreen — item repository with filter sidebar and item table.
 * Mirrors React ItemBank.jsx.
 */
public final class ItemBankScreen extends HBox {

    private record Item(String title, String type, String[] tags, int diff, int usage, double disc) {}

    private static final List<Item> ITEMS = List.of(
        new Item("Define the rank of a matrix", "Single", new String[]{"algebra", "rank"}, 2, 142, 0.51),
        new Item("Eigenvalue / eigenvector pairing", "Relational", new String[]{"eigen", "algebra"}, 4, 88, 0.38),
        new Item("Order the Gram–Schmidt steps", "Sequencing", new String[]{"orthogonal"}, 3, 64, 0.44),
        new Item("Span of a vector set", "Multiple", new String[]{"vectors", "span"}, 4, 51, 0.29),
        new Item("Determinant of a 2×2 matrix", "Completion", new String[]{"determinant"}, 1, 230, 0.62),
        new Item("Is the matrix invertible?", "Binary", new String[]{"inverse"}, 2, 176, 0.55),
        new Item("Diagonalizability conditions", "Multiple", new String[]{"eigen", "diagonal"}, 5, 33, 0.41),
        new Item("Null space dimension (nullity)", "Completion", new String[]{"nullity", "rank"}, 3, 97, 0.47)
    );

    private record TypeFilter(String label, int count) {}
    private static final List<TypeFilter> TYPES = List.of(
        new TypeFilter("Single selection", 612),
        new TypeFilter("Multiple selection", 488),
        new TypeFilter("Completion", 401),
        new TypeFilter("Relational", 287),
        new TypeFilter("Sequencing", 196),
        new TypeFilter("Binary", 497)
    );

    public ItemBankScreen() {
        setSpacing(22);

        // === FILTER SIDEBAR ===
        VBox filters = new VBox(18);
        filters.setMinWidth(200);
        filters.setPrefWidth(220);
        filters.setStyle("-fx-padding: 4 0 0 0;");

        // Subject
        VBox subjectGroup = filterGroup("Subject");
        subjectGroup.getChildren().add(new AcSelect(new String[]{"Mathematics", "Sciences", "Humanities"}, "Mathematics", "sm"));
        filters.getChildren().add(subjectGroup);

        // Item type checkboxes
        VBox typeGroup = filterGroup("Item type");
        for (TypeFilter tf : TYPES) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            AcCheckbox cb = new AcCheckbox();
            cb.setSelected("Single selection".equals(tf.label) || "Relational".equals(tf.label));
            Label l = new Label(tf.label);
            l.setStyle("-fx-font-size: 13px; -fx-text-fill: #3b3f4c;");
            HBox.setHgrow(l, Priority.ALWAYS);
            Label n = new Label(String.valueOf(tf.count));
            n.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");
            row.getChildren().addAll(cb, l, n);
            typeGroup.getChildren().add(row);
        }
        filters.getChildren().add(typeGroup);

        // Tags
        VBox tagGroup = filterGroup("Tags");
        FlowPane tagFlow = new FlowPane(6, 6);
        for (String tag : new String[]{"algebra", "eigen", "vectors", "rank", "determinant"}) {
            Label t = new Label("# " + tag);
            boolean active = "algebra".equals(tag);
            t.setStyle("-fx-font-size: 12px; -fx-padding: 4 10 4 10; -fx-background-radius: 999;" +
                (active ? " -fx-background-color: #eaedfa; -fx-text-fill: #2c40a0; -fx-font-weight: bold;"
                    : " -fx-background-color: #f2f1ea; -fx-text-fill: #6f7484;"));
            t.setCursor(javafx.scene.Cursor.HAND);
            tagFlow.getChildren().add(t);
        }
        tagGroup.getChildren().add(tagFlow);
        filters.getChildren().add(tagGroup);

        // === MAIN ===
        VBox main = new VBox(16);
        HBox.setHgrow(main, Priority.ALWAYS);

        // Toolbar
        main.getChildren().add(PageKit.toolbar(
            new AcInput(null, "text", null, "Search 2,481 items…",
                AcIcon.create("search", 14, Color.web("#8c909e")), null, "sm", false),
            new AcSelect(new String[]{"Sort: Most used", "Sort: Difficulty", "Sort: Discrimination", "Sort: Newest"}, "Sort: Most used", "sm"),
            PageKit.spacer(),
            new AcButton("Import", AcButton.Variant.SECONDARY, AcButton.Size.SM,
                AcIcon.create("upload", 14, Color.web("#1b1d25")), null),
            new AcButton("New item", AcButton.Variant.PRIMARY, AcButton.Size.SM,
                AcIcon.create("plus", 14, Color.WHITE), null)
        ));

        // Stats
        main.getChildren().add(PageKit.statRow(
            PageKit.stat("library", "brand", "2,481", "Items in bank", "+34 this week", "up", false),
            PageKit.stat("gauge", "gold", "0.46", "Mean discrimination", "healthy"),
            PageKit.stat("triangle-alert", "danger", "11", "Low-discrimination", "review suggested", "down", false)
        ));

        // Table
        AcCard tableCard = new AcCard(null, null, "sm", false);
        VBox tbody = tableCard.getBody();

        // Header
        HBox thead = new HBox();
        thead.setStyle("-fx-background-color: #f2f1ea; -fx-padding: 10 16 10 16; -fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        thead.setAlignment(Pos.CENTER_LEFT);
        AcCheckbox headCb = new AcCheckbox();
        headCb.setMinWidth(30);
        Label hItem = new Label("Item");
        HBox.setHgrow(hItem, Priority.ALWAYS);
        hItem.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
        Label hType = new Label("Type");
        hType.setMinWidth(90);
        hType.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
        Label hDiff = new Label("Difficulty");
        hDiff.setMinWidth(80);
        hDiff.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
        Label hUsed = new Label("Used");
        hUsed.setMinWidth(80);
        hUsed.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
        Label hActions = new Label("");
        hActions.setMinWidth(60);
        thead.getChildren().addAll(headCb, hItem, hType, hDiff, hUsed, hActions);
        tbody.getChildren().add(thead);

        // Rows
        for (Item it : ITEMS) {
            HBox row = new HBox(10);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");

            AcCheckbox cb = new AcCheckbox();
            cb.setMinWidth(30);

            VBox itemCol = new VBox(4);
            HBox.setHgrow(itemCol, Priority.ALWAYS);
            Label title = new Label(it.title);
            title.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
            FlowPane tags = new FlowPane(4, 4);
            for (String tag : it.tags) {
                Label tl = new Label("# " + tag);
                tl.setStyle("-fx-font-size: 11px; -fx-text-fill: #6f7484; -fx-background-color: #f2f1ea; -fx-background-radius: 999; -fx-padding: 2 7 2 7;");
                tags.getChildren().add(tl);
            }
            itemCol.getChildren().addAll(title, tags);

            HBox typeBox = new HBox(new AcBadge(it.type, AcBadge.Variant.NEUTRAL));
            typeBox.setMinWidth(90);

            // Difficulty dots
            HBox diffDots = new HBox(3);
            diffDots.setMinWidth(80);
            diffDots.setAlignment(Pos.CENTER_LEFT);
            for (int d = 1; d <= 5; d++) {
                Region dot = new Region();
                dot.setMinSize(7, 7); dot.setPrefSize(7, 7); dot.setMaxSize(7, 7);
                dot.setStyle("-fx-background-radius: 999; -fx-background-color: " +
                    (d <= it.diff ? "#2c40a0" : "#d6d5cd") + ";");
                diffDots.getChildren().add(dot);
            }

            // Usage
            HBox usageBox = new HBox(4);
            usageBox.setMinWidth(80);
            usageBox.setAlignment(Pos.CENTER_LEFT);
            Label usageL = new Label(it.usage + "×");
            usageL.setStyle("-fx-font-size: 13px; -fx-text-fill: #3b3f4c;");
            String discColor = it.disc >= 0.45 ? "#2e6b4f" : it.disc >= 0.3 ? "#985f1f" : "#9b2c2c";
            Label discL = new Label("· δ" + it.disc);
            discL.setStyle("-fx-font-size: 11px; -fx-text-fill: " + discColor + ";");
            usageBox.getChildren().addAll(usageL, discL);

            // Actions
            HBox actions = new HBox(4);
            actions.setMinWidth(60);
            actions.getChildren().addAll(
                new AcIconButton(AcIcon.create("pencil", 15, Color.web("#565a68")), "Edit", AcIconButton.Variant.GHOST, AcIconButton.Size.SM),
                new AcIconButton(AcIcon.create("plus", 15, Color.web("#565a68")), "Insert", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM)
            );

            row.getChildren().addAll(cb, itemCol, typeBox, diffDots, usageBox, actions);
            tbody.getChildren().add(row);
        }

        main.getChildren().add(tableCard);
        getChildren().addAll(filters, main);
    }

    private VBox filterGroup(String caption) {
        VBox g = new VBox(8);
        Label cap = new Label(caption);
        cap.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
        g.getChildren().add(cap);
        return g;
    }
}
