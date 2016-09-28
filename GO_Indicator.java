package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 9/10/2016.
 * Property of boxedworks.
 */

public class GO_Indicator extends BasicEntity {

    String color;

    GO_Indicator(){
        super("Scenery1");

        this.setCurrentAnimation(this.addAnimation("cube"));

        this.position = new float[]{0f, -9f, 11f};
        // Get color
        this.color = GameConstants.getRandomColor();
    }

    @Override
    protected void draw(){
        MaterialManager.changeMaterialColor("Wall", MaterialManager.getVector3fColor(color));
        super.draw();
    }

}
