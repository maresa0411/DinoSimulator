package de.ossenbeck.dinosimulator.controller;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.ossenbeck.dinosimulator.model.Orientation;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.view.DinoSimulatorStageView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

public class DBSerializationController {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DB_NAME = "exampleDB";
    private static final String DB_URL_PREFIX = "jdbc:derby:" + DB_NAME;
    private static final String DB_URL = DB_URL_PREFIX + ";create=false";
    private static final String DB_URL_CREATE = DB_URL_PREFIX + ";create=true";

    private static final String TABLENAME_EXAMPLE = "EXAMPLE";
    private static final String CREATE_EXAMPLE_TABLE_STATEMENT = "CREATE TABLE " + TABLENAME_EXAMPLE + " "
            + "(id INT NOT NULL PRIMARY KEY, name VARCHAR(255) NOT NULL, code VARCHAR(255) NOT NULL, territoryField CLOB NOT NULL, dinoRow INT NOT NULL, dinoCol INT NOT NULL, amountOfBones INT NOT NULL, orientation INT NOT NULL)";

    private static final String TABLENAME_TAG = "TAG";
    private static final String CREATE_TAG_TABLE_STATEMENT = "CREATE TABLE " + TABLENAME_TAG + " " + "(name VARCHAR(255) NOT NULL PRIMARY KEY)";

    private static final String TABLENAME_TAG_EXAMPLE = "TAG_EXAMPLE";
    private static final String CREATE_TAG_EXAMPLE_TABLE_STATEMENT = "CREATE TABLE " + TABLENAME_TAG_EXAMPLE + " "
            + "(tag_name VARCHAR(255) NOT NULL, example_id INT NOT NULL, PRIMARY KEY (tag_name, example_id), FOREIGN KEY (tag_name) REFERENCES TAG(name), FOREIGN KEY (example_id) REFERENCES EXAMPLE(id))";

    private static final String INSERT_EXAMPLE_STATEMENT = "INSERT INTO " + TABLENAME_EXAMPLE + "(id, name, code, territoryField, dinoRow, dinoCol, amountOfBones, orientation) values (?, ?, ?, ?, ?, ?, ?, ?) ";
    private static final String INSERT_TAG_STATEMENT = "INSERT INTO " + TABLENAME_TAG + " values (?) ";
    private static final String INSERT_TAG_EXAMPLE_STATEMENT = "INSERT INTO " + TABLENAME_TAG_EXAMPLE + " values (?, ?) ";

    private static final String SELECT_EXAMPLE_BY_TAG_STATEMENT = "SELECT id," + TABLENAME_EXAMPLE + ".name FROM " + TABLENAME_EXAMPLE
            + " JOIN " + TABLENAME_TAG_EXAMPLE + " ON " + TABLENAME_EXAMPLE + ".id = example_id"
            + " JOIN " + TABLENAME_TAG + " ON " + TABLENAME_TAG + ".name = tag_name"
            + " WHERE " + TABLENAME_TAG + ".name = (?) ";

    private static final String SELECT_EXAMPLE_BY_ID = "SELECT code, territoryField, dinoRow, dinoCol, amountOfBones, orientation FROM " + TABLENAME_EXAMPLE + " WHERE id=(?)";

    private static final String SELECT_TAG_EXISTS_STATEMENT = "SELECT COUNT(*) AS count FROM " + TABLENAME_TAG + " WHERE name=(?)";

    private static Connection connection = null;

    private static int counter = 1;

    public DBSerializationController(final Territory territory, final DinoSimulatorStageView stage,
                                     final SimulationController simController) {
        if (init()) {
            stage.getSaveDBMenuItem().setOnAction(_ -> save(stage, territory));
            stage.getLoadDBMenuItem().setOnAction(_ -> load(stage, territory));
            stage.getLoadDBMenuItem().disableProperty().bind(simController.isSimulationRunning());
        } else {
            stage.getSaveDBMenuItem().setDisable(true);
            stage.getLoadDBMenuItem().setDisable(true);
        }
    }

    /**
     * Opens dialog to type in tags under which the current territory and code will be saved
     * @param stage the current DinoSimulatorStageView
     * @param territory the current territory
     */
    private static void save(DinoSimulatorStageView stage, Territory territory){
        TextInputDialog saveExampleDialog = new TextInputDialog();
        saveExampleDialog.setTitle("Beispiel speichern");
        saveExampleDialog.setHeaderText("Tags");
        saveExampleDialog.setContentText("Bitte Tags eingeben (durch Leerzeichen getrennt");

        Optional<String> result = saveExampleDialog.showAndWait();

        if(result.isPresent()){
            String[] tags = result.get().split(" ");
            saveExample(stage.getTitle(), stage.getTextArea().getText(), territory, tags);
        }
    }

    /**
     * Opens a dialog to type in a tag.
     * Offers a selection of examples saved under this tag.
     * Opens the selected example.
     * @param stage the current DinoSimulatorStageView
     * @param territory the current territory
     */
    private static void load(final DinoSimulatorStageView stage, final Territory territory){
        TextInputDialog tagInputDialog = new TextInputDialog();
        tagInputDialog.setTitle("Beispiel laden");
        tagInputDialog.setHeaderText("Tag");
        tagInputDialog.setContentText("Bitte einen Tag eingeben");

        // does not allow whitespace or hyphens
        Button okButton = (Button) tagInputDialog.getDialogPane().lookupButton(ButtonType.OK);
        BooleanBinding invalidInput = Bindings.createBooleanBinding(() -> tagInputDialog.getEditor().getText().contains(" ") || tagInputDialog.getEditor().getText().contains("-"), tagInputDialog.getEditor().textProperty());
        okButton.disableProperty().bind(invalidInput);

        Optional<String> resultTag = tagInputDialog.showAndWait();

        if(resultTag.isPresent()){
            List<String> examples = loadExamples(resultTag.get());
            if(examples.isEmpty()){
                Alert noExampleAvailable = new Alert(AlertType.ERROR, "Kein Beispiel verfügbar", ButtonType.OK);
                noExampleAvailable.showAndWait();
                return;
            }

            // generated with ChatGPT at 06.01.2026
            Dialog<String> selectExampleDialog = new Dialog<>();
            selectExampleDialog.setTitle("Beispiel auswählen");
            selectExampleDialog.setHeaderText("Beispiel");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            selectExampleDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().setAll(examples);
            comboBox.getSelectionModel().selectFirst();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            grid.add(new Label("Bitte ein Beispiel auswählen:"), 0, 0);
            grid.add(comboBox, 1, 0);

            selectExampleDialog.getDialogPane().setContent(grid);

            selectExampleDialog.setResultConverter(button -> {
                if (button == okButtonType) {
                    return comboBox.getValue();
                }
                return null;
            });

            Optional<String> resultExample = selectExampleDialog.showAndWait();
            resultExample.ifPresent(s -> loadExample(s, stage, territory));
        }
    }

    /**
     * Initializes the tables in the database if necessary
     * @return if initializing was successful
     */
    private static boolean init() {
        if (connection != null) {
            return true;
        }

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException _) {
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL_CREATE);
             ResultSet resultSet1 = conn.getMetaData().getTables(null, null, TABLENAME_EXAMPLE, null);
             ResultSet resultSet2 = conn.getMetaData().getTables(null, null, TABLENAME_TAG, null);
             ResultSet resultSet3 = conn.getMetaData().getTables(null, null, TABLENAME_TAG_EXAMPLE, null)){
            if (!resultSet1.next()) {
                createAndInitTable(conn, CREATE_EXAMPLE_TABLE_STATEMENT);
            }
            if (!resultSet2.next()) {
                createAndInitTable(conn, CREATE_TAG_TABLE_STATEMENT);
            }
            if (!resultSet3.next()) {
                createAndInitTable(conn, CREATE_TAG_EXAMPLE_TABLE_STATEMENT);
            }
        } catch (SQLException _) {
            return false;
        }
        return true;
    }

    private static void createAndInitTable(Connection conn, String table) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(table);
        }
    }

    private static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed() && connection.isValid(0)) {
                return connection;
            }

            if (connection != null) {
                connection.close();
                connection = null;
                return getConnection();
            }
            connection = DriverManager.getConnection(DB_URL);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return connection;
        } catch (SQLException _) {
            return null;
        }
    }

    public static void shutdown() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException _) {
                // nothing to do
            }
        }
        if (DRIVER.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException _) {
                // nothing to do
            }
        }
    }

    /**
     * Loads examples saved under the given tag from the database
     * @param tag to search for in the database
     * @return a list of names of examples saved under the given tag
     */
    private static List<String> loadExamples(final String tag) {
        Connection conn = getConnection();
        if (conn == null) {
            Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return new ArrayList<>();
        }
        List<String> examples = new ArrayList<>();
        try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_EXAMPLE_BY_TAG_STATEMENT)){
            selectStmt.setString(1, tag);
            ResultSet resultSet = selectStmt.executeQuery();
            while(resultSet.next()){
                examples.add(resultSet.getString("id") + "-" + resultSet.getString("name"));
            }
        } catch (Exception _) {
            Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler beim Lesen!", ButtonType.OK);
            alert.showAndWait();
        }
        return examples;
    }

    /**
     * Loads the selected example (territory and code) in the currently opened stage
     * @param name the example to be opened
     * @param stage the currently opened stage
     * @param territory the current territory
     */
    private static void loadExample(final String name, final DinoSimulatorStageView stage, final Territory territory){
        Connection conn = getConnection();
        if (conn == null) {
            Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_EXAMPLE_BY_ID)){
            selectStmt.setString(1, name.split("-")[0]);
            ResultSet resultSet = selectStmt.executeQuery();
            if(resultSet.next()){
                stage.getTextArea().setText(resultSet.getString("code"));
                territory.setTerritoryField(xmlToTerritoryField(resultSet.getString("territoryField")));
                territory.getDino().setAmountOfBones(resultSet.getInt("amountOfBones"));
                territory.getDino().setOrientation(intToOrientation(resultSet.getInt("orientation")));
                territory.getDino().setPosition(resultSet.getInt("dinoRow"), resultSet.getInt("dinoCol"));
            }else{
                Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler beim Lesen!", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception _) {
            Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler beim Lesen!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Saves the given example (code, territory and name) under the tags in the database
     * @param name the name of the example
     * @param code the code of the example
     * @param territory the territory of the example
     * @param tags the tags the example will be saved under
     */
     private static void saveExample(final String name, final String code, final Territory territory, final String[] tags) {
        Connection conn = getConnection();
        if (conn == null) {
            Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try (PreparedStatement insertExampleStmt = conn.prepareStatement(INSERT_EXAMPLE_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {

            for(String tag: tags){
                try(PreparedStatement selectTagStmt = conn.prepareStatement(SELECT_TAG_EXISTS_STATEMENT)) {
                    selectTagStmt.setString(1, tag);
                    ResultSet countTag = selectTagStmt.executeQuery();
                    countTag.next();
                    if (countTag.getInt("count") == 0) {
                        try (PreparedStatement insertTagStmt = conn.prepareStatement(INSERT_TAG_STATEMENT)) {
                            insertTagStmt.setString(1, tag);
                            insertTagStmt.execute();
                        }
                    }
                }
            }
            synchronized (territory) {
                conn.setAutoCommit(false);
                insertExampleStmt.setInt(1, counter);
                insertExampleStmt.setString(2, name);
                insertExampleStmt.setString(3, code);

                String xml = territoryFieldToXml(territory.getTerritoryField());
                insertExampleStmt.setCharacterStream(4, new StringReader(xml), xml.length());
                insertExampleStmt.setInt(5, territory.getDino().getRow());
                insertExampleStmt.setInt(6, territory.getDino().getCol());
                insertExampleStmt.setInt(7, territory.getDino().getAmountOfBones());
                insertExampleStmt.setInt(8, orientationToInt(territory.getDino().getOrientation()));

                insertExampleStmt.execute();
                for(String tag: tags){
                    try(PreparedStatement insertExampleTagStmt = conn.prepareStatement(INSERT_TAG_EXAMPLE_STATEMENT)) {
                        insertExampleTagStmt.setString(1, tag);
                        insertExampleTagStmt.setInt(2, counter);
                        insertExampleTagStmt.execute();
                    }
                }
                counter++;
                conn.commit();
            }
        } catch (SQLException _) {
            try {
                Alert alert = new Alert(AlertType.ERROR, "Sorry, Datenbankfehler beim Speichern!", ButtonType.OK);
                alert.showAndWait();
                conn.rollback();
            } catch (SQLException _) {
                // nothing to do
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException _) {
                // nothing to do
            }
        }
    }

    // generated with ChatGPT at 07.01.2026

    /**
     * Converts a territory field (two-dimensional int array) into a xml string
     * @param territoryField the two-dimensional int array displaying a territory field
     * @return the converted territory field as xml string
     */
    private static String territoryFieldToXml(int[][] territoryField) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XMLEncoder encoder = new XMLEncoder(baos)) {
            encoder.writeObject(territoryField);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    /**
     * Converts a xml string into a territory field
     * @param xml the xml string
     * @return a two-dimensional int array displaying a territory
     */
    private static int[][] xmlToTerritoryField(String xml) {
        try (XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)))) {
            return (int[][]) decoder.readObject();
        }
    }

    /**
     * Converts an orientation into an int from 0 to 3
     * @param orientation the orientatiom
     * @return an int from 0 to 3, 0 is the default
     */
    private static int orientationToInt(Orientation orientation){
        switch(orientation){
            case SOUTH -> {return 1;}
            case WEST -> {return 2;}
            case NORTH -> {return 3;}
            case null, default -> {return 0;}
        }
    }

    /**
     * Converts an int from 0 to 3 into an orientation
     * @param o an int from 0 to 3
     * @return the orientation, default is east
     */
    private static Orientation intToOrientation(int o){
        switch (o){
            case 1 -> {return Orientation.SOUTH;}
            case 2 -> {return Orientation.WEST;}
            case 3 -> {return Orientation.NORTH;}
            default -> {return Orientation.EAST;}
        }
    }


}
