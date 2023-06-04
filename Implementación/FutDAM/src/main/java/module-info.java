module dev.sergiomisas.futdam {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires java.sql;
    requires com.google.gson;
    requires simple.xml;


    opens dev.sergiomisas.futdam to javafx.fxml;
    exports dev.sergiomisas.futdam;

    opens dev.sergiomisas.futdam.controllers to javafx.fxml;
    exports dev.sergiomisas.futdam.controllers;

}
