package de.ossenbeck.dinosimulator.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesController {

    private static final String PROPFILE = "dinosimulator.properties";

    private static PropertiesController propertiesController = null;

    public static PropertiesController getPropertiesController() {
        if (PropertiesController.propertiesController == null) {
            PropertiesController.propertiesController = new PropertiesController();
        }
        return PropertiesController.propertiesController;
    }

    private static final String PROP_LANGUAGE = "language";
    private static final String DEF_LANGUAGE = null;

    private Properties prop = new Properties();

    private PropertiesController() {
        this.prop = new Properties();
        try (FileInputStream propfile = new FileInputStream(PropertiesController.PROPFILE)) {
            this.prop.load(propfile);
        } catch (IOException _) {
            // nothing
        }
    }

    public String getLanguage() {
        String lang = this.prop.getProperty(PropertiesController.PROP_LANGUAGE);
        return lang == null ? DEF_LANGUAGE : lang;
    }

}
