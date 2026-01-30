package de.ossenbeck.dinosimulator.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
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
    private static final String DEF_LANGUAGE = Locale.getDefault().getLanguage();

    private Properties prop;

    private PropertiesController() {
        this.prop = new Properties();
        try (FileInputStream propfile = new FileInputStream(PropertiesController.PROPFILE)) {
            this.prop.load(propfile);
        } catch (IOException _) {
            // nothing
        }
    }

    public String getLanguage() {
        final String lang = prop.getProperty(PROP_LANGUAGE);
        return lang == null || !isValidLocale(lang) ? DEF_LANGUAGE : lang;
    }

    public static boolean isValidLocale(String input) {
        Locale locale = Locale.forLanguageTag(input);
        for (Locale available : Locale.getAvailableLocales()) {
            if (available.getLanguage().equals(locale.getLanguage())) {
                return true;
            }
        }
        return false;
    }
}
