package de.ossenbeck.dinosimulator;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // VM Args: --module-path "\path\to\javafx-sdk-24\lib" --add-modules javafx.controls,javafx.fxml
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Territory territory = new Territory();
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

        SeparatorMenuItem separatorMenuItem3 = new SeparatorMenuItem();

        CheckMenuItem placeDinoCheckMenuItem = new CheckMenuItem("_Dino platzieren");
        CheckMenuItem placeBoneCheckMenuItem = new CheckMenuItem("_Knochen platzieren");
        CheckMenuItem placeRockCheckMenuItem = new CheckMenuItem("_Felsen platzieren");
        CheckMenuItem removeTileCheckMenuItem = new CheckMenuItem("_Kachel löschen");

        territoriumMenu.getItems().addAll(saveMenu, loadMenu, savePictureMenu, printTerritoriumMenuItem, changeSizeMenuItem, separatorMenuItem3, placeDinoCheckMenuItem, placeBoneCheckMenuItem, placeRockCheckMenuItem, removeTileCheckMenuItem);

        // GUI dino menu
        Menu dinoMenu = new Menu("_Dino");
        MenuItem eatBoneMenuItem = new MenuItem("_Knochen essen");
        MenuItem turnLeftMenuItem = new MenuItem("_linksUm");
        turnLeftMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+L"));
        MenuItem forwardMenuItem = new MenuItem("_vor");
        forwardMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+V"));
        MenuItem takeMenuItem = new MenuItem("_nimm");
        takeMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+N"));
        MenuItem leaveMenuItem = new MenuItem("_gib");
        leaveMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+G"));

        dinoMenu.getItems().addAll(eatBoneMenuItem, turnLeftMenuItem, forwardMenuItem, takeMenuItem, leaveMenuItem);

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

        Separator vSeparator1 = new Separator(Orientation.VERTICAL);

        Button saveButton = new Button();
        saveButton.setGraphic(new ImageView("Save24.gif"));
        saveButton.setTooltip(new Tooltip("Speichern"));

        Button compileButton = new Button();
        compileButton.setGraphic(new ImageView("Compile24.gif"));
        compileButton.setTooltip(new Tooltip("Kompilieren"));

        Separator vSeparator2 = new Separator(Orientation.VERTICAL);

        Button adjustSizeButton = new Button();
        adjustSizeButton.setGraphic(new ImageView("Size24.gif"));
        adjustSizeButton.setTooltip(new Tooltip("Größe ändern"));

        Button placeDinoButton = new Button();
        ImageView imageDino = new ImageView("Trex.png");
        imageDino.setFitWidth(24);
        imageDino.setFitHeight(24);
        placeDinoButton.setGraphic(imageDino);
        placeDinoButton.setTooltip(new Tooltip("Dino platzieren"));

        Button addBoneButton = new Button();
        ImageView imageBone = new ImageView("Bone.png");
        imageBone.setFitHeight(24);
        imageBone.setFitWidth(24);
        addBoneButton.setGraphic(imageBone);
        addBoneButton.setTooltip(new Tooltip("Knochen platzieren"));

        Button addRockButton = new Button();
        ImageView imageRock = new ImageView("Rock.png");
        imageRock.setFitWidth(24);
        imageRock.setFitHeight(24);
        addRockButton.setGraphic(imageRock);
        addRockButton.setTooltip(new Tooltip("Felsen platzieren"));

        Button deleteButton = new Button();
        deleteButton.setGraphic(new ImageView("Delete24.gif"));
        deleteButton.setTooltip(new Tooltip("Löschen"));

        Separator vSeparator3 = new Separator(Orientation.VERTICAL);

        Button eatBoneButton = new Button();
        ImageView imageTrexWithBone = new ImageView("TrexWithBone.png");
        imageTrexWithBone.setFitHeight(24);
        imageTrexWithBone.setFitWidth(24);
        eatBoneButton.setGraphic(imageTrexWithBone);
        eatBoneButton.setTooltip(new Tooltip("Knochen essen"));

        Button turnLeftButton = new Button();
        ImageView imageTrexLeftTurn = new ImageView("TrexLeftTurn.png");
        imageTrexLeftTurn.setFitHeight(24);
        imageTrexLeftTurn.setFitWidth(24);
        turnLeftButton.setGraphic(imageTrexLeftTurn);
        turnLeftButton.setTooltip(new Tooltip("nach links drehen"));

        Button moveForwardButton = new Button();
        ImageView imageTrexForward = new ImageView("TrexForward.png");
        imageTrexForward.setFitHeight(24);
        imageTrexForward.setFitWidth(24);
        moveForwardButton.setGraphic(imageTrexForward);
        moveForwardButton.setTooltip(new Tooltip("vorwärts"));

        Button pickUpBoneButton = new Button();
        ImageView imageTrexPickUpBone = new ImageView("TrexPickUpBone.png");
        imageTrexPickUpBone.setFitHeight(24);
        imageTrexPickUpBone.setFitWidth(24);
        pickUpBoneButton.setGraphic(imageTrexPickUpBone);
        pickUpBoneButton.setTooltip(new Tooltip("Knochen aufheben"));

        Button placeBoneButton = new Button();
        ImageView imageTrexLayDownBone = new ImageView("TrexLayDownBone.png");
        imageTrexLayDownBone.setFitHeight(24);
        imageTrexLayDownBone.setFitWidth(24);
        placeBoneButton.setGraphic(imageTrexLayDownBone);
        placeBoneButton.setTooltip(new Tooltip("Knochen ablegen"));

        Separator vSeparator4 = new Separator(Orientation.VERTICAL);

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
                adjustSizeButton, placeDinoButton, addBoneButton, addRockButton, deleteButton, vSeparator3,
                eatBoneButton, turnLeftButton, moveForwardButton, pickUpBoneButton, placeBoneButton, vSeparator4,
                startContinueButton, pauseButton, stopButton, vSeparator5, slider);

        // GUI main working space
        TextArea textArea = new TextArea();
        ScrollPane scrollPane = new ScrollPane(new DinoSimulatorPane(territory));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(textArea, scrollPane);

        Label label = new Label("Willkommen!");

        // assembling all parts of the GUI
        VBox root = new VBox();
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        root.getChildren().addAll(menuBar, toolBar, splitPane, label);

        // building stage to display GUI
        primaryStage.setTitle("Dino Simulator");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
}