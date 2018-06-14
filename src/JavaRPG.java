import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JavaRPG implements NativeKeyListener {

    private static final String[][] map = new String[8][8];
    private static int px = 0;
    private static int py = 0;
    private static int mapId = 0;
    private static int xp = 0;
    private static int lvl = 1;
    private static int lvlua = 100;
    private static int pmh = 10;
    private static int pch = 10;
    private static final String[][][] fmap = new String[8][8][50];
    private static int damage = 1;
    private static int gold = 99;
    private static int defense = 1;
    private static ArrayList<String> inv = new ArrayList<>();
    private static final char playerName = 'P';
    private static final ArrayList<String> sitems = new ArrayList<>();
    private static final ArrayList<Integer> sitemsp = new ArrayList<>();
    private static boolean defeatedAll = false;
    private static char input = 'n';
    private static volatile boolean justPressed = true;

    public static void main(String[] args) {
        /*Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new JavaRPG());
        start();*/
    }
/*
  public static void moveup() {
        if (py >= 1) {
            py--;
        }
    }

    public static void movedown() {
        if (py <= 6) {
            py++;
        }
    }

    public static void moveleft(){
        if (px >= 1) {
            px--;
        }
    }

    public static void moveright() {
        if (px <= 6) {
            px++;
        }
    }*/

    private static void move(String ps) {
        defeatedAll = monstersLeft() == 0;
        for (int i = 0; i < fmap[mapId].length; i++) {
            System.arraycopy(fmap[mapId][i], 0, map[i], 0, fmap[mapId][i].length);
        }
        if ("M".equals(fmap[mapId][py][px]) && "M".equals(map[py][px])) {
            fight("Mage", 1 + lvl / 2, 1 + lvl / 2);
        } else if ("S".equals(fmap[mapId][py][px]) && "S".equals(map[py][px])) {
            shop();
        } else if ("D".equals(fmap[mapId][py][px]) && "D".equals(map[py][px])) {
            if (instancesOf("M") + instancesOf("K") + instancesOf("B") == 0) {
                mapId++;
                draw();
            } else {
                System.out.println("You have not defeated everything yet");
            }
        } else if ("K".equals(fmap[mapId][py][px]) && "K".equals(map[py][px])) {
            fight("Skeleton", 1 + lvl / 2, 1 + lvl / 2);
        } else if ("B".equals(fmap[mapId][py][px]) && "B".equals(map[py][px])) {
            fight("Barbarian", 1 + lvl / 2, 1 + lvl / 2);
        } else if ("E".equals(fmap[mapId][py][px]) && "E".equals(map[py][px])) {
            if (instancesOf("M") + instancesOf("K") + instancesOf("B") == 0) {
                mapId--;
                draw();
            } else {
                System.out.println("You have not defeated everything yet");
            }
        }
        map[py][px] = ps;
    }

    private static void setup() throws IOException, InterruptedException {


        sitems.add("Major Potion");
        sitems.add("Minor Potion");
        sitemsp.add(10);
        sitemsp.add(5);
        fmap[0] = new String[][]{
                {"@", "|", "M", "B", "K", "M", "|", "S"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "@", "@", "@", "@", "@", "@", "@"},
                {"@", "|", "@", "D", "@", "@", "|", "@"}

        };
        fmap[1] = new String[][]{
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"M", "|", "@", "@", "@", "@", "|", "E"},
                {"@", "|", "@", "@", "@", "@", "|", "M"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "@", "@", "@", "@", "@", "@", "@"},
                {"M", "|", "@", "D", "@", "@", "|", "@"}
        };
        fmap[2] = new String[][]{
                {"E", "|", "@", "@", "@", "@", "|", "@"},
                {"M", "|", "@", "@", "@", "@", "|", "@"},
                {"@", "|", "@", "@", "@", "@", "M", "@"},
                {"@", "|", "@", "@", "@", "@", "M", "D"},
                {"@", "|", "@", "@", "@", "@", "M", "@"},
                {"@", "|", "@", "@", "@", "@", "M", "@"},
                {"@", "@", "@", "@", "@", "@", "|", "@"},
                {"M", "|", "@", "@", "@", "@", "|", "@"}
        };
        fmap[3] = new String[][]{
                {"@", "@", "@", "@", "@", "@", "|", "@"},
                {"@", "@", "@", "@", "@", "@", "|", "@"},
                {"@", "@", "@", "@", "@", "@", "M", "@"},
                {"@", "E", "@", "@", "@", "@", "M", "@"},
                {"@", "@", "@", "@", "@", "@", "M", "@"},
                {"@", "@", "@", "@", "@", "@", "M", "@"},
                {"@", "@", "@", "@", "@", "@", "|", "@"},
                {"@", "@", "@", "@", "@", "@", "|", "@"}
        };
        String everything = "";
        if (new File("test.csv").exists() && new File("test.csv").length() >= 10) {
            try (BufferedReader br = new BufferedReader(new FileReader("test.csv"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();
            } catch (IOException ex) {
                Logger.getLogger(JavaRPG.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] data = everything.split(",");
            px = Integer.parseInt(data[0]);
            py = Integer.parseInt(data[1]);
            mapId = Integer.parseInt(data[2]);
            pch = Integer.parseInt(data[3]);
            pmh = Integer.parseInt(data[4]);
            gold = Integer.parseInt(data[5]);
            xp = Integer.parseInt(data[6]);
            lvl = Integer.parseInt(data[7]);
            defeatedAll = Boolean.parseBoolean(data[8]);
            System.out.println(defeatedAll);
            inv = new ArrayList<>();
            inv.addAll(Arrays.asList(data[data.length - 1].split(System.lineSeparator())));
        } else {

            Writer writerfns;
            try {
                writerfns = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("test.csv"), "utf-8"));
                {
                    writerfns.write(px + "," + py + "," + mapId + "," + pch + "," + pmh + "," + gold + "," + xp + "," + lvl + ",");
                    for (String item : inv) {
                        writerfns.write(item + System.lineSeparator());
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(JavaRPG.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        move(Character.toString(playerName));
        cL();

        draw();

        while (true) {

            while (justPressed) {
            }
            justPressed = true;
            //cL();
            switch (input) {
                case 'a':
                    if (px - 1 >= 0 && !("|".equals(map[py][px - 1]))) {
                        px--;
                    } else {
                        System.out.println("You are blocked by a wall");
                    }
                    cL();
                    break;
                case 'd':
                    if (px < map.length - 1 && !("|".equals(map[py][px + 1]))) {
                        px++;
                    } else {
                        System.out.println("You are blocked by a wall");
                    }
                    cL();
                    break;
                case 'w':
                    if (py - 1 >= 0 && !("|".equals(map[py - 1][px]))) {
                        py--;
                    } else {
                        System.out.println("You are blocked by a wall");
                    }
                    cL();
                    break;
                case 's':
                    if (py + 1 < 8 && !("|".equals(map[py + 1][px]))) {
                        py++;
                    } else {
                        System.out.println("You are blocked by a wall");
                    }
                    cL();
                    break;
                case 'e':
                    return;
                case 'v':
                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream("test.csv"), "utf-8"))) {
                        //System.out.println(px + "," + py + "," + mapId + "," + pch + "," + pmh + "," + gold + "," + xp + "," + lvl + "," + defeatedAll + ",");
                        writer.write(px + "," + py + "," + mapId + "," + pch + "," + pmh + "," + gold + "," + xp + "," + lvl + "," + defeatedAll + ",");
                        for (String item : inv) {
                            writer.write(item + System.lineSeparator());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(JavaRPG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Saved.");
                    break;
                default:
                    System.out.println();
            }
            input = 'n';
            switch (px) {
                case 8:
                    px = 0;
                    break;
                case -1:
                    px = 7;
                    break;
                default:
                    System.out.print("");
                    break;
            }
            switch (py) {
                case 8:
                    py = 0;
                    break;
                case -1:
                    py = 7;
                    break;
                default:
                    System.out.print("");
                    break;
            }
            try {
                int invn = Integer.parseInt(Character.toString(input));
                if (invn < inv.size()) {
                    System.out.println("You used the " + inv.get(invn) + "!");
                    if (inv.get(invn).contains("Major")) {
                        pch += 10;
                    } else if (inv.get(invn).contains("Minor")) {
                        pch += 5;
                    }
                    inv.remove(invn);
                    if (pch >= pmh) {
                        pch = pmh;
                    }

                }
            } catch (NumberFormatException ignored) {
            }
            move("P");
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            draw();
        }

    }

    private static void draw() {
        hpBar(pmh, pch);
        inv.forEach((item) -> System.out.print(item + ", "));
        System.out.println();
        System.out.println(lvlua - xp + " xp left");
        System.out.println("Level " + lvl);
        System.out.println(monstersLeft());
        System.out.println("$" + gold);
        for (String[] as : map) {
            for (String s : as) {
                System.out.print(s + " ");
            }
            System.out.println();
        }

    }

    private static void cL() {
        if (xp >= lvlua) {
            xp -= lvlua;
            lvlua = lvl * 500;
            lvl++;
        }
        defense = lvl * 3 / 2;
        damage = lvl * 3 / 2;
    }

    private static void hpBar(int mh, int ch) {
        System.out.print("[" + repeat("=", ch) + repeat(" ", mh - ch) + "]" + " " + ch + "/" + mh + "\n");
    }

    private static String repeat(String s, int n) {
        StringBuilder rv = new StringBuilder();
        for (int i = 0; i < n; i++) {
            rv.append(s);
        }
        return rv.toString();
    }

    public void start() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new JavaRPG());
        try {
            setup();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void shop() {
        while (justPressed) {
        }
        justPressed = true;
        for (int i = 0; i < sitems.size(); i++) {
            System.out.println(sitems.get(i) + " Price: " + sitemsp.get(i));
        }
        System.out.println(gold);
        System.out.println("\b");
        while (true) {

            if (input == 'l') {
                if (py > 0 && "@".equals(map[py - 1][px])) {
                    py--;
                    move(Character.toString(playerName));
                    draw();
                    return;
                } else if (py < 7 && "@".equals(map[py + 1][px])) {
                    py++;
                    move(Character.toString(playerName));
                    draw();
                    return;
                } else if (px > 0 && "@".equals(map[py][px - 1])) {
                    px--;
                    move(Character.toString(playerName));
                    draw();
                    return;
                } else if (py < 7 && "@".equals(map[py][px + 1])) {
                    px++;
                    move(Character.toString(playerName));
                    draw();
                    return;
                }
                return;
            }
            try {
                int num = Integer.parseInt(Character.toString(input));
                if (num < sitems.size() && num >= 0) {
                    if (gold >= sitemsp.get(num)) {
                        inv.add(sitems.get(num));
                        gold -= sitemsp.get(num);
                    } else {
                        System.out.println("Not enough money");
                    }
                    cL();
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a number");
            }
            for (int i = 0; i < sitems.size(); i++) {
                System.out.println(sitems.get(i) + " Price: " + sitemsp.get(i));
            }
            System.out.println(gold);
            while (justPressed) {
            }
            justPressed = true;
        }
    }

    private static void fight(String monster, int x, int y) {

        boolean fighting = true;

        System.out.printf("You have found a %s %n", monster);
        int healthb = y;
        int healtha = pch;
        int maxhealtha = pmh;
        hpBar(x, healthb);
        System.out.printf("%n%n%n");
        hpBar(maxhealtha, healtha);
        while (fighting) {
            int r = new Random().nextInt((lvl / 3) + 1);
            System.out.println("Your action? (a to attack)");
            while (justPressed) {
            }
            justPressed = true;
            switch (input) {
                case 'a':
                    healthb -= damage * 3;
                    pch -= r - (defense * ((new Random().nextInt(25) + 50) / 100));
                    System.out.println(defense);
                    System.out.println(r - (defense * 0.75));
                    break;
                case 'r':
                    if (new Random().nextInt(10) < 8) {
                        System.out.println("You escaped!");
                        return;
                    } else {
                        System.out.println("You couldn't escape!");
                        pch -= r;
                    }
                    cL();
                    break;
                default:
                    System.out.println();
            }

            if (pch <= 0) {
                System.out.println("You lost!");
                Runtime.getRuntime().exit(0);
                return;
            } else if (healthb <= 0) {
                fighting = false;
                xp += x * 100;
                gold += x;
                System.out.println("You win!");
                System.out.println("You got " + x * 100 + " xp and " + x + " gold!");
                fmap[mapId][py][px] = "@";
                map[py][px] = "@";
                cL();
                Random rr = new Random();
                boolean b1 = rr.nextBoolean();
                boolean b2 = rr.nextBoolean();
                if (b1 && b2) {
                    inv.add("Major Potion");
                    System.out.println("You got a Major Potion!");
                } else if (b1 || b2) {
                    inv.add("Minor Potion");
                    System.out.println("You got a Minor Potion!");
                }
                //System.out.println(fmap[mapId][py][px] + " " + map[py][px]);
            } else {
                hpBar(x, healthb);
                System.out.printf("%n%n%n%n");
                hpBar(pmh, pch);
            }
            //health bars and space between

        }
    }

    private static int instancesOf(Object c) {
        int amount = 0;
        for (Object[] tt : JavaRPG.map) {
            for (Object o : tt) {

                if (o != null && o.equals(c)) {
                    amount++;
                }
            }
        }
        return amount;
    }

    private static int monstersLeft() {
        return instancesOf("M") + instancesOf("B") + instancesOf("K");
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        //no
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        input = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase().charAt(0);
        justPressed = false;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        //no
    }
}