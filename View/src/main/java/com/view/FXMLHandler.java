/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;

/**
 * This class is created to handle FXMLLoader instance creation and
 * initialization
 *
 * Instead of calling each time FXMLLoader = new FXMLLoader();
 * loader.setResourses(ResourceBundle b); loader.setLocation(URL url);
 *
 * we can just invoke its static method and return its instance already
 * initialized.
 */
public class FXMLHandler {

    private final FXMLLoader loader;

    public FXMLHandler(URL url, ResourceBundle bundle) {
        loader = new FXMLLoader();
        loader.setLocation(url);
        loader.setResources(bundle);
    }

    public static FXMLLoader getFXMLLoader(URL url, ResourceBundle bundle) {
        return new FXMLLoader(url, bundle);
    }
}
