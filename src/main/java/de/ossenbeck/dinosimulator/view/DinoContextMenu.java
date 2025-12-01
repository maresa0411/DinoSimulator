package de.ossenbeck.dinosimulator.view;

import de.ossenbeck.dinosimulator.model.DinoSimulatorGame;
import de.ossenbeck.dinosimulator.model.Territory;
import de.ossenbeck.dinosimulator.util.Invisible;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DinoContextMenu extends ContextMenu {

    public DinoContextMenu(final DinoSimulatorGame game){
        List<Method> methods = getMethods(game.getTerritory());
        for(Method method : methods){
            StringBuilder parameters = new StringBuilder();
            for(int i=0; i<method.getParameterCount(); i++){
                parameters.append(method.getParameterTypes()[i].toString());
                if(i < method.getParameterCount()-1){
                    parameters.append(",");
                }
            }
            String content = method.getReturnType().toString() + " " + method.getName() + "(" + parameters + ")";
            MenuItem menuItem = new MenuItem(content);
            if(method.getParameterCount() > 0){
                menuItem.setDisable(true);
            }
            menuItem.setOnAction(e -> {
                //todo implement this
            });

        }
    }

    private List<Method> getMethods(Territory territory){
        List<Method> methods = new ArrayList<>();
        for(Method method : territory.getClass().getDeclaredMethods()){
            if(!Modifier.isPrivate(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && !Modifier.isAbstract(method.getModifiers()) && method.getAnnotation(Invisible.class) == null){
                methods.add(method);
            }
        }
        return methods;
    }
}
