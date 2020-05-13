package com.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This controller is launched first, and user chooses whether he continues a
 * previously saved game or launches a new one.
 *
 * Language settings can also be set here.
 */
public class GameStart implements Initializable {

    private File file;
    private ResourceBundle bundle;

    private String alertBoxMessage;
    private String alertBoxTitle;

    @FXML
    private ToggleButton continueButton;
    @FXML
    private ToggleButton newGameButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Button enButton;
    @FXML
    private Button plButton;

    @FXML
    public void openFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Java files", "*.ser"));
        Stage stage = (Stage) continueButton.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        checkForNull(file);
    }

    public void checkForNull(File file) {
        if (file == null) {
            showAlertBox();
        }
    }

    public void showAlertBox() {
        try {
            FXMLLoader loader = FXMLHandler.getFXMLLoader(getClass().getResource("alertBox.fxml"), bundle);
            Parent root = loader.load();

            AlertBox alertBox = loader.getController();
            alertBox.setLabelText(alertBoxMessage);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            root.getStylesheets().addAll(this.getClass().getResource("alertBoxCSS.css").toExternalForm());
            stage.setResizable(false);
            stage.setTitle(alertBoxTitle);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadNextView(ActionEvent event) throws IOException {

        Scene scene;
        Parent parent;

        if (event.getSource() == continueButton) {

            openFile();
            FXMLLoader loader = FXMLHandler.getFXMLLoader(
                    getClass().getResource("playingArea.fxml"), bundle);
            parent = (Parent) loader.load();
            scene = new Scene(parent);
            scene.getStylesheets().addAll(this.getClass().getResource("playingArea.css").toExternalForm());

            PlayingArea area = (PlayingArea) loader.getController();
            area.readFromFile(file.getName());
            area.setUpEventHandlers();
            area.displayUserBoard();

        } else {

            FXMLLoader loader = FXMLHandler.getFXMLLoader(getClass().getResource("settings.fxml"), bundle);
            parent = loader.load();
            scene = new Scene(parent);
            scene.getStylesheets().addAll(this.getClass().getResource("settingsCSS.css").toExternalForm());
        }

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    @FXML
    public void switchLanguage(ActionEvent e) {

        plButton.setMouseTransparent(true); //prevents interaction with button but doesn't grey it out like setDisable()
        enButton.setMouseTransparent(true);
        TranslateTransition slideRight = new TranslateTransition(Duration.seconds(0.5));
        TranslateTransition slideLeft = new TranslateTransition(Duration.seconds(0.5));
        slideRight.setToX(56); //node will move 56px right 
        slideLeft.setToX(-56);

        Button source = (Button) e.getSource();
        String id = source.getId(); //changed checking which button is pressed from text to its ID
        if (id.equals("enButton")) {
            bundle = ResourceBundle.getBundle("bundles.language", new Locale("en", "EN"));
            if (enButton.getLayoutX() < 0) {
                slideRight.setNode(enButton);
                slideLeft.setNode(plButton);
                slideRight.play();
                slideLeft.play();
                // remember that TranslateTransition doesn't change the coordinates of the node, so after its done, we need to "update" them manually
                slideRight.setOnFinished(event -> {
                    enButton.setLayoutX(0);
                    enButton.setTranslateX(0);
                    plButton.setMouseTransparent(false); //allows to click the plButton only after enButton finished "hiding"
                });
                slideLeft.setOnFinished(event -> {
                    plButton.setLayoutX(-56);
                    plButton.setTranslateX(0);
                    enButton.setMouseTransparent(false);
                });
            } else { //if no animation took place, enable buttons' interaction
                enButton.setMouseTransparent(false);
                plButton.setMouseTransparent(false);
            }
        } else {
            bundle = ResourceBundle.getBundle("bundles.language", new Locale("pl", "PL"));
            if (plButton.getLayoutX() < 0) {
                slideRight.setNode(plButton);
                slideLeft.setNode(enButton);
                slideRight.play();
                slideLeft.play();
                slideLeft.setOnFinished(event -> {
                    enButton.setLayoutX(-56);
                    enButton.setTranslateX(0);
                    plButton.setMouseTransparent(false);
                });
                slideRight.setOnFinished(event -> {
                    plButton.setLayoutX(0);
                    plButton.setTranslateX(0);
                    enButton.setMouseTransparent(false);
                });
            } else {
                plButton.setMouseTransparent(false);
                enButton.setMouseTransparent(false);
            }
        }
        updateStrings();
    }

    private void updateStrings() {
        welcomeLabel.setText(bundle.getString("welcome"));
        newGameButton.setText(bundle.getString("buttonNewGame"));
        continueButton.setText(bundle.getString("buttonContinue"));
        alertBoxMessage = (bundle.getString("openFileAlertMessage"));
        alertBoxTitle = (bundle.getString("openFileAlertTitle"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        welcomeLabelGradientAnimation();
        setLanguageButtonsPosition();
        bundle = rb;
        updateStrings();
    }

    public void welcomeLabelGradientAnimation() {

        welcomeLabel.getStyleClass().add("animated-gradient");

        ObjectProperty<Color> baseColor = new SimpleObjectProperty<>();

        KeyValue keyValue1 = new KeyValue(baseColor, Color.RED);
        KeyValue keyValue2 = new KeyValue(baseColor, Color.YELLOW);
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, keyValue1);
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(2500), keyValue2);
        Timeline timeline = new Timeline(keyFrame1, keyFrame2);

        baseColor.addListener((obs, oldColor, newColor) -> {
            welcomeLabel.setStyle(String.format("-gradient-base: #%02x%02x%02x; ",
                    (int) (newColor.getRed() * 255),
                    (int) (newColor.getGreen() * 255),
                    (int) (newColor.getBlue() * 255)));
        });

        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setLanguageButtonsPosition() {
//if system lang is polish, PL button is extended. If not, the EN one is.
        Locale plLocale = new Locale("pl", "PL");

        if (Locale.getDefault().equals(plLocale)) {
            plButton.setLayoutX(0);
        } else {
            enButton.setLayoutX(0);
        }
    }

    /*public void rotateButtons(){
    RotateTransition rotate = new RotateTransition();
      rotate.setNode(welcomeLabel);
      rotate.setFromAngle(0);
      rotate.setToAngle(360);
      rotate.setInterpolator(Interpolator.LINEAR);
      rotate.setCycleCount(Timeline.INDEFINITE);
      rotate.setDuration(new Duration(20000));
      rotate.play();
    }
     */
}
