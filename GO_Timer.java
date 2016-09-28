package project.etrumper.thomas.ghostbutton;

/**
 * Created by thoma on 9/13/2016.
 * Property of boxedworks.
 */

public class GO_Timer extends BasicEntity {

    long currentTick, lastTick = System.currentTimeMillis();

    String color;

    GO_Timer() {
        super("Scenery1");
        this.setCurrentAnimation(this.addAnimation("cube"));

        this.position = new float[]{0f, -10f, 8.5f};
    }

    @Override
    protected void update(){
        super.update();
        // Check if lost
        if(GameConstants.timeLeftInMilli <= 0){
            GameConstants.loseGame();
            return;
        }
        // Set width using global time
        this.scale[0] = -1.f + (GameConstants.timeLeftInMilli / 1000.f);
        // Only update if game has started
        if(!GameConstants.tappers.started || GameConstants.mode == GO_TapperManager.ManagerType.RUSHED){
            this.lastTick = System.currentTimeMillis();
            return;
        }
        // De-increment global time
        currentTick = System.currentTimeMillis();
        GameConstants.timeLeftInMilli -= currentTick - lastTick;
        lastTick = currentTick;
    }

    @Override
    protected void draw(){
        this.color = GameConstants.indicator.color;
        MaterialManager.changeMaterialColor("Wall", MaterialManager.getVector3fColor(this.color));
        super.draw();
    }
}
