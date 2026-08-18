Der DinoSimulator ist ein Uni-Projekt aus dem WS 2026/2027. Inspiriert am HamsterSimulator (https://www.java-hamster-modell.de/simulator.html) bietet der Simulator die Möglichkeit, spielerisch Java zu lernen.
Das Spielfeld lässt sich gestalten, indem Felsen als Hindernis und Knochen zum Aufheben platziert werden können.
Der Dino kann nach vorne bewegt werden, links drehen, einen Knochen aufheben und ablegen und die Knochenmenge im Maul anpassen.
Im Textfeld links kann Java-Code zur Bewegung des Dinos mithilfe der unten beschriebenen Basisfunktionen geschrieben werden. Nach erfolgreicher Kompilierung kann dieser Code durch die Simulation ausgeführt werden.
Ein Territorium kann per Serialisierung gespeichert und geladen werden. Außerdem können Beispiele unter Tags in einer Datenbank abgespeichert und aus dieser geladen werden. Java-Code kann auch gespeichert und geladen werden.
Zudem kann die Sprache zwischen Deutsch und Englisch über das Menü oder vor Start in den properties angepasst werden (die englische Übersetzung ist aber nicht vollständig umgesetzt).

Der Dino im DinoSimulator kann mit folgenden Methoden gesteuert werden:
moveForward()       , um eine Kachel vorwärts zu gehen.
turnLeft()          , um sich nach links zu drehen.
pickUpBone()        , um, wenn vorhanden, einen Knochen aufzunehmen.
putDownBone()       , um, wenn das Maul nicht leer ist, einen Knochen abzulegen.
boneThere()         , um zu prüfen, ob auf der aktuellen Kachel ein Knochen liegt.
canMoveForward()    , um zu prüfen, ob der Dino sich vorwärts bewegen kann.
isMouthEmpty()      , um zu prüfen, ob der Dino mindestens einen Knochen im Maul hat.

Folgende Technologien wurden bei der Umsetzung des Projektes eingesetzt: JavaFX-Anwendung mit MVC-Struktur, OOP, Event-basierte Kommunikation, Compiler/Classloader, Reflection, parallele Programmierung (Threads), Serialisierung, JDBC, Java-Properties und -RessourceBundles

Der DinoSimulator kann über die unter Releases bereitgestellte .jar ausgeführt werden.

Bekannte Fehler und Informationen:
Die Übersetzung auf Englisch ist nicht vollständig umgesetzt.
Die Bilder vom Dino, Knochen und Felsen wurden mit ChatGPT generiert.
