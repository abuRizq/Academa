package com.academa.components;

import javafx.scene.Group;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import java.util.HashMap;
import java.util.Map;

/**
 * SVG icon system — replaces lucide-react.
 * Each icon is a hand-translated SVG path from the Lucide icon set.
 */
public final class AcIcon {

    private AcIcon() {}

    private static final Map<String, String[]> ICONS = new HashMap<>();

    static {
        // Each entry: icon name -> array of SVG path strings (d attribute)
        // Paths are drawn in a 24x24 viewBox, stroke-based

        ICONS.put("layout-dashboard", new String[]{
            "M3 3h7v9H3zM14 3h7v5h-7zM14 12h7v9h-7zM3 16h7v5H3z"
        });
        ICONS.put("file-pen-line", new String[]{
            "M18 22H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h10l6 6v14",
            "M14 2v6h6",
            "M2 18h7",
            "M18 14l-4 4"
        });
        ICONS.put("circle-check-big", new String[]{
            "M22 11.08V12a10 10 0 1 1-5.93-9.14",
            "M9 11l3 3L22 4"
        });
        ICONS.put("trophy", new String[]{
            "M6 9H4.5a2.5 2.5 0 0 1 0-5H6",
            "M18 9h1.5a2.5 2.5 0 0 0 0-5H18",
            "M4 22h16",
            "M10 22V8a4 4 0 1 1 8 0v14",
            "M8 22h8"
        });
        ICONS.put("list-checks", new String[]{
            "M10 6h11", "M10 12h11", "M10 18h11",
            "M3 6l2 2 4-4", "M3 12l2 2 4-4", "M3 18l2 2 4-4"
        });
        ICONS.put("chart-no-axes-column", new String[]{
            "M12 20V10", "M18 20V4", "M6 20v-4"
        });
        ICONS.put("users", new String[]{
            "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2",
            "M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z",
            "M22 21v-2a4 4 0 0 0-3-3.87",
            "M16 3.13a4 4 0 0 1 0 7.75"
        });
        ICONS.put("shield-check", new String[]{
            "M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z",
            "M9 12l2 2 4-4"
        });
        ICONS.put("settings", new String[]{
            "M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z",
            "M12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8z"
        });
        ICONS.put("search", new String[]{
            "M11 19a8 8 0 1 0 0-16 8 8 0 0 0 0 16z",
            "M21 21l-4.35-4.35"
        });
        ICONS.put("bell", new String[]{
            "M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9",
            "M10.3 21a1.94 1.94 0 0 0 3.4 0"
        });
        ICONS.put("chevrons-up-down", new String[]{
            "M7 15l5 5 5-5", "M7 9l5-5 5 5"
        });
        ICONS.put("check", new String[]{
            "M20 6L9 17l-5-5"
        });
        ICONS.put("play", new String[]{
            "M6 4l14 8-14 8z"
        });
        ICONS.put("arrow-right", new String[]{
            "M5 12h14", "M12 5l7 7-7 7"
        });
        ICONS.put("arrow-left", new String[]{
            "M19 12H5", "M12 19l-7-7 7-7"
        });
        ICONS.put("mail", new String[]{
            "M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z",
            "M22 6l-10 7L2 6"
        });
        ICONS.put("lock", new String[]{
            "M19 11H5a2 2 0 0 0-2 2v7a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2z",
            "M7 11V7a5 5 0 0 1 10 0v4"
        });
        ICONS.put("key-round", new String[]{
            "M2 18v3c0 .6.4 1 1 1h4v-3h3v-3h2l1.4-1.4a6.5 6.5 0 1 0-4-4z",
            "M16.5 7.5a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"
        });
        ICONS.put("x", new String[]{
            "M18 6L6 18", "M6 6l12 12"
        });
        ICONS.put("timer", new String[]{
            "M10 2h4", "M12 14l3-3",
            "M12 22a8 8 0 1 0 0-16 8 8 0 0 0 0 16z"
        });
        ICONS.put("flag", new String[]{
            "M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z",
            "M4 22v-7"
        });
        ICONS.put("send", new String[]{
            "M22 2L11 13", "M22 2l-7 20-4-9-9-4z"
        });
        ICONS.put("plus", new String[]{
            "M12 5v14", "M5 12h14"
        });
        ICONS.put("grip-vertical", new String[]{
            "M9 5h.01M9 12h.01M9 19h.01M15 5h.01M15 12h.01M15 19h.01"
        });
        ICONS.put("trash-2", new String[]{
            "M3 6h18", "M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6",
            "M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2",
            "M10 11v6", "M14 11v6"
        });
        ICONS.put("toggle-left", new String[]{
            "M16 5H8a7 7 0 0 0 0 14h8a7 7 0 0 0 0-14z",
            "M8 12a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"
        });
        ICONS.put("circle-dot", new String[]{
            "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20z",
            "M12 12m-1 0a1 1 0 1 0 2 0 1 1 0 1 0-2 0"
        });
        ICONS.put("waypoints", new String[]{
            "M12 2a3 3 0 0 0-3 3c0 2 3 4 3 4s3-2 3-4a3 3 0 0 0-3-3z",
            "M4 12a3 3 0 0 0-3 3c0 2 3 4 3 4s3-2 3-4a3 3 0 0 0-3-3z",
            "M20 12a3 3 0 0 0-3 3c0 2 3 4 3 4s3-2 3-4a3 3 0 0 0-3-3z"
        });
        ICONS.put("arrow-down-up", new String[]{
            "M3 16l4 4 4-4", "M7 20V4",
            "M21 8l-4-4-4 4", "M17 4v16"
        });
        ICONS.put("text-cursor-input", new String[]{
            "M5 4h2", "M17 4h2",
            "M5 20h2", "M17 20h2",
            "M12 4v16"
        });
        ICONS.put("graduation-cap", new String[]{
            "M22 10v6M2 10l10-5 10 5-10 5z",
            "M6 12v5c0 2 6 3 6 3s6-1 6-3v-5"
        });
        ICONS.put("activity", new String[]{
            "M22 12h-4l-3 9L9 3l-3 9H2"
        });
        ICONS.put("download", new String[]{
            "M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4",
            "M7 10l5 5 5-5",
            "M12 15V3"
        });
        ICONS.put("user-plus", new String[]{
            "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2",
            "M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z",
            "M20 8v6", "M23 11h-6"
        });
        ICONS.put("shield-off", new String[]{
            "M19.7 14a6.7 6.7 0 0 0 .3-2V5l-8-3-3.2 1.2",
            "M2 2l20 20",
            "M4.7 4.7L4 5v7c0 6 8 10 8 10a20 20 0 0 0 5.6-4.8"
        });
        ICONS.put("more-horizontal", new String[]{
            "M12 12h.01M19 12h.01M5 12h.01"
        });
        ICONS.put("chevron-left", new String[]{
            "M15 18l-6-6 6-6"
        });
        ICONS.put("chevron-right", new String[]{
            "M9 18l6-6-6-6"
        });
        ICONS.put("crown", new String[]{
            "M2 4l3 12h14l3-12-5 4-5-4-5 4z",
            "M2 20h20"
        });

        // === NEW ICONS for expanded pages ===
        ICONS.put("target", new String[]{
            "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20z",
            "M12 18a6 6 0 1 0 0-12 6 6 0 0 0 0 12z",
            "M12 14a2 2 0 1 0 0-4 2 2 0 0 0 0 4z"
        });
        ICONS.put("trending-up", new String[]{
            "M22 7l-8.5 8.5-5-5L2 17",
            "M16 7h6v6"
        });
        ICONS.put("trending-down", new String[]{
            "M22 17l-8.5-8.5-5 5L2 7",
            "M16 17h6v-6"
        });
        ICONS.put("minus", new String[]{
            "M5 12h14"
        });
        ICONS.put("gauge", new String[]{
            "M12 22a10 10 0 0 1-7-17",
            "M12 22a10 10 0 0 0 7-17",
            "M12 6v6l4 2"
        });
        ICONS.put("inbox", new String[]{
            "M22 12h-6l-2 3H10l-2-3H2",
            "M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"
        });
        ICONS.put("radio", new String[]{
            "M12 12m-2 0a2 2 0 1 0 4 0 2 2 0 1 0-4 0",
            "M16.24 7.76a6 6 0 0 1 0 8.49",
            "M7.76 16.24a6 6 0 0 1 0-8.49",
            "M19.07 4.93a10 10 0 0 1 0 14.14",
            "M4.93 19.07a10 10 0 0 1 0-14.14"
        });
        ICONS.put("award", new String[]{
            "M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14z",
            "M8.21 13.89L7 23l5-3 5 3-1.21-9.12"
        });
        ICONS.put("library", new String[]{
            "M3 7v10", "M8 4v16", "M13 4v16", "M18 4v16",
            "M21 7v10"
        });
        ICONS.put("triangle-alert", new String[]{
            "M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z",
            "M12 9v4", "M12 17h.01"
        });
        ICONS.put("shield-alert", new String[]{
            "M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z",
            "M12 8v4", "M12 16h.01"
        });
        ICONS.put("file-check-2", new String[]{
            "M4 22h14a2 2 0 0 0 2-2V7l-5-5H6a2 2 0 0 0-2 2v4",
            "M14 2v6h6",
            "M2 15l3 3 4-4"
        });
        ICONS.put("user-x", new String[]{
            "M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2",
            "M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z",
            "M17 6l5 5", "M22 6l-5 5"
        });
        ICONS.put("building-2", new String[]{
            "M6 22V4a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v18z",
            "M6 12H4a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h2",
            "M18 9h2a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2h-2",
            "M10 6h4", "M10 10h4", "M10 14h4", "M10 18h4"
        });
        ICONS.put("file-lock-2", new String[]{
            "M4 22h14a2 2 0 0 0 2-2V7l-5-5H6a2 2 0 0 0-2 2v1",
            "M14 2v6h6",
            "M2 13v-1a2 2 0 1 1 4 0v1",
            "M1 14a1 1 0 0 0-1 1v2a1 1 0 0 0 1 1h4a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1z"
        });
        ICONS.put("plug", new String[]{
            "M12 22v-5", "M9 8V2", "M15 8V2",
            "M18 8v5a6 6 0 0 1-12 0V8z"
        });
        ICONS.put("webhook", new String[]{
            "M18 16.98h1a2 2 0 0 0 0-4h-1",
            "M12 12a4 4 0 1 0-8 0 4 4 0 0 0 8 0z",
            "M10.17 14.83L6 22",
            "M17 2l-4 7h5l-4 7"
        });
        ICONS.put("alarm-clock", new String[]{
            "M12 21a8 8 0 1 0 0-16 8 8 0 0 0 0 16z",
            "M12 9v4l2 2",
            "M5 3L2 6", "M22 6l-3-3"
        });
        ICONS.put("circle-play", new String[]{
            "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20z",
            "M10 8l6 4-6 4z"
        });
        ICONS.put("loader", new String[]{
            "M12 2v4", "M12 18v4",
            "M4.93 4.93l2.83 2.83",
            "M16.24 16.24l2.83 2.83",
            "M2 12h4", "M18 12h4",
            "M4.93 19.07l2.83-2.83",
            "M16.24 7.76l2.83-2.83"
        });
        ICONS.put("pen-line", new String[]{
            "M12 20h9",
            "M16.5 3.5a2.12 2.12 0 0 1 3 3L7 19l-4 1 1-4z"
        });
        ICONS.put("pencil", new String[]{
            "M17 3a2.85 2.85 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5z"
        });
        ICONS.put("calendar", new String[]{
            "M16 2v4", "M8 2v4",
            "M3 10h18",
            "M21 8V6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V8z"
        });
        ICONS.put("upload", new String[]{
            "M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4",
            "M17 8l-5-5-5 5",
            "M12 3v12"
        });
        ICONS.put("layers", new String[]{
            "M12 2L2 7l10 5 10-5-10-5z",
            "M2 17l10 5 10-5",
            "M2 12l10 5 10-5"
        });
        ICONS.put("git-compare", new String[]{
            "M18 21a2 2 0 1 0 0-4 2 2 0 0 0 0 4z",
            "M6 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4z",
            "M6 7v8a2 2 0 0 0 2 2h3",
            "M18 17V9a2 2 0 0 0-2-2h-3"
        });
    }

    /**
     * Create a javafx Group containing SVG path shapes for the named icon.
     * @param name   Lucide-style icon name (kebab-case)
     * @param size   Desired pixel size
     * @param color  Fill/stroke color
     * @return Group node representing the icon, or empty Group if name unknown
     */
    public static Group create(String name, double size, Color color) {
        Group g = new Group();
        String[] paths = ICONS.get(name);
        if (paths == null) {
            // Fallback: render a simple circle as placeholder
            SVGPath p = new SVGPath();
            p.setContent("M12 12m-4 0a4 4 0 1 0 8 0 4 4 0 1 0-8 0");
            p.setFill(null);
            p.setStroke(color);
            p.setStrokeWidth(1.7);
            g.getChildren().add(p);
        } else {
            for (String d : paths) {
                SVGPath p = new SVGPath();
                p.setContent(d);
                if (isFillIcon(name)) {
                    p.setFill(color);
                    p.setStroke(null);
                } else {
                    p.setFill(null);
                    p.setStroke(color);
                    p.setStrokeWidth(1.7);
                    p.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
                    p.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
                }
                g.getChildren().add(p);
            }
        }

        // Scale from 24x24 viewBox to desired size
        double scale = size / 24.0;
        g.getTransforms().add(new Scale(scale, scale));
        return g;
    }

    /** Convenience overload with default color */
    public static Group create(String name, double size) {
        return create(name, size, Color.web("#565a68"));
    }

    /** Some icons look better filled rather than stroked */
    private static boolean isFillIcon(String name) {
        return name.equals("play");
    }
}
