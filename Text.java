package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 6/14/2016.
 */
public class Text extends BasicEntity {

    float letter_spacing = 1.1f,
        scaleModifier;

    BasicEntity[] children;

    Text(String word) {
        super(word);
        this.children = new BasicEntity[0];
        // Handle letters
        for (char c : word.toCharArray()) {
            switch (Character.toLowerCase(c)) {
                case ('a'):
                case ('b'):
                case ('c'):
                case ('d'):
                case ('e'):
                case ('f'):
                case ('g'):
                case ('h'):
                case ('i'):
                case ('j'):
                case ('k'):
                case ('l'):
                case ('m'):
                case ('n'):
                case ('o'):
                case ('p'):
                case ('q'):
                case ('r'):
                case ('s'):
                case ('t'):
                case ('u'):
                case ('v'):
                case ('w'):
                case ('x'):
                case ('y'):
                case ('z'):
                case ('0'):
                case ('1'):
                case ('2'):
                case ('3'):
                case ('4'):
                case ('5'):
                case ('6'):
                case ('7'):
                case ('8'):
                case ('9'):
                    addChild(new Letter("Letters", c + ""));
                    break;
                case(' '):
                    addChild(new Letter(null, null));
                    break;
                case('>'):
                    addChild(new Letter("Letters", "pointer"));
                    break;
                default:
                    LOGE(String.format("Letter %c does not exist in library", c));
            }
        }
        //this.tilePosition = new float[]{this.getLength() / 2f, 0f, 5f};
        this.position[2] = 5.f;
        this.rotation[1] = 180f;

        for (BasicEntity child : this.children) {
            child.rotation = this.rotation;
        }

        this.changeScale(-0.2f); // Make text smaller
    }

    protected void changeScale(float newScale){
        this.scaleModifier = 1.f + newScale; // Messed up scaling needs fixing
        for(BasicEntity child : this.children) {
            child.scale = new float[]{newScale, newScale, newScale};
        }
    }

    protected float getLength() {
        return letter_spacing * this.children.length * this.scaleModifier;
    }

    protected void update(float ypos){
        super.update();
        this.position[1] = ypos;
        // Space out letters
        int i = 0;
        for(BasicEntity child : this.children){
            child.position = new float[]{this.position[0] - (i * letter_spacing * this.scaleModifier), this.position[1], this.position[2]};
            i++;
            child.update();
        }
    }

    public void centerX(float x){
        this.position[0] = x + (this.getLength() / 2f - 1f);
    }

    protected boolean isTextSelection(){
        return (this.TAG.endsWith("TEXTSELECTION"));
    }

    protected void addChild(BasicEntity piece){
        for(BasicEntity piece1 : children){
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

    protected int[] getData(){
        return null;
    }

    @Override
    protected void draw(){
        for(BasicEntity child : this.children){
            child.draw();
        }
    }

}
