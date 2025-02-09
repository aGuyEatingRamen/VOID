module com.example.yessir {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.prefs;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.mail;

    opens com.example.yessir to javafx.fxml;
    exports com.example.yessir;
}