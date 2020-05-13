package com.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TerminatorBox implements Initializable  {
    
     private ResourceBundle bundle;
    
    @FXML 
    private Button tButton;
    
    @FXML 
    private Label warning;
   
    public void displayAlert(String title, ResourceBundle bundle) {
        
        try {
            FXMLLoader loader = FXMLHandler.getFXMLLoader(getClass().getResource("terminatorBox.fxml"), bundle);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            scene.getStylesheets().addAll(this.getClass().getResource("terminatorBoxCSS.css").toExternalForm());
            Stage window = new Stage();
            window.setResizable(false);
            window.initStyle(StageStyle.UNDECORATED);
            window.setScene(scene);
            window.setTitle(title);
            window.initModality(Modality.APPLICATION_MODAL);
            window.show();

        } catch (IOException o) {
            o.getStackTrace();
        }
    }
    
    @FXML
    public void closeTerminator(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    
    public void updateStrings() {
        warning.setText(bundle.getString("terminator"));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        updateStrings();
    }
    
}
