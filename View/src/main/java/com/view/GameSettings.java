package com.view;

import java.io.IOException;
import javafx.fxml.FXML;

import com.mycompany.sudoku.DifficultyLevel;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 * This controller sets a difficulty level of the game and launches it.
 */
public class GameSettings implements Initializable {

    @FXML
    private Label setLevelLabel;
    @FXML
    private Label selectedLevelLabel;
    @FXML
    private Button startGameButton;
    @FXML
    private ToggleButton buttonSimple;
    @FXML
    private ToggleButton buttonMedium;
    @FXML
    private ToggleButton buttonHard;
    
    private ResourceBundle bundle;
    private ResourceBundle tips;

    private DifficultyLevel level;

    /**
     * In FXML, we declared a toggle group for level buttons.When pressed, a
     * source is identified and a corresponding level is set: SIMPLE, MEDIUM or
     * HARD
     *
     * @param e a mouse click
     */
    public void setLevel(ActionEvent e) {

        ToggleButton source = (ToggleButton) e.getSource();

        if (source == buttonSimple) {
            level = DifficultyLevel.SIMPLE;
        } else if (source == buttonMedium) {
            level = DifficultyLevel.MEDIUM;
        } else {
            level = DifficultyLevel.HARD;
        }

        update();
    }

    @FXML
    public void update() {
        selectedLevelLabel.setText(tips.getString(
                String.valueOf(level)));
        startGameButton.setDisable(false);
    }

    /**
     * After choosing a level and clicking a "Play" button, a new view with a
     * Sudoku board is loaded.
     *
     * A new instance of PlayingArea class is created by FXMLLoader, when a
     * loader.load() method is called.
     *
     * loader.getController() returns a creates PlayingArea instance, so we can
     * access its methods
     *
     * @param event is a mouse click
     * @throws IOException
     * @throws CloneNotSupportedException
     */
    @FXML
    public void openPlayingAreaView(ActionEvent event) throws IOException, CloneNotSupportedException {

        FXMLLoader loader = FXMLHandler.getFXMLLoader(getClass().getResource("playingArea.fxml"), bundle);

        Parent parent = loader.load();
        PlayingArea area = loader.getController();
        area.getUserBoard().prepareBoardForGame(level);
        area.saveOriginalBoard();
        area.displayUserBoard();

        Scene scene = new Scene(parent);
        scene.getStylesheets().addAll(this.getClass().getResource("playingAreaCSS.css").toExternalForm());

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    public void setListResources() {

        if ("EN".equals(bundle.getString("lang"))) {
            tips = ResourceBundle.getBundle("com.view.tips.Tips", new Locale("en", "EN"));
        } else {
            tips = ResourceBundle.getBundle("com.view.tips.Tips", new Locale("pl", "PL"));
        }
    }

    public void updateStrings() {
        setLevelLabel.setText(bundle.getString("levelSet"));
        startGameButton.setText(bundle.getString("buttonStart"));
        buttonSimple.setText(bundle.getString("levelSimple"));
        buttonMedium.setText(bundle.getString("levelMedium"));
        buttonHard.setText(bundle.getString("levelHard"));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        updateStrings();
        setListResources();
    }

    public void showTerminatorBox() {
        TerminatorBox box = new TerminatorBox();
        box.displayAlert("Terminator", bundle);
    }
   
}
