package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * AdminOverview — institution dashboard with hero panel, stats, feed, approvals, and cohort health.
 * Mirrors React AdminOverview.jsx.
 */
public final class AdminOverview extends VBox {

    private static final int[] LOAD = {22, 18, 14, 30, 46, 58, 71, 88, 96, 84, 67, 52};

    private record FeedItem(String icon, String tone, String text, String time) {}
    private static final FeedItem[] FEED = {
        new FeedItem("user-plus", "brand", "Dr. Idris Khan was granted Educator access to Sciences.", "2 min ago"),
        new FeedItem("shield-alert", "danger", "Integrity flag raised on MATH 204 Midterm — 2 sessions paused.", "18 min ago"),
        new FeedItem("file-check-2", "success", "BIO 110 assessment window closed · 142 submissions auto-graded.", "1 hour ago"),
        new FeedItem("settings", "gold", "Late-submission policy updated for Grade 11 cohorts.", "3 hours ago"),
        new FeedItem("user-x", "danger", "Leo Park account suspended pending review.", "Yesterday"),
    };

    private record Approval(String name, String desc) {}
    private static final Approval[] APPROVALS = {
        new Approval("Marcus Webb", "Educator invite · Humanities"),
        new Approval("Sana Iqbal", "Role change → Department lead"),
        new Approval("Grade 10 · D", "New cohort · 28 learners"),
    };

    public AdminOverview() {
        setSpacing(22);

        // === HERO ===
        getChildren().add(createHero());

        // === STATS ===
        getChildren().add(PageKit.statRow(
            PageKit.stat("graduation-cap", "brand", "12,840", "Enrolled learners", "+128 this term", "up", false),
            PageKit.stat("users", "gold", "642", "Educators", "+9 this term", "up", false),
            PageKit.stat("file-pen-line", "success", "318", "Assessments live", "across 41 cohorts"),
            PageKit.stat("shield-alert", "danger", "6", "Integrity flags", "needs review", "down", false)
        ));

        // === BODY ===
        HBox body = new HBox(22);
        VBox feed = createFeed();
        HBox.setHgrow(feed, Priority.ALWAYS);
        VBox side = createSide();
        side.setMinWidth(320);
        side.setPrefWidth(360);
        body.getChildren().addAll(feed, side);
        getChildren().add(body);
    }

    private Node createHero() {
        HBox hero = new HBox();
        hero.setStyle("-fx-background-color: #1e2029; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(20,21,27,0.07), 16, 0, 0, 6);");

        // Left: text
        VBox left = new VBox(8);
        left.setPadding(new Insets(28, 32, 28, 32));
        HBox.setHgrow(left, Priority.ALWAYS);

        Label eye = new Label("Autumn Term · Week 6 of 11");
        eye.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #d9b566;");
        Label title = new Label("Institution running nominally");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label sub = new Label("All assessment services operational · 99.98% uptime this term");
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.6);");

        HBox metrics = new HBox(28);
        metrics.setPadding(new Insets(16, 0, 0, 0));
        metrics.getChildren().addAll(
            heroMetric("1,204", "Active sessions", true),
            heroMetric("8,910", "Items delivered today", false),
            heroMetric("0.4s", "Median grade latency", false)
        );

        left.getChildren().addAll(eye, title, sub, metrics);

        // Right: bar chart
        VBox viz = new VBox(10);
        viz.setPadding(new Insets(28, 32, 28, 28));
        viz.setMinWidth(260);
        viz.setAlignment(Pos.TOP_CENTER);

        HBox vizCap = new HBox();
        vizCap.setAlignment(Pos.CENTER_LEFT);
        Label vcl = new Label("Session load · 24h");
        vcl.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.5);");
        HBox.setHgrow(vcl, Priority.ALWAYS);
        Label vcr = new Label("peak 96%");
        vcr.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.5);");
        vizCap.getChildren().addAll(vcl, vcr);

        HBox bars = new HBox(3);
        bars.setAlignment(Pos.BOTTOM_CENTER);
        bars.setMinHeight(80);
        for (int i = 0; i < LOAD.length; i++) {
            Region bar = new Region();
            bar.setMinWidth(14);
            bar.setPrefWidth(14);
            bar.setMinHeight(LOAD[i] * 0.8);
            String color = i < 7 ? "rgba(255,255,255,0.15)" : "#3d55b5";
            bar.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 3 3 0 0;");
            HBox.setHgrow(bar, Priority.NEVER);
            bars.getChildren().add(bar);
        }

        HBox axis = new HBox();
        Label a1 = new Label("00:00");
        a1.setStyle("-fx-font-size: 9px; -fx-text-fill: rgba(255,255,255,0.35);");
        HBox.setHgrow(a1, Priority.ALWAYS);
        Label a2 = new Label("12:00");
        a2.setStyle("-fx-font-size: 9px; -fx-text-fill: rgba(255,255,255,0.35);");
        HBox.setHgrow(a2, Priority.ALWAYS);
        Label a3 = new Label("now");
        a3.setStyle("-fx-font-size: 9px; -fx-text-fill: rgba(255,255,255,0.35);");
        axis.getChildren().addAll(a1, a2, a3);

        viz.getChildren().addAll(vizCap, bars, axis);
        hero.getChildren().addAll(left, viz);
        return hero;
    }

    private VBox heroMetric(String value, String label, boolean live) {
        VBox m = new VBox(2);
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox kRow = new HBox(5);
        kRow.setAlignment(Pos.CENTER_LEFT);
        if (live) {
            Region dot = new Region();
            dot.setMinSize(6, 6); dot.setPrefSize(6, 6); dot.setMaxSize(6, 6);
            dot.setStyle("-fx-background-color: #2e6b4f; -fx-background-radius: 999;");
            kRow.getChildren().add(dot);
        }
        Label k = new Label(label);
        k.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.5);");
        kRow.getChildren().add(k);
        m.getChildren().addAll(v, kRow);
        return m;
    }

    private VBox createFeed() {
        AcCard card = new AcCard("Governance activity", new AcButton("Audit log", AcButton.Variant.GHOST, AcButton.Size.SM,
            null, AcIcon.create("arrow-right", 14, Color.web("#3b3f4c"))), "sm", false);

        VBox items = new VBox();
        for (FeedItem f : FEED) {
            HBox row = new HBox(12);
            row.setPadding(new Insets(12, 0, 12, 0));
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
            row.setAlignment(Pos.TOP_LEFT);

            String bgColor = switch (f.tone) {
                case "success" -> "#e6f4ed";
                case "danger" -> "#fce8e8";
                case "gold" -> "#fef6e6";
                default -> "#eaedfa";
            };
            String fgColor = switch (f.tone) {
                case "success" -> "#2e6b4f";
                case "danger" -> "#9b2c2c";
                case "gold" -> "#985f1f";
                default -> "#2c40a0";
            };
            StackPane iconBox = new StackPane();
            iconBox.setMinSize(32, 32); iconBox.setPrefSize(32, 32); iconBox.setMaxSize(32, 32);
            iconBox.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");
            iconBox.setAlignment(Pos.CENTER);
            iconBox.getChildren().add(AcIcon.create(f.icon, 15, Color.web(fgColor)));

            VBox textBox = new VBox(3);
            HBox.setHgrow(textBox, Priority.ALWAYS);
            Label txt = new Label(f.text);
            txt.setWrapText(true);
            txt.setStyle("-fx-font-size: 13.5px; -fx-text-fill: #3b3f4c;");
            Label time = new Label(f.time);
            time.setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;");
            textBox.getChildren().addAll(txt, time);

            row.getChildren().addAll(iconBox, textBox);
            items.getChildren().add(row);
        }
        card.getBody().getChildren().add(items);

        VBox wrapper = new VBox();
        wrapper.getChildren().add(card);
        return wrapper;
    }

    private VBox createSide() {
        VBox side = new VBox(16);

        // Approvals
        AcCard approvalCard = new AcCard("Pending approvals",
            new AcBadge(String.valueOf(APPROVALS.length), AcBadge.Variant.WARNING), "sm", false);

        VBox approvalList = new VBox();
        for (Approval a : APPROVALS) {
            HBox row = new HBox(11);
            row.setPadding(new Insets(10, 0, 10, 0));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");

            AcAvatar avatar = new AcAvatar(a.name, AcAvatar.Size.SM);
            VBox txt = new VBox();
            HBox.setHgrow(txt, Priority.ALWAYS);
            Label n = new Label(a.name);
            n.setStyle("-fx-font-size: 13.5px; -fx-font-weight: bold; -fx-text-fill: #1b1d25;");
            Label m = new Label(a.desc);
            m.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");
            txt.getChildren().addAll(n, m);

            HBox btns = new HBox(4);
            btns.getChildren().addAll(
                new AcIconButton(AcIcon.create("check", 14, Color.web("#2e6b4f")), "Approve", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM),
                new AcIconButton(AcIcon.create("x", 14, Color.web("#565a68")), "Decline", AcIconButton.Variant.GHOST, AcIconButton.Size.SM)
            );

            row.getChildren().addAll(avatar, txt, btns);
            approvalList.getChildren().add(row);
        }
        approvalCard.getBody().getChildren().add(approvalList);

        // Cohort health
        AcCard healthCard = new AcCard("Cohort health",
            new Label("avg score") {{ setStyle("-fx-font-size: 11px; -fx-text-fill: #8c909e;"); }},
            "sm", false);

        VBox bars = new VBox(14);
        bars.getChildren().addAll(
            PageKit.barRow("Mathematics", 88, 100, "success", "88%", "9 cohorts"),
            PageKit.barRow("Sciences", 82, 100, "success", "82%", "7 cohorts"),
            PageKit.barRow("Humanities", 76, 100, "gold", "76%", "11 cohorts"),
            PageKit.barRow("Languages", 69, 100, "amber", "69%", "6 cohorts")
        );
        healthCard.getBody().getChildren().add(bars);

        side.getChildren().addAll(approvalCard, healthCard);
        return side;
    }
}
