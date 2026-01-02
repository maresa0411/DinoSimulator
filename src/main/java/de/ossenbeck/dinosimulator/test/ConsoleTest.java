package de.ossenbeck.dinosimulator.test;

import de.ossenbeck.dinosimulator.model.Orientation;
import de.ossenbeck.dinosimulator.model.Territory;

import java.util.Scanner;

public class ConsoleTest {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        boolean gameOn = true;
        Territory territory = null;
        System.out.println("0: eigenes Spiel erstellen, 1: Zufallslevel spielen");
        int choice = scanner.nextInt();
        if(choice == 0) {
            System.out.print("\nAnzahl der Reihen (> 0 und <= 100): ");
            int rows = scanner.nextInt();
            System.out.print("\nAnzahl der Spalten (> 0 und <= 100): ");
            int cols = scanner.nextInt();
            territory = new Territory(rows, cols);
            printTerritory(territory);
        }else if(choice == 1){
            territory = new Territory();
            printTerritory(territory);
        }else{
            gameOn = false;
            System.out.println("Ungültige Eingabe");
        }



        while(gameOn) {
            System.out.println("Aktion ausführen:" +
                    "\nv (nach vorn), l (links um), a (Knochen aufheben), b (Knochen ablegen)" +
                    "\np (Dino platzieren), k (Knochen platzieren), f (Felsen platzieren), d (löschen) \nq (beenden)" +
                    "\ng (Größe ändern)");

            String input = scanner.next();
            if(input.equals("g")){
                System.out.println("Neue Reihenanzahl: (> 0)");
                int newRows = scanner.nextInt();
                System.out.println("Neue Spaltenanzahl: (> 0)");
                int newCols = scanner.nextInt();
                territory.resize(newRows, newCols);
            } else if (input.equals("p") || input.equals("k") || input.equals("f") || input.equals("d")) {
                System.out.println("Reihe: (>= 0 und < " + territory.getNumberOfRows() + ")");
                int row = scanner.nextInt();
                System.out.println("Spalte: (>= 0 und < " + territory.getNumberOfCols()  +")");
                int col = scanner.nextInt();

                switch (input) {
                    case "p" -> territory.placeDino(row, col);
                    case "k" -> territory.placeBone(row, col);
                    case "f" -> territory.placeRock(row, col);
                    case "d" -> territory.removeItem(row, col);
                }
            } else {
                switch (input) {
                    case "v" -> territory.getDino().moveForward();
                    case "l" -> territory.getDino().turnLeft();
                    case "a" -> territory.getDino().pickUpBone();
                    case "b" -> territory.getDino().putDownBone();
                    case "q" -> gameOn = false;
                    default -> System.out.println("Ungueltige Eingabe");
                }
            }
            printTerritory(territory);
        }
    }

    private static void printTerritory(Territory territory){
        int dinoRow = territory.getDino().getRow();
        int dinoCol = territory.getDino().getCol();
        Orientation dinoOrientation = territory.getDino().getOrientation();

        for(int i=0; i< territory.getNumberOfRows(); i++){
            for(int j=0; j< territory.getNumberOfCols(); j++){
                String print = "";
                if(dinoRow == i && dinoCol == j){
                    switch (dinoOrientation){
                        case EAST -> print=">";
                        case NORTH -> print="^";
                        case WEST -> print="<";
                        case SOUTH -> print="v";
                    }
                }else {
                    if (territory.isEmpty(i,j)) {print = "*";}
                    else if (territory.isRock(i,j)) {print = "!";}
                    else {print = String.valueOf(territory.getBones(i,j));}

                }
                System.out.print(print);
            }
            System.out.println();
        }
        System.out.println();
    }

}


