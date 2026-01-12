package de.ossenbeck.dinosimulator.controller;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ResourcesController {

    public static  String PATH = "/";
    public static  String JAR_PREFIX = "";

    private static final String PROP_CLASS = JAR_PREFIX + "de.ossenbeck.dinosimulator.i18n_resources.text";

    private static ResourcesController resourcesController = null;

    public static ResourcesController getResourcesController() {
        if (ResourcesController.resourcesController == null) {
            ResourcesController.resourcesController = new ResourcesController();
        }
        return ResourcesController.resourcesController;
    }

    private Locale locale;
    private ResourceBundle bundle;
    private ObservableResourceFactory resourceFactory;

    private ResourcesController() {
        try {
            String localeStr = PropertiesController.getPropertiesController().getLanguage();
            if (localeStr == null) {
                this.locale = Locale.getDefault();
            } else {
                this.locale = Locale.of(localeStr);
            }
        } catch (Exception _) {
            this.locale = Locale.getDefault();
        }
        Locale.setDefault(this.locale);
        this.bundle = ResourceBundle.getBundle(PROP_CLASS, this.locale);
        this.resourceFactory = new ObservableResourceFactory();
        this.resourceFactory.setResources(this.bundle);
    }

    public String getI18nValue(String key) {
        return this.bundle.getString(key);
    }

    public StringBinding i18n(String key) {
        return this.resourceFactory.getStringBinding(key);
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale loc) {
        this.locale = loc;
        Locale.setDefault(this.locale);
        this.bundle = ResourceBundle.getBundle(PROP_CLASS, this.locale);
        this.resourceFactory.setResources(this.bundle);
    }

}

class ObservableResourceFactory {

    private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public final ResourceBundle getResources() {
        return resourcesProperty().get();
    }

    public final void setResources(ResourceBundle resources) {
        resourcesProperty().set(resources);
    }

    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(resourcesProperty());
            }

            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }
}
