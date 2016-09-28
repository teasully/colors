package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 6/9/2016.
 */
public class Overlay {

    enum CurrentScreen {
        TITLE,
        OPTIONS,
        OVERLAY_ONLY,
        LOSE,
        GAMEMODE,
        DIFFICULTY
    }

    private static CurrentScreen currentScreen = null,
            lastScreen = null;

    // Main menu is shown on app creation
    private static TextMenu mainMenu = new TextMenu("colors", "play", "extras") {
        // start the game
        @Override
        protected void tapped0() {
            changeScreen(CurrentScreen.GAMEMODE);
        }

        // switch to extras menu
        @Override
        protected void tapped1() {
            changeScreen(CurrentScreen.OPTIONS);
        }
    },
    // Options menu handles all the customizable options
    optionsMenu = new TextMenu("extras", "back") {

        // Go back to last screen
        @Override
        protected void tapped0() {
            // Save options
            GameConstants.saveGame();
            // Return to last screen
            changeScreen(lastScreen);
        }
    },
    // Menu shown when lost
    loseMenu = new TextMenu("lose", "restart", "title") {
        // Restart
        @Override
        protected void tapped0() {
            changeScreen(CurrentScreen.OVERLAY_ONLY);
            GameConstants.newGame();
        }

        // Back to main menu
        @Override
        protected void tapped1() {
            changeScreen(CurrentScreen.TITLE);
        }

    },
    // Menu for game-modes
    gamemodeMenu = new TextMenu("mode", "classic", "rushed", "back"){
        @Override
        protected void tapped0(){
            // Change game mode
            GameConstants.mode = GO_TapperManager.ManagerType.CASUAL;
            // Go to difficulty menu
            //changeScreen(CurrentScreen.DIFFICULTY);
            changeScreen(CurrentScreen.OVERLAY_ONLY);
            GameConstants.newGame();
        }

        @Override
        protected void tapped1(){
            // Change game mode
            GameConstants.mode = GO_TapperManager.ManagerType.RUSHED;
            // Go to difficulty menu
            //changeScreen(CurrentScreen.DIFFICULTY);
            changeScreen(CurrentScreen.OVERLAY_ONLY);
            GameConstants.newGame();
        }
        @Override
        protected void tapped2(){
            changeScreen(CurrentScreen.TITLE);
        }

    },
    // Menu for choosing columns (difficulty)
    difficultyMenu = new TextMenu("difficulty", "2", "3", "4", "back"){
        @Override
        protected void init(){
            this.prompt.changeScale(0.1f);
        }
        @Override
        protected void tapped0(){
            GameConstants.numberColumns = 2;
            changeScreen(CurrentScreen.OVERLAY_ONLY);
            GameConstants.newGame();
        }
        @Override
        protected void tapped1(){
            GameConstants.numberColumns = 3;
            changeScreen(CurrentScreen.OVERLAY_ONLY);
            GameConstants.newGame();
        }
        @Override
        protected void tapped2(){
            GameConstants.numberColumns = 4;
            changeScreen(CurrentScreen.OVERLAY_ONLY);
            GameConstants.newGame();
        }
        @Override
        protected void tapped3(){
            changeScreen(CurrentScreen.GAMEMODE);
        }
    };

    static TextMenu currentMenu = mainMenu;

    private static TextMenu[] menus = new TextMenu[]{
            mainMenu, optionsMenu, loseMenu, gamemodeMenu, difficultyMenu
    };

    private static BasicEntity background;

    public static void draw() {
        // Check to see if menus on top of game
        if (currentScreen != CurrentScreen.OVERLAY_ONLY) {
            // Draw menu
            background.draw();
            currentMenu.draw();
        }
    }

    public static void update() {
        // Initiate title screen on boot
        if (currentScreen == null) {
            changeScreen(CurrentScreen.TITLE);
        }
        if (currentScreen != CurrentScreen.OVERLAY_ONLY) {
            currentMenu.update();
            background.update();
        }
    }

    public static void changeScreen(CurrentScreen newScreen){
        // Save the scene being changed
        if(lastScreen != currentScreen) {
            lastScreen = currentScreen;
        }
        // Change the scene
        currentScreen = newScreen;
        // Change the current TextMenu being updated and drawn based on passed parameter
        if (currentScreen == CurrentScreen.TITLE) {
            currentMenu = mainMenu;
        } else if (currentScreen == CurrentScreen.OPTIONS) {
            currentMenu = optionsMenu;
        }else if (currentScreen == CurrentScreen.LOSE) {
            currentMenu = loseMenu;
        } else if (currentScreen == CurrentScreen.GAMEMODE) {
            currentMenu = gamemodeMenu;
        } else if (currentScreen == CurrentScreen.DIFFICULTY) {
            currentMenu = difficultyMenu;
        }
        // Update current menu to prevent graphical glitch
        currentMenu.update();
        // Set current selection to -1
        currentMenu.selected = -1;
    }

    public static void init() {
        // Set menus' positions
        for (TextMenu menu : menus) {
            menu.position = new float[]{0f, 0f, 0f + GameConstants.menuCameraZoom};
        }
        background = new BasicEntity("Scenery1") {
            @Override
            protected void draw() {
                Vector3f color = MaterialManager.getVector3fColor("Brown");
                MaterialManager.changeMaterialColor("Wall", color);
                super.draw();
            }
        };
        background.setCurrentAnimation(background.addAnimation("wallstandard1"));
        // Move and scale
        background.position = new float[]{0f, 0f, 13f};
        background.scale = new float[]{15f, 0f, 25f};
    }

    public static boolean hasMenu(){
        return currentScreen != CurrentScreen.OVERLAY_ONLY;
    }

    public static int getRemainder(int value, int size) {
        // Get modulo
        int result = value % size;
        // If negative, subtract frm size to get correct return value
        if (result < 0) {
            result = size + result;
        }
        return result;
    }

}
