package com.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    private static final int windowHeight = 731;
    private static final int windowWidth = 504;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException { //this method is called by launch()
        stage.setTitle("The Best Sudoku You Have Ever Played");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.language", setInitialLanguage());
        Parent root = FXMLLoader.load(getClass().getResource("gameStart.fxml"), bundle);
        scene = new Scene(root, windowWidth, windowHeight);

        scene.getStylesheets().addAll(this.getClass().getResource("gameStartCSS.css").toExternalForm()); //linking css file
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public Locale setInitialLanguage() {
        Locale plLocale = new Locale("pl", "PL");
        if (Locale.getDefault().equals(plLocale)) {
            return plLocale;
        } else { //if lang is different than PL
            Locale enLocale = new Locale("en", "EN");
            return enLocale;
        }
    }

    public static void main(String[] args) {
        launch(); //method from Application class; it "starts" the program as JavaFX app
    }

}

//stage - the entire window of the app
//scene - the contents of that window
//stageName.setScene(sceneName) - set the stage to display particular scene
