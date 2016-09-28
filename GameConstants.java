package project.etrumper.thomas.ghostbutton;

import android.util.Log;

/**
 * Created by thoma on 6/20/2016.
 * Property of boxedworks.
 */
public class GameConstants {

    enum Direction{
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    static final String[] COLORS = new String[]{"Red", "Blue", "Green", "Black"};

    static float ambiance = 1f; // Amount of light (or RGB for more specific)

    static Camera camera;
    static float menuCameraZoom = 12f,
        defaultShininess = 1f;

    static int Score;
    static long timeLeftInMilli;

    static float[] sceneAmbience = new float[]{ambiance, ambiance, ambiance};

    static Controller controller;

    static long frameRate = 60;

    static int numberColumns; // Number of columns used for game
    static GO_TapperManager.ManagerType mode;

    static GO_Indicator indicator;
    static GO_TapperManager tappers;
    static GO_Timer timer;
    static GO_ScoreCounter scoreCounter;

    public static void init(){
        controller = new Controller();
        // Init overlay
        Overlay.init();
        // Load config
        loadGame();
        // Init stuff
        numberColumns = 3;
        mode = GO_TapperManager.ManagerType.CASUAL;
        indicator = new GO_Indicator();
        tappers = new GO_TapperManager(mode);
        timer = new GO_Timer();
        scoreCounter = new GO_ScoreCounter();
    }

    public static void update() {
        // Update controller
        controller.update();
        // Update camera
        camera.update();
        camera.updateCamera();
        // Update GOs
        if(!Overlay.hasMenu()) {
            indicator.update();
            tappers.update();
            timer.update();
            scoreCounter.update();
        }
    }

    public static void newGame(){
        Score = 0;
        indicator = new GO_Indicator();
        tappers = new GO_TapperManager(mode);
        timer = new GO_Timer();
        if(mode == GO_TapperManager.ManagerType.CASUAL) {
            timeLeftInMilli = (long) (5 * 1000); // 10 seconds
        }else if(mode == GO_TapperManager.ManagerType.RUSHED){
            timeLeftInMilli = (long) (10 * 1000); // 10 seconds
        }
        scoreCounter.updateText();
    }

    public static void loseGame(){
        Overlay.changeScreen(Overlay.CurrentScreen.LOSE);
    }

    public static void saveGame(){
        StringBuilder saveData = new StringBuilder("");
        // Add save data

        // Save to internal storage
        Loader.writeToFile(saveData.toString());
    }

    public static void loadGame(){
        // Retrieve string
        String temp = Loader.readFromFile();
        if(temp == null || temp.length() == 0 || temp.equals("")){
            LOGE("No config.txt file to load");
            return;
        }
        String[] datum = temp.split("&");
        for(String data : datum){
            String[] words = data.split(" ");
            switch (words[0]){
                default:
                    LOGE(String.format("Reading un-parsed line of code:\n%s", data));
                    break;
            }
        }
    }

    public static String getRandomColor(){
        int i = Math.abs(SuperManager.r.nextInt() % numberColumns);
        return COLORS[i];
    }

    private static void LOGE(String message){
        Log.e("GameConstants", message);
    }
}


/*

3 button = 3 ingredients
=SPAGHETTI
Cheese
Lasagna
Sauce
=ICE CREAM
Chocolate
Vanilla
Strawberry
=SANDWICH
Meats
Veggie
Cheese


 */
