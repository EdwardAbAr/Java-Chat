module com.example.ejemplo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ejemplo to javafx.fxml;
    exports com.example.ejemplo;
}