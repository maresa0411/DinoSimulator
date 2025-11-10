package de.ossenbeck.dinosimulator;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.Optional;

public class DinoSimulatorStage extends Stage {

    private Territory territory;
    private DinoSimulatorPane dinoSimulatorPane;
    private Label messageLabel;
    private static final int GAP = 10;
    private static final ImageView dinoWithBones;
    private static final int MAX_ROWS_COLS = 100;

    static{
        dinoWithBones = new ImageView("TrexWithBone.png");
        dinoWithBones.setFitWidth(50);
        dinoWithBones.setFitHeight(50);
    }

    public DinoSimulatorStage(Territory territory){
        this.territory = territory;
        territory.initTest();

        // GUI menubar
        MenuBar menuBar = new MenuBar();

        // GUI editor menu
        Menu editorMenu = new Menu("_Editor");

        MenuItem newMenuItem = new MenuItem("_Neu");
        newMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
        newMenuItem.setGraphic(new ImageView("New16.gif"));

        MenuItem openMenuItem = new MenuItem("_Öffnen");
        openMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
        openMenuItem.setGraphic(new ImageView("Open16.gif"));

        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        MenuItem compileMenuItem = new MenuItem("_Kompilieren");
        compileMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+K"));

        MenuItem printEditorMenuItem = new MenuItem("_Drucken");
        printEditorMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+P"));
        printEditorMenuItem.setGraphic(new ImageView("Print16.gif"));

        SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();

        MenuItem stopMenuItem = new MenuItem("_Beenden");
        stopMenuItem.setOnAction(_ -> Platform.exit());
        stopMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Q"));

        editorMenu.getItems().addAll(newMenuItem, openMenuItem, separatorMenuItem1,compileMenuItem, printEditorMenuItem, separatorMenuItem2,stopMenuItem);

        // GUI territorium menu
        Menu territoriumMenu = new Menu("_Territorium");

        Menu saveMenu = new Menu("_Speichern");
        MenuItem saveXMLMenuItem = new MenuItem("XML");
        MenuItem saveJAXBMenuItem = new MenuItem("JAXB");
        MenuItem serializeMenuItem = new MenuItem("Serialisieren");
        saveMenu.getItems().addAll(saveXMLMenuItem, saveJAXBMenuItem, serializeMenuItem);

        Menu loadMenu = new Menu("_Laden");
        MenuItem loadXMLMenuItem = new MenuItem("XML");
        MenuItem loadJAXBMenuItem = new MenuItem("JAXB");
        MenuItem deserializeMenuItem = new MenuItem("Deserialisieren");
        loadMenu.getItems().addAll(loadXMLMenuItem, loadJAXBMenuItem, deserializeMenuItem);

        Menu savePictureMenu = new Menu("Als _Bild speichern");

        MenuItem printTerritoriumMenuItem = new MenuItem("_Drucken");
        MenuItem changeSizeMenuItem = new MenuItem("_Größe ändern");
        changeSizeMenuItem.setOnAction(_ -> changeSizeDialog());

        SeparatorMenuItem separatorMenuItem3 = new SeparatorMenuItem();

        CheckMenuItem placeDinoCheckMenuItem = new CheckMenuItem("_Dino platzieren");
        placeDinoCheckMenuItem.setOnAction(_ -> selectAction(Selection.PLACE_DINO));
        CheckMenuItem placeBoneCheckMenuItem = new CheckMenuItem("_Knochen platzieren");
        placeBoneCheckMenuItem.setOnAction(_ -> selectAction(Selection.PLACE_BONE));
        CheckMenuItem placeRockCheckMenuItem = new CheckMenuItem("_Felsen platzieren");
        placeRockCheckMenuItem.setOnAction(_ -> selectAction(Selection.PLACE_ROCK));
        CheckMenuItem deleteCheckMenuItem = new CheckMenuItem("_Kachel löschen");
        deleteCheckMenuItem.setOnAction(_ -> selectAction(Selection.DELETE));

        territoriumMenu.getItems().addAll(saveMenu, loadMenu, savePictureMenu, printTerritoriumMenuItem, changeSizeMenuItem, separatorMenuItem3, placeDinoCheckMenuItem, placeBoneCheckMenuItem, placeRockCheckMenuItem, deleteCheckMenuItem);

        // GUI dino menu
        Menu dinoMenu = new Menu("_Dino");

        MenuItem adjustAmountOfBonesMenuItem = new MenuItem("_Knochenmenge anpassen");
        adjustAmountOfBonesMenuItem.setOnAction(_ -> changeAmountOfBonesDialog());

        MenuItem turnLeftMenuItem = new MenuItem("_linksUm");
        turnLeftMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+L"));
        turnLeftMenuItem.setOnAction(_-> {territory.getDino().turnLeft(); dinoSimulatorPane.printBoard();});

        MenuItem forwardMenuItem = new MenuItem("_vor");
        forwardMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+V"));
        forwardMenuItem.setOnAction(_-> {territory.getDino().moveForward(); dinoSimulatorPane.printBoard();});

        MenuItem pickUpBoneMenuItem = new MenuItem("_nimm");
        pickUpBoneMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+N"));
        pickUpBoneMenuItem.setOnAction(_-> {territory.getDino().pickUpBone(); dinoSimulatorPane.printBoard();});

        MenuItem putDownBoneMenuItem = new MenuItem("_gib");
        putDownBoneMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+G"));
        putDownBoneMenuItem.setOnAction(_-> {territory.getDino().putDownBone(); dinoSimulatorPane.printBoard();});

        dinoMenu.getItems().addAll(adjustAmountOfBonesMenuItem, turnLeftMenuItem, forwardMenuItem, pickUpBoneMenuItem, putDownBoneMenuItem);

        // GUI simulation menu
        Menu simulationMenu = new Menu("_Simulation");
        MenuItem startContinueMenuItem = new MenuItem("Start/_Fortsetzen");
        startContinueMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F11"));
        startContinueMenuItem.setGraphic(new ImageView("Start16.gif"));
        MenuItem pauseMenuItem = new MenuItem("_Pause");
        pauseMenuItem.setGraphic(new ImageView("Pause16.gif"));
        MenuItem stopDinoMenuItem = new MenuItem("_Stopp");
        stopDinoMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F12"));
        stopDinoMenuItem.setGraphic(new ImageView("Stop16.gif"));
        simulationMenu.getItems().addAll(startContinueMenuItem, pauseMenuItem, stopDinoMenuItem);

        // adding menus to menubar
        menuBar.getMenus().addAll(editorMenu, territoriumMenu, dinoMenu, simulationMenu);

        // GUI toolbar
        ToolBar toolBar = new ToolBar();

        Button newButton = new Button();
        newButton.setGraphic(new ImageView("New24.gif"));
        newButton.setTooltip(new Tooltip("Neu"));

        Button loadButton = new Button();
        loadButton.setGraphic(new ImageView("Load24.gif"));
        loadButton.setTooltip(new Tooltip("Laden"));

        Separator vSeparator1 = new Separator(javafx.geometry.Orientation.VERTICAL);

        Button saveButton = new Button();
        saveButton.setGraphic(new ImageView("Save24.gif"));
        saveButton.setTooltip(new Tooltip("Speichern"));

        Button compileButton = new Button();
        compileButton.setGraphic(new ImageView("Compile24.gif"));
        compileButton.setTooltip(new Tooltip("Kompilieren"));

        Separator vSeparator2 = new Separator(javafx.geometry.Orientation.VERTICAL);

        Button adjustSizeButton = new Button();
        adjustSizeButton.setGraphic(new ImageView("Size24.gif"));
        adjustSizeButton.setTooltip(new Tooltip("Größe ändern"));
        adjustSizeButton.setOnAction(_ -> changeSizeDialog());

        ToggleButton placeDinoButton = new ToggleButton();
        placeDinoButton.setOnAction(_ -> selectAction(Selection.PLACE_DINO));
        ImageView imageDino = new ImageView("Trex.png");
        imageDino.setFitWidth(24);
        imageDino.setFitHeight(24);
        placeDinoButton.setGraphic(imageDino);
        placeDinoButton.setTooltip(new Tooltip("Dino platzieren"));

        ToggleButton placeBoneButton = new ToggleButton();
        placeBoneButton.setOnAction(_ -> selectAction(Selection.PLACE_BONE));
        ImageView imageBone = new ImageView("Bone.png");
        imageBone.setFitHeight(24);
        imageBone.setFitWidth(24);
        placeBoneButton.setGraphic(imageBone);
        placeBoneButton.setTooltip(new Tooltip("Knochen platzieren"));

        ToggleButton placeRockButton = new ToggleButton();
        placeRockButton.setOnAction(_ -> selectAction(Selection.PLACE_ROCK));
        ImageView imageRock = new ImageView("Rock.png");
        imageRock.setFitWidth(24);
        imageRock.setFitHeight(24);
        placeRockButton.setGraphic(imageRock);
        placeRockButton.setTooltip(new Tooltip("Felsen platzieren"));

        ToggleButton deleteButton = new ToggleButton();
        deleteButton.setOnAction(_ -> selectAction(Selection.DELETE));
        deleteButton.setGraphic(new ImageView("Delete24.gif"));
        deleteButton.setTooltip(new Tooltip("Löschen"));

        Separator vSeparator3 = new Separator(javafx.geometry.Orientation.VERTICAL);

        Button adjustAmountOfBonesButton = new Button();
        adjustAmountOfBonesButton.setOnAction(_->changeAmountOfBonesDialog());
        ImageView imageTrexWithBone = new ImageView("TrexWithBone.png");
        imageTrexWithBone.setFitHeight(24);
        imageTrexWithBone.setFitWidth(24);
        adjustAmountOfBonesButton.setGraphic(imageTrexWithBone);
        adjustAmountOfBonesButton.setTooltip(new Tooltip("Knochenmenge anpassen"));

        Button turnLeftButton = new Button();
        turnLeftButton.setOnAction(_-> {territory.getDino().turnLeft(); dinoSimulatorPane.printBoard();});
        ImageView imageTrexLeftTurn = new ImageView("TrexLeftTurn.png");
        imageTrexLeftTurn.setFitHeight(24);
        imageTrexLeftTurn.setFitWidth(24);
        turnLeftButton.setGraphic(imageTrexLeftTurn);
        turnLeftButton.setTooltip(new Tooltip("nach links drehen"));

        Button moveForwardButton = new Button();
        moveForwardButton.setOnAction(_-> {territory.getDino().moveForward(); dinoSimulatorPane.printBoard();});
        ImageView imageTrexForward = new ImageView("TrexForward.png");
        imageTrexForward.setFitHeight(24);
        imageTrexForward.setFitWidth(24);
        moveForwardButton.setGraphic(imageTrexForward);
        moveForwardButton.setTooltip(new Tooltip("vorwärts"));

        Button pickUpBoneButton = new Button();
        pickUpBoneButton.setOnAction(_-> {territory.getDino().pickUpBone(); dinoSimulatorPane.printBoard();});
        ImageView imageTrexPickUpBone = new ImageView("TrexPickUpBone.png");
        imageTrexPickUpBone.setFitHeight(24);
        imageTrexPickUpBone.setFitWidth(24);
        pickUpBoneButton.setGraphic(imageTrexPickUpBone);
        pickUpBoneButton.setTooltip(new Tooltip("Knochen aufheben"));

        Button putDownBoneButton = new Button();
        putDownBoneButton.setOnAction(_-> {territory.getDino().putDownBone(); dinoSimulatorPane.printBoard();});
        ImageView imageTrexLayDownBone = new ImageView("TrexLayDownBone.png");
        imageTrexLayDownBone.setFitHeight(24);
        imageTrexLayDownBone.setFitWidth(24);
        putDownBoneButton.setGraphic(imageTrexLayDownBone);
        putDownBoneButton.setTooltip(new Tooltip("Knochen ablegen"));

        Separator vSeparator4 = new Separator(javafx.geometry.Orientation.VERTICAL);

        Button startContinueButton = new Button();
        startContinueButton.setGraphic(new ImageView("Start24.gif"));
        startContinueButton.setTooltip(new Tooltip("Start/Fortsetzen"));

        Button pauseButton = new Button();
        pauseButton.setGraphic(new ImageView("Pause24.gif"));
        pauseButton.setTooltip(new Tooltip("Pause"));

        Button stopButton = new Button();
        stopButton.setGraphic(new ImageView("Stop24.gif"));
        stopButton.setTooltip(new Tooltip("Stopp"));

        Separator vSeparator5 = new Separator(Orientation.VERTICAL);

        Slider slider = new Slider();
        slider.setTooltip(new Tooltip("Geschwindigkeit regulieren"));

        // adding items to toolbar
        toolBar.getItems().addAll(newButton, loadButton, vSeparator1, saveButton, compileButton, vSeparator2,
                adjustSizeButton, placeDinoButton, placeBoneButton, placeRockButton, deleteButton, vSeparator3,
                adjustAmountOfBonesButton, turnLeftButton, moveForwardButton, pickUpBoneButton, putDownBoneButton, vSeparator4,
                startContinueButton, pauseButton, stopButton, vSeparator5, slider);

        // GUI main working space
        TextArea textArea = new TextArea();
        dinoSimulatorPane = new DinoSimulatorPane(territory);
        ScrollPane scrollPane = new ScrollPane(dinoSimulatorPane);
        //scrollPane.setPannable(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(textArea, scrollPane);

        messageLabel = new Label("Willkommen!");

        //synchronizing buttons and menu items
        dinoSimulatorPane.getSelectedActionProperty().addListener(((_, _, newValue) ->
        {
            placeDinoCheckMenuItem.setSelected(newValue == Selection.PLACE_DINO);
            placeBoneCheckMenuItem.setSelected(newValue == Selection.PLACE_BONE);
            placeRockCheckMenuItem.setSelected(newValue == Selection.PLACE_ROCK);
            deleteCheckMenuItem.setSelected(newValue == Selection.DELETE);

            placeDinoButton.setSelected(newValue == Selection.PLACE_DINO);
            placeBoneButton.setSelected(newValue == Selection.PLACE_BONE);
            placeRockButton.setSelected(newValue == Selection.PLACE_ROCK);
            deleteButton.setSelected(newValue == Selection.DELETE);

            switch(newValue){
                case PLACE_DINO -> placeDinoButton.requestFocus();
                case PLACE_BONE -> placeBoneButton.requestFocus();
                case PLACE_ROCK -> placeRockButton.requestFocus();
                case DELETE -> deleteButton.requestFocus();
                case NONE -> new ToggleButton().requestFocus();
            }
        }));

        // assembling all parts of the GUI
        VBox root = new VBox();
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        root.getChildren().addAll(menuBar, toolBar, splitPane, messageLabel);

        setTitle("Dino Simulator");
        setScene(new Scene(root, 800, 500));
    }


    private void selectAction(Selection selection){
        if(dinoSimulatorPane.getSelectedAction() == selection){
            dinoSimulatorPane.setSelectedAction(Selection.NONE);
        }else{
            dinoSimulatorPane.setSelectedAction(selection);
        }
    }

    //StackOverflow (https://stackoverflow.com/questions/31556373/javafx-dialog-with-2-input-fields)
    private void changeSizeDialog(){
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Größe des Territoriums anpassen");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Label rowLabel = new Label("Anzahl der Reihen: ");
        TextField rows = new TextField();
        rows.setPromptText("1-"+MAX_ROWS_COLS);

        Label colLabel = new Label("Anzahl der Spalten: ");
        TextField cols = new TextField();
        cols.setPromptText("1-"+MAX_ROWS_COLS);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.add(rowLabel, 0, 0);
        gridPane.add(rows, 1, 0);
        gridPane.add(colLabel, 0, 1);
        gridPane.add(cols, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        BooleanBinding invalidInput = Bindings.createBooleanBinding(()-> (!isValidRowColInput(rows.getText()) || !isValidRowColInput(cols.getText())), rows.textProperty(), cols.textProperty());
        okButton.disableProperty().bind(invalidInput);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new Pair<>(Integer.parseInt(rows.getText()), Integer.parseInt(cols.getText()));
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        if(result.isPresent()){
            int r = result.get().getKey();
            int c = result.get().getValue();
            territory.resize(r,c);
            dinoSimulatorPane.printBoard();
        }

    }

    private boolean isValidRowColInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value > 0 && value <= MAX_ROWS_COLS;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void changeAmountOfBonesDialog(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Knochenmenge anpassen");
        dialog.setHeaderText("Gib die gewünschte Anzahl an Knochen ein (0-100)");
        dialog.setGraphic(dinoWithBones);
        dialog.getEditor().setPromptText("0-"+ Dino.getMaxBones());

        // mit ChatGPT
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        BooleanBinding invalidInput = Bindings.createBooleanBinding(() -> !isValidBoneInput(dialog.getEditor().getText()), dialog.getEditor().textProperty());
        okButton.disableProperty().bind(invalidInput);
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(s -> territory.getDino().setAmountOfBones(Integer.parseInt(s)));
    }

    private boolean isValidBoneInput(String input){
        try{
            int value = Integer.parseInt(input);
            return value >= 0 && value <= Dino.getMaxBones();
        }catch(NumberFormatException e){
            return false;
        }
    }
}
