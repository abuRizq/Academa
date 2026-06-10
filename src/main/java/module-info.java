module com.academa {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.academa to javafx.fxml;
    opens com.academa.components to javafx.fxml;
    opens com.academa.screens to javafx.fxml;

    exports com.academa;
    exports com.academa.components;
    exports com.academa.screens;
}
