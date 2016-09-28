package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 4/29/2016.
 * Property of boxedworks.
 */

public class Controller extends BasicEntity{

    GameConstants.Direction direction;

    boolean justPressed,
            justReleased;

    long timeHeld;

    int numTaps;

    Controller(){
        super("Controller");
        this.direction = null;
        this.justPressed = false;
        this.justReleased = false;
        this.timeHeld = 0;
        this.numTaps = 0;
    }

    static int inputHandlerID = 0;

    static Sound sButtonPress = new Sound(R.raw.tick1);

    @Override
    protected void update() {
        // If menu is showing, handle menu input, otherwise handle game input
        if (Overlay.hasMenu()) {
            this.handleTextMenu();
        }else{
            this.handleGameInput();
        }
    }

    //float y = (TouchManager.y / SuperManager.height);

    private void handleGameInput(){
        // Check if controller has input
        if(this.justPressed){
            this.justPressed = false;
            // Get column using screen position as percentage multiplied by number of columns
            float x = (TouchManager.x / SuperManager.width);
            int column = (int)(x * GameConstants.numberColumns);
            // Send column data to tapperManager
            GameConstants.tappers.handleColumnTap(column);
        }
    }

    private void handleTextMenu(){
        // Get selection off of rough y
        int selection = (int)((TouchManager.y / SuperManager.height - 0.45f) * 15.5f),
                numElements = Overlay.currentMenu.children.length - 1;
        // Make sure selection in range
        if(selection > numElements || selection < 0){
            selection = -1;
        }
        // Make sound if just pressed on selection
        if(this.justPressed && selection != -1){
            this.justPressed = false;
            // Play button press sound
            sButtonPress.play();
        }
        Overlay.currentMenu.selected = selection;
        // Send tap release to menu
        if(this.justReleased && selection != -1){
            this.justReleased = false;
            // Play button release sound
            sButtonPress.play();
            // Fix double tap
            this.justPressed = false;
            // Send null if justReleased, no cameraDirection
            Overlay.currentMenu.tapped();
        }
    }
}
