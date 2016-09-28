package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 9/20/2016.
 * Property of boxedworks.
 */

public class GO_ScoreCounter extends Logable{

    Text scoreText;
    BasicEntity background;

    GO_ScoreCounter(){
        super("ScoreCounter");

        this.scoreText = new Text(GameConstants.tappers.rowsCleared+"");
        this.scoreText.centerX(0.6f);
        this.scoreText.changeScale(-0.25f);

        this.background = new BasicEntity("Scenery1");
        this.background.setCurrentAnimation(this.background.addAnimation("cube"));
        this.background.position = new float[]{0f, 6.4f, this.scoreText.position[2]};
        this.background.scale[0] = 10f;
        this.background.scale[2] = 4f;
        this.background.scale[1] = -0.9f;
    }

    public void update(){
        scoreText.update(4.35f);
        background.update();
    }

    public void updateText() {
        // Update text with new scoreText
        scoreText = new Text(GameConstants.tappers.rowsCleared + "");
        // Move text to top-center screen + scale
        this.scoreText.centerX(0.6f);
        this.scoreText.changeScale(-0.25f);
    }

    public void draw(){
        MaterialManager.changeMaterialColor("Letter", MaterialManager.getVector3fColor(GameConstants.indicator.color));
        scoreText.draw();
        MaterialManager.changeMaterialColor("Wall", MaterialManager.getVector3fColor("Brown"));
        background.draw();
    }

}
