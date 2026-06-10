package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

/**
 * Login screen — split layout with brand panel and sign-in form.
 * Mirrors the React Login component exactly.
 */
public final class LoginScreen extends HBox {

    private Runnable onSignIn;

    public LoginScreen(Runnable onSignIn) {
        this.onSignIn = onSignIn;
        getStyleClass().add("login-root");

        // === Left: Brand panel ===
        VBox brand = createBrandPanel();
        HBox.setHgrow(brand, Priority.ALWAYS);

        // === Right: Form panel ===
        VBox formPanel = createFormPanel();
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        getChildren().addAll(brand, formPanel);
    }

    private VBox createBrandPanel() {
        VBox brand = new VBox();
        brand.getStyleClass().add("login-brand");
        brand.setSpacing(0);

        // Top: Logo
        HBox brandTop = new HBox(11);
        brandTop.setAlignment(Pos.CENTER_LEFT);

        // Logo icon
        StackPane logo = new StackPane();
        Rectangle rect = new Rectangle(34, 34);
        rect.setArcWidth(8); rect.setArcHeight(8);
        rect.setFill(Color.WHITE);
        SVGPath aPath = new SVGPath();
        aPath.setContent("M17 7.4L24 26.6H21.1L19.8 22.4H14.2L12.9 26.6H10L17 7.4ZM17 15.1L15.4 19.1H18.6L17 15.1Z");
        aPath.setFill(Color.web("#2c40a0"));
        aPath.setScaleX(0.9); aPath.setScaleY(0.9);
        logo.getChildren().addAll(rect, aPath);

        Label brandWord = new Label("Academa");
        brandWord.getStyleClass().add("login-brand-word");

        brandTop.getChildren().addAll(logo, brandWord);

        // Mid: Tagline
        VBox brandMid = new VBox(8);
        brandMid.setPadding(new Insets(60, 0, 0, 0));

        Label eyebrow = new Label("ACADEMIC EVALUATION, REFINED");
        eyebrow.getStyleClass().add("login-eyebrow");

        Label heading = new Label("Assessment with the\ngravitas it deserves.");
        heading.getStyleClass().add("login-heading");
        heading.setWrapText(true);
        heading.setMaxWidth(420);

        Label subtitle = new Label("Author rigorous evaluations, run them in a focus-driven environment, and resolve outcomes the instant a learner commits.");
        subtitle.getStyleClass().add("login-subtitle");
        subtitle.setMaxWidth(420);

        brandMid.getChildren().addAll(eyebrow, heading, subtitle);
        VBox.setVgrow(brandMid, Priority.ALWAYS);

        // Bottom: Stats
        HBox stats = new HBox(36);
        stats.getChildren().addAll(
            createStat("142", "institutions"),
            createStat("3.4M", "items resolved"),
            createStat("99.98%", "uptime")
        );

        brand.getChildren().addAll(brandTop, brandMid, stats);
        return brand;
    }

    private VBox createStat(String value, String label) {
        VBox stat = new VBox(2);
        Label v = new Label(value);
        v.getStyleClass().add("login-stat-value");
        Label l = new Label(label);
        l.getStyleClass().add("login-stat-label");
        stat.getChildren().addAll(v, l);
        return stat;
    }

    private VBox createFormPanel() {
        VBox panel = new VBox();
        panel.getStyleClass().add("login-panel");
        panel.setAlignment(Pos.CENTER);

        VBox form = new VBox(0);
        form.setMaxWidth(380);
        form.setPrefWidth(380);

        // Eyebrow
        Label eyebrow = new Label("SIGN IN");
        eyebrow.getStyleClass().addAll("login-eyebrow", "login-eyebrow-dark");

        // Title
        Label title = new Label("Welcome back");
        title.getStyleClass().add("login-form-title");
        VBox.setMargin(title, new Insets(8, 0, 4, 0));

        // Subtitle
        Label sub = new Label("Access resolves to your role automatically.");
        sub.getStyleClass().add("login-form-sub");
        VBox.setMargin(sub, new Insets(0, 0, 26, 0));

        // Email input
        AcInput emailInput = new AcInput("Institutional email", "email", "e.voss@northbridge.edu",
            null, AcIcon.create("mail", 17, Color.web("#8c909e")), null, "md", false);

        // Password input
        AcInput pwInput = new AcInput("Password", "password", "evaluate",
            null, AcIcon.create("lock", 17, Color.web("#8c909e")), null, "md", false);

        VBox.setMargin(emailInput, new Insets(0, 0, 16, 0));
        VBox.setMargin(pwInput, new Insets(0, 0, 16, 0));

        // Keep signed in row
        HBox keepRow = new HBox();
        keepRow.setAlignment(Pos.CENTER_LEFT);
        AcCheckbox keepMe = new AcCheckbox("Keep me signed in", true);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label forgot = new Label("Forgot password?");
        forgot.getStyleClass().add("login-forgot-link");
        keepRow.getChildren().addAll(keepMe, spacer, forgot);
        VBox.setMargin(keepRow, new Insets(0, 0, 16, 0));

        // Sign in button
        AcButton signInBtn = new AcButton("Sign in", AcButton.Variant.PRIMARY, AcButton.Size.LG,
            null, AcIcon.create("arrow-right", 16, Color.WHITE));
        signInBtn.setFullWidth(true);
        signInBtn.setMaxWidth(Double.MAX_VALUE);
        signInBtn.setOnAction(e -> { if (onSignIn != null) onSignIn.run(); });

        // Divider
        HBox divider = new HBox(14);
        divider.setAlignment(Pos.CENTER);
        Separator sep1 = new Separator();
        HBox.setHgrow(sep1, Priority.ALWAYS);
        Label orLabel = new Label("or");
        orLabel.getStyleClass().add("login-divider-text");
        Separator sep2 = new Separator();
        HBox.setHgrow(sep2, Priority.ALWAYS);
        divider.getChildren().addAll(sep1, orLabel, sep2);
        VBox.setMargin(divider, new Insets(22, 0, 16, 0));

        // SSO button
        AcButton ssoBtn = new AcButton("Continue with Institution SSO", AcButton.Variant.SECONDARY, AcButton.Size.MD,
            AcIcon.create("key-round", 16, Color.web("#1b1d25")), null);
        ssoBtn.setFullWidth(true);
        ssoBtn.setMaxWidth(Double.MAX_VALUE);

        // Legal text
        Label legal = new Label("By continuing you agree to the Academa Acceptable Use & Academic Integrity policy.");
        legal.getStyleClass().add("login-legal");
        VBox.setMargin(legal, new Insets(20, 0, 0, 0));

        form.getChildren().addAll(eyebrow, title, sub, emailInput, pwInput, keepRow, signInBtn, divider, ssoBtn, legal);
        panel.getChildren().add(form);
        return panel;
    }
}
