package com.academa.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.function.Consumer;

/**
 * Tabs component — underline or pill tab switcher.
 * Mirrors the React Tabs component.
 */
public final class AcTabs extends HBox {

    public record TabDef(String id, String label, Node icon, Integer count) {
        public TabDef(String id, String label) { this(id, label, null, null); }
        public TabDef(String id, String label, int count) { this(id, label, null, count); }
        public TabDef(String id, String label, Node icon) { this(id, label, icon, null); }
    }

    public enum Variant { UNDERLINE, PILL }

    private String activeId;

    public AcTabs(List<TabDef> tabs, String value, Consumer<String> onChange, Variant variant) {
        this.activeId = value != null ? value : (tabs.isEmpty() ? "" : tabs.get(0).id());

        getStyleClass().add("ac-tabs");
        if (variant == Variant.PILL) {
            getStyleClass().add("ac-tabs-pill");
        } else {
            getStyleClass().add("ac-tabs-underline");
        }
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(2);

        for (TabDef t : tabs) {
            HBox tab = new HBox(7);
            tab.getStyleClass().add("ac-tab");
            tab.setAlignment(Pos.CENTER);
            tab.setCursor(javafx.scene.Cursor.HAND);

            if (t.icon() != null) {
                tab.getChildren().add(t.icon());
            }

            Label lbl = new Label(t.label());
            tab.getChildren().add(lbl);

            if (t.count() != null) {
                Label countLbl = new Label(String.valueOf(t.count()));
                countLbl.getStyleClass().add("ac-tab-count");
                tab.getChildren().add(countLbl);
            }

            if (t.id().equals(activeId)) {
                tab.getStyleClass().add("active");
            }

            tab.setOnMouseClicked(e -> {
                this.activeId = t.id();
                refreshActive(tabs);
                if (onChange != null) onChange.accept(t.id());
            });

            getChildren().add(tab);
        }
    }

    private void refreshActive(List<TabDef> tabs) {
        for (int i = 0; i < getChildren().size(); i++) {
            Node child = getChildren().get(i);
            if (child instanceof HBox hbox) {
                hbox.getStyleClass().remove("active");
                if (i < tabs.size() && tabs.get(i).id().equals(activeId)) {
                    hbox.getStyleClass().add("active");
                }
            }
        }
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String id, List<TabDef> tabs) {
        this.activeId = id;
        refreshActive(tabs);
    }
}
