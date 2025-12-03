package de.ossenbeck.dinosimulator.view;

import de.ossenbeck.dinosimulator.model.Dino;
import de.ossenbeck.dinosimulator.model.DinoSimulatorGame;
import de.ossenbeck.dinosimulator.util.Invisible;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DinoContextMenu extends ContextMenu {

    public DinoContextMenu(final DinoSimulatorGame game){
        List<Method> methods = getMethods(game.getTerritory().getDino());
        for(Method method : methods){
            StringBuilder parameters = new StringBuilder();
            for(int i=0; i<method.getParameterCount(); i++){
                parameters.append(method.getParameterTypes()[i]);
                if(i < method.getParameterCount()-1){
                    parameters.append(",");
                }
            }
            String content = method.getReturnType() + " " + method.getName() + "(" + parameters + ")";
            MenuItem menuItem = new MenuItem(content);
            if(method.getParameterCount() > 0){
                menuItem.setDisable(true);
            }
            this.getItems().add(menuItem);
            menuItem.setOnAction(_ -> {
                try {
                    method.invoke(game.getTerritory().getDino());
                    game.getTerritory().onTerritoryChange();
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler", ButtonType.OK);
                    alert.showAndWait();
                }
            });

        }
    }

    private List<Method> getMethods(Dino dino){
        List<Method> methods = new ArrayList<>();
        for(Method method : dino.getClass().getDeclaredMethods()){
            if(!Modifier.isPrivate(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers()) && method.getAnnotation(Invisible.class) == null){
                methods.add(method);
                method.setAccessible(true);
            }
        }

        return methods;
    }
}
