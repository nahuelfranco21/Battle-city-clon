module com.tp1.tp1yabc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires javafx.media;


    opens com.tp1.tp1yabc to javafx.fxml;
    exports com.tp1.tp1yabc;
}