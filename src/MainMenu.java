import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Press 's' to load the current save, or press o to load a different save");
        while (true) {
            String input = s.nextLine();
            switch (input) {
                case "s":
                    JavaRPG main = new JavaRPG();
                    main.start();
                    break;
                case "o":
                    System.out.println("Are you sure you want to load a different save? This will overwrite your current save. y/n");
                    input = s.nextLine();
                    switch(input){
                        case "y":
                            chooseSave();
                            break;
                        case "n":
                            System.out.println("Canceled");
                            break;
                    }
                    break;
                default:
                    System.out.println("Unknown command");
            }
        }

    }

    private static void chooseSave() {
        JFrame frame = new JFrame();
        FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fd.setDirectory("C:\\");
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename == null)
            System.out.println("Please choose a save file");
        else
            System.out.println("Loading save...");
    }
}
