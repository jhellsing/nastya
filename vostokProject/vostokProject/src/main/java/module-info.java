module com.example.vostokproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
   // requires poi;

    opens com.example.vostokproject to javafx.fxml;
    exports com.example.vostokproject;
}