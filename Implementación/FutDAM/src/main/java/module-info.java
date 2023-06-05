module dev.sergiomisas.futdam {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires koin.logger.slf4j;
    requires org.slf4j;
    requires io.github.microutils.kotlinlogging;
    requires java.sql;
    requires com.google.gson;
    requires kotlin.result.jvm;


    opens dev.sergiomisas.futdam to javafx.fxml;
    exports dev.sergiomisas.futdam;

    opens dev.sergiomisas.futdam.controllers to javafx.fxml;
    exports dev.sergiomisas.futdam.controllers;

    opens dev.sergiomisas.futdam.models to javafx.fxml;
    exports dev.sergiomisas.futdam.models;

    opens dev.sergiomisas.futdam.dto to com.google.gson;
    exports dev.sergiomisas.futdam.dto;

}
