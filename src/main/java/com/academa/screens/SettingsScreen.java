package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * SettingsScreen — institution settings with sectioned navigation.
 * Mirrors React Settings.jsx.
 */
public final class SettingsScreen extends HBox {

    private String activeSection = "general";
    private final VBox bodyContainer;

    private record NavDef(String id, String label, String icon) {}
    private static final List<NavDef> NAV = List.of(
        new NavDef("general", "General", "building-2"),
        new NavDef("assessment", "Assessment policy", "file-lock-2"),
        new NavDef("integrity", "Integrity", "shield-check"),
        new NavDef("integrations", "Integrations", "plug"),
        new NavDef("danger", "Danger zone", "triangle-alert")
    );

    public SettingsScreen() {
        setSpacing(22);

        // Left nav
        VBox nav = new VBox(2);
        nav.setMinWidth(200);
        nav.setPrefWidth(220);
        nav.setStyle("-fx-padding: 4 0 0 0;");

        for (NavDef n : NAV) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 12, 10, 12));
            item.setStyle("-fx-background-radius: 9; -fx-cursor: hand;");
            item.setCursor(javafx.scene.Cursor.HAND);
            if (n.id.equals(activeSection)) {
                item.setStyle(item.getStyle() + " -fx-background-color: #eaedfa;");
            }
            item.getChildren().add(AcIcon.create(n.icon, 16, Color.web(n.id.equals(activeSection) ? "#2c40a0" : "#565a68")));
            Label l = new Label(n.label);
            l.setStyle("-fx-font-size: 14px; -fx-font-weight: " + (n.id.equals(activeSection) ? "bold" : "500") +
                "; -fx-text-fill: " + (n.id.equals(activeSection) ? "#2c40a0" : "#565a68") + ";");
            item.getChildren().add(l);
            item.setOnMouseClicked(e -> {
                activeSection = n.id;
                refreshAll();
            });
            nav.getChildren().add(item);
        }

        // Body
        bodyContainer = new VBox();
        HBox.setHgrow(bodyContainer, Priority.ALWAYS);
        refreshBody();

        getChildren().addAll(nav, bodyContainer);
    }

    private void refreshAll() {
        getChildren().clear();
        // Rebuild nav
        VBox nav = new VBox(2);
        nav.setMinWidth(200);
        nav.setPrefWidth(220);
        for (NavDef n : NAV) {
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 12, 10, 12));
            item.setStyle("-fx-background-radius: 9; -fx-cursor: hand;");
            item.setCursor(javafx.scene.Cursor.HAND);
            if (n.id.equals(activeSection)) {
                item.setStyle(item.getStyle() + " -fx-background-color: #eaedfa;");
            }
            item.getChildren().add(AcIcon.create(n.icon, 16, Color.web(n.id.equals(activeSection) ? "#2c40a0" : "#565a68")));
            Label l = new Label(n.label);
            l.setStyle("-fx-font-size: 14px; -fx-font-weight: " + (n.id.equals(activeSection) ? "bold" : "500") +
                "; -fx-text-fill: " + (n.id.equals(activeSection) ? "#2c40a0" : "#565a68") + ";");
            item.getChildren().add(l);
            item.setOnMouseClicked(e -> {
                activeSection = n.id;
                refreshAll();
            });
            nav.getChildren().add(item);
        }
        bodyContainer.getChildren().clear();
        refreshBody();
        getChildren().addAll(nav, bodyContainer);
    }

    private void refreshBody() {
        bodyContainer.getChildren().clear();
        bodyContainer.getChildren().add(switch (activeSection) {
            case "assessment" -> buildAssessment();
            case "integrity" -> buildIntegrity();
            case "integrations" -> buildIntegrations();
            case "danger" -> buildDanger();
            default -> buildGeneral();
        });
    }

    private VBox buildGeneral() {
        VBox sec = new VBox(16);
        AcCard card = new AcCard(null, null, "lg", false);
        VBox body = card.getBody();

        body.getChildren().add(sectionHead("Institution profile",
            "Public-facing identity used across reports and learner views."));
        body.getChildren().add(field("Institution name", new AcInput(null, "text", "Northbridge Institute", null, null, null, "md", false)));
        body.getChildren().add(field("Short code", new AcInput(null, "text", "NBI", null, null, null, "md", false)));
        body.getChildren().add(field("Primary timezone", new AcSelect(new String[]{"GMT (London)", "EST (New York)", "CET (Berlin)", "GST (Dubai)"}, "GMT (London)")));
        body.getChildren().add(field("Academic term", new AcSelect(new String[]{"Autumn 2025", "Spring 2026", "Summer 2026"}, "Autumn 2025")));

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(8, 0, 0, 0));
        buttons.getChildren().addAll(
            new AcButton("Discard", AcButton.Variant.GHOST, AcButton.Size.MD),
            new AcButton("Save changes", AcButton.Variant.PRIMARY, AcButton.Size.MD,
                AcIcon.create("check", 14, Color.WHITE), null)
        );
        sec.getChildren().addAll(card, buttons);
        return sec;
    }

    private Node buildAssessment() {
        AcCard card = new AcCard(null, null, "lg", false);
        VBox body = card.getBody();
        body.getChildren().add(sectionHead("Assessment policy",
            "Defaults applied to every new assessment. Educators may override per cohort."));
        body.getChildren().add(field("Default duration", new AcInput(null, "text", "90", null, null, "min", "md", false)));
        body.getChildren().add(field("Grade release", new AcSelect(
            new String[]{"Immediately on submit", "After window closes", "Manual release"}, "After window closes")));

        body.getChildren().add(new Region() {{ setMinHeight(8); }});
        body.getChildren().add(toggle("Allow late submissions", "Accept submissions after the window closes, flagged for educator review.", true));
        body.getChildren().add(toggle("Auto-grade objective items", "Single, multiple, binary and completion items graded on submit.", true));
        body.getChildren().add(toggle("Shuffle item order", "Randomise item sequence per learner to reduce collusion.", false));
        body.getChildren().add(toggle("Show item-level feedback", "Reveal correct answers and educator notes after grade release.", true));
        return card;
    }

    private Node buildIntegrity() {
        AcCard card = new AcCard(null, null, "lg", false);
        VBox body = card.getBody();
        body.getChildren().add(sectionHead("Integrity & proctoring", "Controls for secure assessment delivery."));
        body.getChildren().add(toggle("Lockdown browser", "Prevent tab-switching and copy/paste during summative assessments.", true));
        body.getChildren().add(toggle("Anomaly detection", "Flag unusual timing and answer-change patterns for review.", true));
        body.getChildren().add(toggle("Plagiarism scan on free-text", "Compare completion responses against the institutional corpus.", false));

        HBox radioRow = new HBox(18);
        radioRow.setAlignment(Pos.CENTER_LEFT);
        ToggleGroup tg = new ToggleGroup();
        for (String opt : new String[]{"Low", "Balanced", "Strict"}) {
            RadioButton rb = new RadioButton(opt);
            rb.setToggleGroup(tg);
            if ("Balanced".equals(opt)) rb.setSelected(true);
            rb.setStyle("-fx-font-size: 13px;");
            radioRow.getChildren().add(rb);
        }
        body.getChildren().add(field("Flag sensitivity", radioRow));
        return card;
    }

    private Node buildIntegrations() {
        AcCard card = new AcCard(null, null, "lg", false);
        VBox body = card.getBody();
        body.getChildren().add(sectionHead("Integrations", "Connect Academa to your institution's systems."));

        record IntDef(String name, String desc, boolean on, String icon) {}
        var items = List.of(
            new IntDef("SIS · PowerSchool", "Roster & enrollment sync", true, "library"),
            new IntDef("SSO · SAML 2.0", "Single sign-on for all roles", true, "key-round"),
            new IntDef("LMS · Canvas", "Grade passback", false, "graduation-cap"),
            new IntDef("Webhooks", "Real-time event stream", false, "webhook")
        );

        for (IntDef it : items) {
            HBox row = new HBox(13);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(14, 0, 14, 0));
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");

            StackPane iconBox = new StackPane();
            iconBox.setMinSize(38, 38); iconBox.setPrefSize(38, 38); iconBox.setMaxSize(38, 38);
            iconBox.setStyle("-fx-background-color: #eaedfa; -fx-background-radius: 9;");
            iconBox.setAlignment(Pos.CENTER);
            iconBox.getChildren().add(AcIcon.create(it.icon, 18, Color.web("#2c40a0")));

            VBox txt = new VBox();
            HBox.setHgrow(txt, Priority.ALWAYS);
            HBox nameRow = new HBox(8);
            nameRow.setAlignment(Pos.CENTER_LEFT);
            Label n = new Label(it.name);
            n.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
            nameRow.getChildren().add(n);
            if (it.on) nameRow.getChildren().add(new AcBadge("Connected", AcBadge.Variant.SUCCESS, true));
            Label d = new Label(it.desc);
            d.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
            txt.getChildren().addAll(nameRow, d);

            AcButton btn = new AcButton(it.on ? "Manage" : "Connect",
                it.on ? AcButton.Variant.GHOST : AcButton.Variant.SECONDARY, AcButton.Size.SM);
            row.getChildren().addAll(iconBox, txt, btn);
            body.getChildren().add(row);
        }
        return card;
    }

    private Node buildDanger() {
        AcCard card = new AcCard(null, null, "lg", false);
        card.setStyle("-fx-border-color: #fce8e8;");
        VBox body = card.getBody();
        body.getChildren().add(sectionHead("Danger zone", "Irreversible actions. Proceed with care."));

        body.getChildren().add(dangerItem("Archive term data",
            "Move all Autumn 2025 assessments and results to cold storage.",
            new AcButton("Archive", AcButton.Variant.SECONDARY, AcButton.Size.SM)));
        body.getChildren().add(dangerItem("Purge integrity flags",
            "Permanently delete all resolved integrity flags older than 1 year.",
            new AcButton("Purge", AcButton.Variant.DANGER, AcButton.Size.SM)));
        return card;
    }

    // Helpers
    private VBox sectionHead(String title, String sub) {
        VBox h = new VBox(4);
        h.setPadding(new Insets(0, 0, 16, 0));
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
        Label s = new Label(sub);
        s.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #6f7484;");
        s.setWrapText(true);
        h.getChildren().addAll(t, s);
        return h;
    }

    private HBox field(String label, Node control) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 12, 0));
        row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #3b3f4c;");
        l.setMinWidth(160);
        HBox.setHgrow(l, Priority.SOMETIMES);
        HBox right = new HBox(control);
        right.setMinWidth(260);
        HBox.setHgrow(right, Priority.ALWAYS);
        row.getChildren().addAll(l, right);
        return row;
    }

    private HBox toggle(String label, String desc, boolean on) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 0, 14, 0));
        row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        VBox txt = new VBox(2);
        HBox.setHgrow(txt, Priority.ALWAYS);
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
        Label d = new Label(desc);
        d.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #8c909e;");
        d.setWrapText(true);
        txt.getChildren().addAll(l, d);

        AcCheckbox cb = new AcCheckbox();
        cb.setSelected(on);
        row.getChildren().addAll(txt, cb);
        return row;
    }

    private HBox dangerItem(String label, String desc, AcButton btn) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 0, 14, 0));
        row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        VBox txt = new VBox(2);
        HBox.setHgrow(txt, Priority.ALWAYS);
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
        Label d = new Label(desc);
        d.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #8c909e;");
        d.setWrapText(true);
        txt.getChildren().addAll(l, d);
        row.getChildren().addAll(txt, btn);
        return row;
    }
}
