package com.academa;

/**
 * Launcher class — entry point for non-modular JAR execution.
 * Delegates to AcademaApp.main().
 */
public final class AcademaLauncher {
    private AcademaLauncher() {}
    public static void main(String[] args) {
        AcademaApp.main(args);
    }
}
