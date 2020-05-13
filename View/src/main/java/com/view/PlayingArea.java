package com.view;

import com.mycompany.sudoku.BacktrackingSudokuSolver;
import com.mycompany.sudoku.Dao;
import com.mycompany.sudoku.SudokuBoard;
import com.mycompany.sudoku.SudokuBoardDaoFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * userBoard is a list of 81 buttons, it is updated in a run time due to binding
 * (implementation is below) originalBoard is not a updated, it only stores
 * initially generated board
 *
 *
 * Game is started when a field is selected. Then, you should choose an input
 * value. This order implies to the whole game (select field -> select input).
 *
 */
public class PlayingArea implements Initializable {

    // used for convenience when boards are displayed
    private int counter = 0;

    private SudokuBoard userBoard;
    private SudokuBoard originalBoard;

    private boolean gameStarted;

    SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
    Dao<SudokuBoard> dao;

    private File selectedFile;

    private ResourceBundle bundle;

    private String alertBoxMessage;
    private String alertBoxTitle;

    @FXML
    private String userInput;
    @FXML
    private int selectedFieldNum;
    @FXML
    private List<Button> boardButtons;
    @FXML
    private List<ToggleButton> userInputButtons;

    // needs to be initialized because we use it in saveToFile()
    @FXML
    private Button saveToFileButton = new Button();
    @FXML
    private ToggleButton deleteButton;
    @FXML
    private Button backToStartButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label message;

    @FXML
    public void displayUserBoard() {

        for (int i = 0; i < boardButtons.size(); i++) {
            if (userBoard.get(i) != 0) {
                boardButtons.get(i).setText(String.valueOf(userBoard.get(i)));
                boardButtons.get(i).getStyleClass().add("predefinedValueBoxes");
            } else {
                boardButtons.get(i).setText(null);
                boardButtons.get(i).getStyleClass().add("toBeFilledBoxes");
            }
        }
    }

    @FXML
    public void saveToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Java files", "*.ser"));
        Stage stage = (Stage) saveToFileButton.getScene().getWindow();
        selectedFile = fileChooser.showSaveDialog(stage);

        checkForNull(selectedFile);
        writeToFile();
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

    /**
     * I have changed implementation of Dao for convenience (I guess it's not so
     * important), now it returns a List from read() method, and accepts two
     * boards instead of one in write() method
     *
     * So we avoid redundant repetitions :)
     */
    public void writeToFile() {
        dao = factory.getFileDao(selectedFile.getName());
        dao.write(userBoard, originalBoard);
    }

    public void readFromFile(String fileName) {

        dao = factory.getFileDao(fileName);
        List<SudokuBoard> boards = dao.read();

        userBoard = boards.get(0);
        originalBoard = boards.get(1);

//         System.out.println("---------------------------------- \n" 
//                + "Original board from readFromFile() \n" + 
//                originalBoard.toString());
//         
//        System.out.println(
//                "User board from readFromFile() \n" + 
//                userBoard.toString()
//                + " ----------------------------------");         
    }

    public SudokuBoard getUserBoard() {
        return userBoard;
    }

    public void saveOriginalBoard() throws CloneNotSupportedException {
        originalBoard = userBoard.clone();
    }

    @FXML
    public void resetToOriginal() throws CloneNotSupportedException {
        userBoard = originalBoard.clone();
        setUpEventHandlers();
        displayUserBoard();
    }

    public void handleDeleteButton() {
        if (originalBoard.get(selectedFieldNum) == 0 && gameStarted) {
            boardButtons.get(selectedFieldNum).setText(null);
        }
    }

    /**
     * I want to break it into separate functions later, but in general there's
     * no need since we always invoke it as a whole to bind the properties and
     * set event handlers
     *
     * -> is a lambda expression
     */
    public void setUpEventHandlers() {

        /**
         * When a field is selected, we determine its index, and the game
         * starts.
         */
        EventHandler<ActionEvent> sudokuFieldEvent = (ActionEvent e) -> {
            for (Button button : boardButtons) {
                if (button == (Button) e.getSource()) {
                    gameStarted = true;
                    selectedFieldNum = boardButtons.indexOf(button);
                }
            }
        };

        /**
         * Then, we pick up an input value. If allowed, this value is inserted
         * into the board. A corresponding button label is updated.
         */
        EventHandler<ActionEvent> inputChoiceEvent = (ActionEvent e) -> {
            ToggleButton sourceButton = (ToggleButton) e.getSource();
            userInput = sourceButton.getText();

            if (originalBoard.get(selectedFieldNum) == 0 && gameStarted) {

                System.out.println("User Input: " + userInput);
                boardButtons.get(selectedFieldNum).setText(userInput);
                System.out.println(
                        "STEP " + (++counter) + "\n"
                        + "Original Board: \n" + originalBoard.toString() + "\n"
                        + "User Board: \n" + userBoard.toString());
            }
        };

        userInputButtons.forEach((button) -> {
            button.getStyleClass().add("userInputToggleButtons");
            button.setOnAction(inputChoiceEvent);
        });

        boardButtons.forEach((button) -> {
            button.setOnAction(sudokuFieldEvent);
        });

        /**
         * Binding.
         *
         * We need a converter since button.textProperty() returns a string and
         * the property we are binding it to (field value) is an integer.
         *
         * Bidirectional binding is between button.textProperty() (button label)
         * and a fieldValue.integerProperty() (we create it using
         * JavaBeansBuilder). When a inputChoiceEvent changes a button label, a
         * userBoard field is updated.
         */
        StringConverter converter = new IntegerStringConverter();

        boardButtons.forEach((button) -> {
            try {
                int index = boardButtons.indexOf(button);
                IntegerProperty fieldValueProperty = new JavaBeanIntegerPropertyBuilder()
                        .bean(userBoard.getField(index)) // create a property for each field
                        .name("fieldValue").build();     // associate it with a fieldValue 

                button.textProperty().bindBidirectional(fieldValueProperty, converter);
//                
//                fieldValueProperty.addListener(new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                        if (gameStarted)
//                            System.out.println("fieldValueProperty changed to: " + fieldValueProperty);
//                    }
//                });

            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void updateStrings() {
        backToStartButton.setText(bundle.getString("buttonBackToStart"));
        saveToFileButton.setText(bundle.getString("buttonSaveToFile"));
        deleteButton.setText(bundle.getString("buttonDelete"));
        resetButton.setText(bundle.getString("buttonReset"));
        message.setText(bundle.getString("message"));
        alertBoxMessage = (bundle.getString("saveAlertMessage"));
        alertBoxTitle = (bundle.getString("saveAlertTitle"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        userBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        userBoard.solveGame();
        bundle = rb;
        setUpEventHandlers();
        updateStrings();
    }

    @FXML
    public void openSettingsView(ActionEvent event) throws IOException {

        FXMLLoader loader = FXMLHandler.getFXMLLoader(getClass().getResource("gameStart.fxml"), bundle);
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 504, 731);
        scene.getStylesheets().addAll(this.getClass().getResource("gameStartCSS.css").toExternalForm());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

}
