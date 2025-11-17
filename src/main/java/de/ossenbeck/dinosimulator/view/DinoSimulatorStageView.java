package de.ossenbeck.dinosimulator.view;

import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Notifier;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DinoSimulatorStageView extends Stage {
    private final Territory territory;
    private final MessagePane notifier;

    private final MenuItem newMenuItem;
    private final MenuItem openMenuItem;
    private final MenuItem compileMenuItem;
    private final MenuItem printEditorMenuItem;
    private final MenuItem stopMenuItem;
    private final MenuItem saveXMLMenuItem;
    private final MenuItem saveJAXBMenuItem;
    private final MenuItem serializeMenuItem;
    private final MenuItem loadXMLMenuItem;
    private final MenuItem loadJAXBMenuItem;
    private final MenuItem deserializeMenuItem;
    private final MenuItem printTerritoriumMenuItem;
    private final MenuItem changeSizeMenuItem;

    private final CheckMenuItem placeDinoCheckMenuItem;
    private final CheckMenuItem placeBoneCheckMenuItem;
    private final CheckMenuItem placeRockCheckMenuItem;
    private final CheckMenuItem deleteCheckMenuItem;

    private final MenuItem adjustAmountOfBonesMenuItem;
    private final MenuItem turnLeftMenuItem;
    private final MenuItem forwardMenuItem;
    private final MenuItem pickUpBoneMenuItem;
    private final MenuItem putDownBoneMenuItem;

    private final MenuItem startContinueMenuItem;
    private final MenuItem pauseMenuItem;
    private final MenuItem stopDinoMenuItem;

    private final Button newButton;
    private final Button loadButton;
    private final Button saveButton;
    private final Button compileButton;

    private final Button adjustSizeButton;
    private final ToggleButton placeDinoButton;
    private final ToggleButton placeBoneButton;
    private final ToggleButton placeRockButton;
    private final ToggleButton deleteButton;

    private final Button adjustAmountOfBonesButton;
    private final Button turnLeftButton;
    private final Button moveForwardButton;
    private final Button pickUpBoneButton;
    private final Button putDownBoneButton;

    private final Button startContinueButton;
    private final Button pauseButton;
    private final Button stopButton;

    private final Slider slider;
    private final TextArea textArea;

    public DinoSimulatorStageView(Territory territory, DinoSimulatorPaneView pane){
        this.territory = territory;
        this.notifier = new MessagePane();
        // GUI menubar
        MenuBar menuBar = new MenuBar();

        // GUI editor menu
        Menu editorMenu = new Menu("_Editor");

        newMenuItem = new MenuItem("_Neu");
        newMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
        newMenuItem.setGraphic(new ImageView("New16.gif"));

        openMenuItem = new MenuItem("_Öffnen");
        openMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
        openMenuItem.setGraphic(new ImageView("Open16.gif"));

        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        compileMenuItem = new MenuItem("_Kompilieren");
        compileMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+K"));

        printEditorMenuItem = new MenuItem("_Drucken");
        printEditorMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+P"));
        printEditorMenuItem.setGraphic(new ImageView("Print16.gif"));

        SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();

        stopMenuItem = new MenuItem("_Beenden");
        stopMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Q"));

        editorMenu.getItems().addAll(newMenuItem, openMenuItem, separatorMenuItem1,compileMenuItem, printEditorMenuItem, separatorMenuItem2,stopMenuItem);

        // GUI territorium menu
        Menu territoriumMenu = new Menu("_Territorium");

        Menu saveMenu = new Menu("_Speichern");
        saveXMLMenuItem = new MenuItem("XML");
        saveJAXBMenuItem = new MenuItem("JAXB");
        serializeMenuItem = new MenuItem("Serialisieren");
        saveMenu.getItems().addAll(saveXMLMenuItem, saveJAXBMenuItem, serializeMenuItem);

        Menu loadMenu = new Menu("_Laden");
        loadXMLMenuItem = new MenuItem("XML");
        loadJAXBMenuItem = new MenuItem("JAXB");
        deserializeMenuItem = new MenuItem("Deserialisieren");
        loadMenu.getItems().addAll(loadXMLMenuItem, loadJAXBMenuItem, deserializeMenuItem);

        Menu savePictureMenu = new Menu("Als _Bild speichern");

        printTerritoriumMenuItem = new MenuItem("_Drucken");
        changeSizeMenuItem = new MenuItem("_Größe ändern");

        SeparatorMenuItem separatorMenuItem3 = new SeparatorMenuItem();

        placeDinoCheckMenuItem = new CheckMenuItem("_Dino platzieren");
        placeBoneCheckMenuItem = new CheckMenuItem("_Knochen platzieren");
        placeRockCheckMenuItem = new CheckMenuItem("_Felsen platzieren");
        deleteCheckMenuItem = new CheckMenuItem("_Kachel löschen");

        territoriumMenu.getItems().addAll(saveMenu, loadMenu, savePictureMenu, printTerritoriumMenuItem, changeSizeMenuItem, separatorMenuItem3, placeDinoCheckMenuItem, placeBoneCheckMenuItem, placeRockCheckMenuItem, deleteCheckMenuItem);

        // GUI dino menu
        Menu dinoMenu = new Menu("_Dino");

        adjustAmountOfBonesMenuItem = new MenuItem("_Knochenmenge anpassen");

        turnLeftMenuItem = new MenuItem("_linksUm");
        turnLeftMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+L"));

        forwardMenuItem = new MenuItem("_vor");
        forwardMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+V"));

        pickUpBoneMenuItem = new MenuItem("_nimm");
        pickUpBoneMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+N"));

        putDownBoneMenuItem = new MenuItem("_gib");
        putDownBoneMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+SHIFT+G"));

        dinoMenu.getItems().addAll(adjustAmountOfBonesMenuItem, turnLeftMenuItem, forwardMenuItem, pickUpBoneMenuItem, putDownBoneMenuItem);

        // GUI simulation menu
        Menu simulationMenu = new Menu("_Simulation");
        startContinueMenuItem = new MenuItem("Start/_Fortsetzen");
        startContinueMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F11"));
        startContinueMenuItem.setGraphic(new ImageView("Start16.gif"));
        pauseMenuItem = new MenuItem("_Pause");
        pauseMenuItem.setGraphic(new ImageView("Pause16.gif"));
        stopDinoMenuItem = new MenuItem("_Stopp");
        stopDinoMenuItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+F12"));
        stopDinoMenuItem.setGraphic(new ImageView("Stop16.gif"));
        simulationMenu.getItems().addAll(startContinueMenuItem, pauseMenuItem, stopDinoMenuItem);

        // adding menus to menubar
        menuBar.getMenus().addAll(editorMenu, territoriumMenu, dinoMenu, simulationMenu);

        // GUI toolbar
        ToolBar toolBar = new ToolBar();

        newButton = new Button();
        newButton.setGraphic(new ImageView("New24.gif"));
        newButton.setTooltip(new Tooltip("Neu"));

        loadButton = new Button();
        loadButton.setGraphic(new ImageView("Load24.gif"));
        loadButton.setTooltip(new Tooltip("Laden"));

        Separator vSeparator1 = new Separator(javafx.geometry.Orientation.VERTICAL);

        saveButton = new Button();
        saveButton.setGraphic(new ImageView("Save24.gif"));
        saveButton.setTooltip(new Tooltip("Speichern"));

        compileButton = new Button();
        compileButton.setGraphic(new ImageView("Compile24.gif"));
        compileButton.setTooltip(new Tooltip("Kompilieren"));

        Separator vSeparator2 = new Separator(javafx.geometry.Orientation.VERTICAL);

        adjustSizeButton = new Button();
        adjustSizeButton.setGraphic(new ImageView("Size24.gif"));
        adjustSizeButton.setTooltip(new Tooltip("Größe ändern"));

        placeDinoButton = new ToggleButton();
        ImageView imageDino = new ImageView("Trex.png");
        imageDino.setFitWidth(24);
        imageDino.setFitHeight(24);
        placeDinoButton.setGraphic(imageDino);
        placeDinoButton.setTooltip(new Tooltip("Dino platzieren"));

        placeBoneButton = new ToggleButton();
        ImageView imageBone = new ImageView("Bone.png");
        imageBone.setFitHeight(24);
        imageBone.setFitWidth(24);
        placeBoneButton.setGraphic(imageBone);
        placeBoneButton.setTooltip(new Tooltip("Knochen platzieren"));

        placeRockButton = new ToggleButton();
        ImageView imageRock = new ImageView("Rock.png");
        imageRock.setFitWidth(24);
        imageRock.setFitHeight(24);
        placeRockButton.setGraphic(imageRock);
        placeRockButton.setTooltip(new Tooltip("Felsen platzieren"));

        deleteButton = new ToggleButton();
        deleteButton.setGraphic(new ImageView("Delete24.gif"));
        deleteButton.setTooltip(new Tooltip("Löschen"));

        Separator vSeparator3 = new Separator(javafx.geometry.Orientation.VERTICAL);

        adjustAmountOfBonesButton = new Button();
        ImageView imageTrexWithBone = new ImageView("TrexWithBone.png");
        imageTrexWithBone.setFitHeight(24);
        imageTrexWithBone.setFitWidth(24);
        adjustAmountOfBonesButton.setGraphic(imageTrexWithBone);
        adjustAmountOfBonesButton.setTooltip(new Tooltip("Knochenmenge anpassen"));

        turnLeftButton = new Button();
        ImageView imageTrexLeftTurn = new ImageView("TrexLeftTurn.png");
        imageTrexLeftTurn.setFitHeight(24);
        imageTrexLeftTurn.setFitWidth(24);
        turnLeftButton.setGraphic(imageTrexLeftTurn);
        turnLeftButton.setTooltip(new Tooltip("nach links drehen"));

        moveForwardButton = new Button();
        ImageView imageTrexForward = new ImageView("TrexForward.png");
        imageTrexForward.setFitHeight(24);
        imageTrexForward.setFitWidth(24);
        moveForwardButton.setGraphic(imageTrexForward);
        moveForwardButton.setTooltip(new Tooltip("vorwärts"));

        pickUpBoneButton = new Button();
        ImageView imageTrexPickUpBone = new ImageView("TrexPickUpBone.png");
        imageTrexPickUpBone.setFitHeight(24);
        imageTrexPickUpBone.setFitWidth(24);
        pickUpBoneButton.setGraphic(imageTrexPickUpBone);
        pickUpBoneButton.setTooltip(new Tooltip("Knochen aufheben"));

        putDownBoneButton = new Button();
        ImageView imageTrexLayDownBone = new ImageView("TrexLayDownBone.png");
        imageTrexLayDownBone.setFitHeight(24);
        imageTrexLayDownBone.setFitWidth(24);
        putDownBoneButton.setGraphic(imageTrexLayDownBone);
        putDownBoneButton.setTooltip(new Tooltip("Knochen ablegen"));

        Separator vSeparator4 = new Separator(javafx.geometry.Orientation.VERTICAL);

        startContinueButton = new Button();
        startContinueButton.setGraphic(new ImageView("Start24.gif"));
        startContinueButton.setTooltip(new Tooltip("Start/Fortsetzen"));

        pauseButton = new Button();
        pauseButton.setGraphic(new ImageView("Pause24.gif"));
        pauseButton.setTooltip(new Tooltip("Pause"));

        stopButton = new Button();
        stopButton.setGraphic(new ImageView("Stop24.gif"));
        stopButton.setTooltip(new Tooltip("Stopp"));

        Separator vSeparator5 = new Separator(Orientation.VERTICAL);

        slider = new Slider();
        slider.setTooltip(new Tooltip("Geschwindigkeit regulieren"));

        // adding items to toolbar
        toolBar.getItems().addAll(newButton, loadButton, vSeparator1, saveButton, compileButton, vSeparator2,
                adjustSizeButton, placeDinoButton, placeBoneButton, placeRockButton, deleteButton, vSeparator3,
                adjustAmountOfBonesButton, turnLeftButton, moveForwardButton, pickUpBoneButton, putDownBoneButton, vSeparator4,
                startContinueButton, pauseButton, stopButton, vSeparator5, slider);

        // GUI main working space
        textArea = new TextArea();
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(textArea, scrollPane);

        // assembling all parts of the GUI
        VBox root = new VBox();
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        root.getChildren().addAll(menuBar, toolBar, splitPane, notifier);

        setTitle("Dino Simulator");
        setScene(new Scene(root, 800, 500));
    }

    // The unused getters will be used later to implement the action

    public MenuItem getNewMenuItem() {
        return newMenuItem;
    }

    public MenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    public MenuItem getCompileMenuItem() {
        return compileMenuItem;
    }

    public MenuItem getPrintEditorMenuItem() {
        return printEditorMenuItem;
    }

    public MenuItem getStopMenuItem() {
        return stopMenuItem;
    }

    public MenuItem getSaveXMLMenuItem() {
        return saveXMLMenuItem;
    }

    public MenuItem getSaveJAXBMenuItem() {
        return saveJAXBMenuItem;
    }

    public MenuItem getSerializeMenuItem() {
        return serializeMenuItem;
    }

    public MenuItem getLoadXMLMenuItem() {
        return loadXMLMenuItem;
    }

    public MenuItem getLoadJAXBMenuItem() {
        return loadJAXBMenuItem;
    }

    public MenuItem getDeserializeMenuItem() {
        return deserializeMenuItem;
    }

    public MenuItem getPrintTerritoriumMenuItem() {
        return printTerritoriumMenuItem;
    }

    public MenuItem getChangeSizeMenuItem() {
        return changeSizeMenuItem;
    }

    public CheckMenuItem getPlaceDinoCheckMenuItem() {
        return placeDinoCheckMenuItem;
    }

    public CheckMenuItem getPlaceBoneCheckMenuItem() {
        return placeBoneCheckMenuItem;
    }

    public CheckMenuItem getPlaceRockCheckMenuItem() {
        return placeRockCheckMenuItem;
    }

    public CheckMenuItem getDeleteCheckMenuItem() {
        return deleteCheckMenuItem;
    }

    public MenuItem getAdjustAmountOfBonesMenuItem() {
        return adjustAmountOfBonesMenuItem;
    }

    public MenuItem getTurnLeftMenuItem() {
        return turnLeftMenuItem;
    }

    public MenuItem getForwardMenuItem() {
        return forwardMenuItem;
    }

    public MenuItem getPickUpBoneMenuItem() {
        return pickUpBoneMenuItem;
    }

    public MenuItem getPutDownBoneMenuItem() {
        return putDownBoneMenuItem;
    }

    public MenuItem getStartContinueMenuItem() {
        return startContinueMenuItem;
    }

    public MenuItem getPauseMenuItem() {
        return pauseMenuItem;
    }

    public MenuItem getStopDinoMenuItem() {
        return stopDinoMenuItem;
    }

    public Button getNewButton() {
        return newButton;
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCompileButton() {
        return compileButton;
    }

    public Button getAdjustSizeButton() {
        return adjustSizeButton;
    }

    public ToggleButton getPlaceDinoButton() {
        return placeDinoButton;
    }

    public ToggleButton getPlaceBoneButton() {
        return placeBoneButton;
    }

    public ToggleButton getPlaceRockButton() {
        return placeRockButton;
    }

    public ToggleButton getDeleteButton() {
        return deleteButton;
    }

    public Button getAdjustAmountOfBonesButton() {
        return adjustAmountOfBonesButton;
    }

    public Button getTurnLeftButton() {
        return turnLeftButton;
    }

    public Button getMoveForwardButton() {
        return moveForwardButton;
    }

    public Button getPickUpBoneButton() {
        return pickUpBoneButton;
    }

    public Button getPutDownBoneButton() {
        return putDownBoneButton;
    }

    public Button getStartContinueButton() {
        return startContinueButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Slider getSlider() {
        return slider;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Notifier getNotifier(){
        return notifier;
    }
}
