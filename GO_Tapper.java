package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 9/10/2016.
 * Property of boxedworks.
 */

public class GO_Tapper extends BasicEntity {

    String color;

    Animation fall;

    GO_Tapper(){
        super("Scenery1");
        this.setCurrentAnimation(this.addAnimation("cube"));

        this.scale = new float[]{2f, 0.3f, 0f};

        this.fall = this.addAnimation("Tapper", "fall");
        this.fall.changeFrameRate(60 + (SuperManager.r.nextLong() % 10));

        this.rotation[1] = 180f;
    }

    @Override
    protected void draw(){
        MaterialManager.changeMaterialColor("Wall", MaterialManager.getVector3fColor(this.color));
        super.draw();
    }

    public static GO_Tapper[][] combineArrays(GO_Tapper[][] ... arrays){
        GO_Tapper[][] returnArray = new GO_Tapper[0][0];
        for(GO_Tapper[][] tappers : arrays){
            GO_Tapper[][] newTappers = new GO_Tapper[returnArray.length + tappers.length][GameConstants.numberColumns];
            System.arraycopy(returnArray, 0, newTappers, 0, returnArray.length);
            System.arraycopy(tappers, 0, newTappers, returnArray.length, tappers.length);
            returnArray = newTappers;
        }
        return returnArray;
    }

}
