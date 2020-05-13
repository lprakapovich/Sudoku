module com.view {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.view to javafx.fxml;
    exports com.view;
    requires com.mycompany;
    
}