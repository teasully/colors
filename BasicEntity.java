package project.etrumper.thomas.ghostbutton;

import android.util.Log;

/**
 * Created by thoma on 2/13/2016.
 */
public class BasicEntity extends Logable {

    static int IDCounter = 0;

    int ID; // Unique ID

    float[] position, rotation, scale;

    float positionalVelocity, anglarVelocity, scalingVelocity,  // For changing values
            deltaTimeModifier;  // For changing speed of change

    boolean visible;    // For drawing

    Animation currentAnimation;

    RawModel model;

    Animation[] animations;

    Ease2 easeYPos;

    BasicEntity(String libraryObjectName) {
        super(libraryObjectName);

        this.ID = IDCounter++;

        this.position = new float[]{0f, 0f, 0f};
        this.rotation = new float[]{270f, 0f, 0f};
        this.scale = new float[]{1f, 1f, 1f};

        this.deltaTimeModifier = 1.f;   // Time modifier for slowing + speed

        this.visible = true;

        this.currentAnimation = null;

        this.model = new RawModel(TAG);

        this.animations = new Animation[0];
    }

    protected void update() {

        // Update animations
        for (Animation animation : this.animations) {
            animation.update();
        }
        // Check model data
        this.updateMesh();
        // Check eases
        if (this.easeYPos != null) {
            this.easeYPos.update();
            this.position[1] = (float) this.easeYPos.easeLinear();
            if (this.easeYPos.done()) {
                this.easeYPos = null;
            }
        }
    }

    protected void changeDeltaTimeModifier(float newModifier) {
        for (Animation animation : this.animations) {
            animation.deltaTimeModifier = newModifier;
        }
    }

    protected void interpolate(boolean value) {
        for (Animation animation : this.animations) {
            animation.interpolate = value;
        }
    }

    private void addAnimation(Animation animation) {
        Animation[] newAnims = new Animation[this.animations.length + 1];
        System.arraycopy(this.animations, 0, newAnims, 0, this.animations.length);
        newAnims[this.animations.length] = animation;
        this.animations = newAnims;
    }


    protected Animation addAnimation(String objectName, String animationName) {
        Animation animation = new Animation(objectName, animationName);
        this.addAnimation(animation);
        return animation;
    }

    protected Animation addAnimation(String animationName) {
        return this.addAnimation(this.TAG, animationName);
    }

    protected Animation addAnimation(String animationName, long timeInMilli, Animation first, Animation second, boolean interpolate) {
        Animation animation = Animation.getTweenedAnimation(this.TAG, animationName, timeInMilli, first, second, interpolate);
        this.addAnimation(animation);
        return animation;
    }

    protected void updateMesh() {
        if(this.currentAnimation == null){
            return;
        }
        int gotID = this.currentAnimation.getcurrentFrame();
        this.model.meshID = gotID;
    }

    protected void play() {
        this.currentAnimation.play();
    }

    protected void setCurrentAnimation(Animation setAnim) {
        this.currentAnimation = setAnim;
    }

    protected void randomizeRotation(int rotIndex, int numberDivisions) {
        int number = SuperManager.r.nextInt() % numberDivisions;
        float addRot = ((float) number / (float) numberDivisions) * 360.f;
        this.rotation[rotIndex] = addRot;
    }

    protected Sound loadSound(int rID){
        Sound s = null;
        try {
            s = SoundManager.loadSound(rID);
        }catch(MandatoryException e){
            LOGE(e.toString());
        }
        return s;
    }

    protected void startYEase(float desiredChange, long timeInMilli){
        this.easeYPos = new Ease2(this.position[1], desiredChange, timeInMilli);
    }

    /*protected void queueModel(String name){
        String[] newQueue = new String[this.queuedModels.length + 1];
        int i = 0;
        for(String model : this.queuedModels){
            newQueue[i++] = model;
        }
        newQueue[i] = name;
        this.queuedModels = newQueue;
    }*/

    /*protected void queueModel(String libraryName, String modelName){
        this.queueModel(String.format("%s.%s", libraryName, modelName));
    }*/

    /*protected void dequeueModel(String name){
        String[] newQueue = new String[this.queuedModels.length - 1];
        int u = 0;
        for(int i = 0; i < this.queuedModels.length; i++){
            String model = this.queuedModels[i];
            if(model.equals((name))){
               continue;
            }
            newQueue[u++] = model;
        }
        this.queuedModels = newQueue;
    }*/

    /*protected RawModel getRawModel(int index){
        if(index > this.attributes.length - 1){
            return null;
        }
        if(this.attributes[index].attributeType != AttributeType.RAWMODEL){
            return null;
        }
        return (RawModel)this.attributes[index];
    }*/

    protected void movePosition(float[] offset) {
        if (offset.length != 3) {
            LOGE("Trying to movePosition() with invalid offset[] length");
        } else {
            for (int i = 0; i < 3; i++) {
                if (Float.isNaN(offset[i])) {
                    LOGE(String.format("Trying to movePosition tilePosition index %d by NaN", i));
                } else {
                    this.position[i] += offset[i];
                }
            }
        }
    }

    protected void draw() {
        if (this.visible && this.currentAnimation != null) {
            this.currentAnimation.draw(this, this.model);
        }
    }

    /*protected float[] getModelMatrix(){
        float[] modelMatrix = new float[16];

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, tilePosition[0], tilePosition[1],tilePosition[2]);  // Combine global tilePosition with local tilePosition
        for(int i = 0; i < 3; i++){
            Matrix.rotateM(modelMatrix, 0, rotation[i],
                    (i == 0 ? 1f : 0f),
                    (i == 1 ? 1f : 0f),
                    (i == 2 ? 1f : 0f));    // Rotate for each axis
        }
        Matrix.scaleM(modelMatrix, 0, scale[0], scale[1], scale[2]); // Scale
        return modelMatrix;
    }*/

    /*protected float[] getAttributeModelMatrix(int attributeIdex){
        RawModel model = getRawModel(attributeIdex);
        if(model == null){
            return new float[]{};
        }
        return model.getModelMatrixWithGlobal(this.tilePosition, this.rotation, this.scale);
    }*/

    protected void pointCameraTo(){
        GameConstants.camera.target = new float[]{this.position[0], this.position[1], this.position[2]};
    }

    protected void LOGE(String message) {
        Log.e(this.TAG, message);
    }
}
