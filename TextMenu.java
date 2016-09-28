package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 6/14/2016.
 */
public class TextMenu extends BasicEntity {

    Text prompt;

    int selected;

    Sound sSelect;

    BasicEntity[] children;

    TextMenu(String prompt, String... options) {
        super("TextMenu." + prompt);
        // Create prompt and make larger
        this.prompt = new Text(prompt);
        this.prompt.changeScale(0.5f);
        // Set the default selection to 0
        this.selected = -1;
        // Load sounds
        this.sSelect = super.loadSound(R.raw.tick1);
        // Add selections
        this.children = new BasicEntity[0];
        for (String option : options) {
            addChild(new Text(option));
        }
        // Allows custom init from overloaded functions
        this.init();
    }

    // Add function to be overloaded
    protected void init(){

    }

    @Override
    protected void update() {
        super.update();
        // Change y tilePosition to space out vertically
        float currenty = 3.5f,
                dy = -1f;
        // Update prompt
        prompt.centerX(this.position[0]);
        prompt.update(this.position[1] + currenty);
        prompt.position[2] = this.position[2];
        currenty += (dy * (this.prompt.scaleModifier + 1f));
        // Update selections
        for (BasicEntity selection : this.children) {
            // Cast the Text to use custom update function for y-pos
            Text text = (Text) selection;
            text.position[0] = this.position[0] + (text.getLength() / 2f - 0.5f);
            text.update(this.position[1] + currenty);
            text.position[2] = this.position[2];
            currenty += (dy * (text.scaleModifier + 1f));
        }
    }

    @Override
    protected void draw() {
        // Check if this is visible or nah
        if(!this.visible){
            return;
        }
        // Draw the prompt blue
        MaterialManager.changeMaterialColor("Letter",MaterialManager.getVector3fColor("Blue"));
        this.prompt.draw();
        int i = 0;
        for (BasicEntity selection : this.children) {
            // Draw the selected text green and the unselected red
            if (this.getSelection() != -1 && this.getSelection() == i) {
                MaterialManager.changeMaterialColor("Letter",MaterialManager.getVector3fColor("Green"));
            } else {
                MaterialManager.changeMaterialColor("Letter",MaterialManager.getVector3fColor("Red"));
            }
            // Cast the Text to use custom draw function for y-pos
            Text text = (Text) selection;
            text.draw();
            i++;
        }
    }

    protected void tapped(){
        //Check selections
        switch(this.getSelection()){
            case(0):
                this.tapped0();
                break;
            case(1):
                this.tapped1();
                break;
            case(2):
                this.tapped2();
                break;
            case(3):
                this.tapped3();
                break;
            case(4):
                this.tapped4();
                break;
            case(5):
                this.tapped5();
                break;
            case(6):
                this.tapped6();
                break;
            case(7):
                this.tapped7();
                break;
            case(8):
                this.tapped8();
                break;
            // Catch unhandled taps
            default:
                LOGE("Caught " + this.getSelection());
                break;
        }
    }

    protected int getSelection(){
        return this.selected;
    }

    protected Text handleTextSelection(BasicEntity child) {
        Text text = (Text) child;
        if (text.isTextSelection()) {
            TextSelection textSelection = (TextSelection) text;
            textSelection.currentSelection++;
        }
        return text;
    }

    protected void addChild(BasicEntity piece){
        for(BasicEntity piece1 : this.children){
            if(piece.ID == piece1.ID){
                LOGE("Trying to add duplicate chess piece");
                return;
            }
        }
        BasicEntity[] np = new BasicEntity[this.children.length + 1];
        System.arraycopy(this.children, 0, np, 0, this.children.length);
        this.children = np;
        this.children[this.children.length - 1] = piece;
    }

    protected void tapped0(){

    }

    protected void tapped1(){

    }

    protected void tapped2(){

    }

    protected void tapped3(){

    }

    protected void tapped4(){

    }

    protected void tapped5(){

    }

    protected void tapped6(){

    }

    protected void tapped7(){

    }

    protected void tapped8(){

    }

    protected int[] getData(){
        return null;
    }

}
