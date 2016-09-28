package project.etrumper.thomas.ghostbutton;


/**
 * Created by thoma on 9/10/2016.
 * Property of boxedworks.
 */

public class GO_TapperManager extends Logable {

    enum ManagerType {
        CASUAL,
        RUSHED
    }

    private enum ChunkType { // Spaces in-between
        INDICATOR_ONLY,     // 0
        INDICATOR_SLIGHT,   // 2
        NORMAL,             // 7
        LONG                // 20
    }

    private int active_tappers_height = 12;

    private GO_Tapper[][] tapper_buffer, tapper_active;

    private GO_Tapper[] garbage;

    private float startHeight,
            tapperDistance;

    private long penaltyTime = 12,
            level;

    long rowsCleared;

    private String lastIndicatorColor = "Red";

    boolean started;

    ManagerType type;

    GO_TapperManager(ManagerType type) {
        super("GO_TapperManager");
        this.type = type;
        this.setUpForNthColumns();
        // Init 2D vectors and iterate
        this.tapper_active = new GO_Tapper[active_tappers_height][GameConstants.numberColumns];
        // Populate buffer
        //this.usedTypes = new ChunkType[]{ChunkType.INDICATOR_ONLY};
        //this.generateBuffer(this.usedTypes[0]);
        this.generateWaveBuffer();
        if (this.type == ManagerType.CASUAL) {
            // Populate active tappers
            for (int i = 0; i < tapper_active.length; i++) {
                for (int u = 0; u < tapper_active[i].length; u++) {
                    this.tapper_active[i][u] = this.tapper_buffer[0][u];
                }
                this.moveDownBuffer();
            }
        }
        // Other local variables
        this.started = false;
        this.rowsCleared = 0;
        this.level = 0;
        this.garbage = new GO_Tapper[0];
    }

    private void setUpForNthColumns() {
        // Set local values based on columns
        switch (GameConstants.numberColumns) {
            case (2):
                this.initNeededValues(-2.5f, 6.5f);
                break;
            case (3):
                this.initNeededValues(-3.5f, 8.5f);
                break;
            case (4):
                this.initNeededValues(-5f, 10.5f);
                break;
            default:
                LOGE("Trying to setUpForNthColumns() unhandled switch " + GameConstants.numberColumns);
                break;
        }
    }

    private void initNeededValues(float startHeight, float tapperDistance) {
        this.startHeight = startHeight;
        this.tapperDistance = tapperDistance;
    }

    private void generateBuffer(ChunkType type) {
        // Check type and fill buffer with respective chunk
        if (type == ChunkType.INDICATOR_ONLY) {
            // Fill buffer with indicator rows
            this.tapper_buffer = this.generateBuffer(0, 10);
        } else if (type == ChunkType.INDICATOR_SLIGHT) {
            // Fill buffer with partially spaced out indicator rows
            this.tapper_buffer = this.generateBuffer(2, 7);
        } else if (type == ChunkType.NORMAL) {
            // Fill buffer spaced out normally
            this.tapper_buffer = this.generateBuffer(6, 4);
        } else if (type == ChunkType.LONG) {
            // Fill buffer with long spaced chunk
            this.tapper_buffer = this.generateBuffer(20, 1);
        }
        this.spaceTappers(this.tapper_buffer);
    }

    private GO_Tapper[][] spaceTappers(GO_Tapper[][] sent_buffer) {
        // Space buffer with spacing function
        float currentY = startHeight;
        for (int rowNum = 0; rowNum < sent_buffer.length; rowNum++, currentY += 1f) {
            // Get number in row
            for (int columnNum = 0; columnNum < sent_buffer[rowNum].length; columnNum++) {
                // Manually move to column (space out tappers) by checking global constant
                switch (GameConstants.numberColumns) {
                    case (2):
                        switch (columnNum) {
                            case (0):
                                sent_buffer[rowNum][columnNum].position = new float[]{1.5f, currentY, this.tapperDistance};
                                break;
                            case (1):
                                sent_buffer[rowNum][columnNum].position = new float[]{-1.5f, currentY, this.tapperDistance};
                                break;
                        }
                        break;
                    case (3):
                        switch (columnNum) {
                            case (0):
                                sent_buffer[rowNum][columnNum].position = new float[]{3f, currentY, this.tapperDistance};
                                break;
                            case (1):
                                sent_buffer[rowNum][columnNum].position = new float[]{0f, currentY, this.tapperDistance};
                                break;
                            case (2):
                                sent_buffer[rowNum][columnNum].position = new float[]{-3f, currentY, this.tapperDistance};
                                break;
                        }
                        break;
                    case (4):
                        switch (columnNum) {
                            case (0):
                                sent_buffer[rowNum][columnNum].position = new float[]{4.5f, currentY, this.tapperDistance};
                                break;
                            case (1):
                                sent_buffer[rowNum][columnNum].position = new float[]{1.5f, currentY, this.tapperDistance};
                                break;
                            case (2):
                                sent_buffer[rowNum][columnNum].position = new float[]{-1.5f, currentY, this.tapperDistance};
                                break;
                            case (3):
                                sent_buffer[rowNum][columnNum].position = new float[]{-4.5f, currentY, this.tapperDistance};
                                break;
                        }
                        break;
                }
            }
        }
        return sent_buffer;
    }

    private GO_Tapper[][] generateBuffer(final int spaceBetweenIndicators, final int numIndicators) {
        GO_Tapper[][] buffer = new GO_Tapper[(spaceBetweenIndicators * numIndicators) + numIndicators][GameConstants.numberColumns];
        for (int indicatorRowNum = 0; indicatorRowNum < numIndicators; indicatorRowNum++) {
            int currentIndex = indicatorRowNum * (spaceBetweenIndicators + 1);
            // Add indicator row
            String[] colors = this.getColors(true);
            for (int columnNumber = 0; columnNumber < GameConstants.numberColumns; columnNumber++) {
                buffer[currentIndex][columnNumber] = new GO_Tapper();
                buffer[currentIndex][columnNumber].color = colors[0];
            }
            // Add rows in-between
            for (int spaceNumber = 0; spaceNumber < spaceBetweenIndicators; spaceNumber++) {
                // Add random row
                colors = this.getColors(false);
                for (int columnNumber = 0; columnNumber < GameConstants.numberColumns; columnNumber++) {
                    buffer[currentIndex + (spaceNumber + 1)][columnNumber] = new GO_Tapper();
                    buffer[currentIndex + (spaceNumber + 1)][columnNumber].color = colors[columnNumber];
                }
            }
        }
        return buffer;
    }

    private void generateWaveBuffer() {
        this.level++;
        LOGE("Level up!");
        if (this.needed > 200) {
            this.needed -= 20;
        }
        if (this.needed < 200){
            LOGE("Max speed reached " + 200);
            this.needed = 200;
        }
        GO_Tapper[][] w0 = this.generateBuffer(0, 10 + (int)(level / 2f)),
                w1 = this.generateBuffer(7 + (int)(level / 2f), 3 + (int)(level / 5f)),
                w2 = this.generateBuffer(14 + (int)(level / 2f), 1 + (int)(level / 5f)),
                w3 = this.generateBuffer(2 + (int)(level / 5f), 7 + (int)(level / 10f));
        this.tapper_buffer = this.spaceTappers(GO_Tapper.combineArrays(w0, w1, w2, w3));
    }

    private String[] getColors(boolean indicatorRow) {
        String[] colors = new String[GameConstants.numberColumns];
        // Check if is indicator row
        if (indicatorRow) {
            String color = GameConstants.getRandomColor();
            // Check color against lastColor
            while (color.equals(this.lastIndicatorColor)) {
                color = GameConstants.getRandomColor();
            }
            this.lastIndicatorColor = color;
            for (int i = 0; i < colors.length; i++) {
                colors[i] = color;
            }
            return colors;
        }
        // Else, set COLORS evenly
        for (int i = 0; i < colors.length; i++) {
            String color = GameConstants.getRandomColor();
            // Check if color was already picked
            boolean foundColor = false;
            for (int u = 0; u < i; u++) {
                if (colors[u].equals(color)) {
                    foundColor = true;
                    break;
                }
            }
            // If was picked already, de-increment and repeat
            if (foundColor) {
                i--;
                continue;
            }
            // If is unique color
            colors[i] = color;
        }
        return colors;
    }

    public void handleColumnTap(int columnNumber) {
        if (this.type == ManagerType.RUSHED) {
            // Find row of nearest active
            int row = 0;
            for (; row < this.tapper_active.length; row++) {
                if (this.tapper_active[row][0] != null) {
                    break;
                }
            }
            // Check if row empty
            if (row == this.tapper_active.length) {
                if (!this.started) {
                    this.started = true;
                } else {
                    LOGE("No rows to update..waiting for rows to move down");
                }
                return;
            }
            if (!this.handleRow(row, columnNumber)) {
                //GameConstants.loseGame();
                this.needed -= this.penaltyTime;
            }
            return;
        }
        // Check if no more
        if (this.tapper_active[0][0] == null) {
            LOGE("Empty");
            return;
        }
        if (!this.started) {
            this.started = true;
        }
        if (!this.handleRow(0, columnNumber)) {
            // Penalty for tapping wrong color
            GameConstants.loseGame();
            //LOGE("Wrong color");
        }
    }

    private boolean handleRow(int row, int column) {
        // Get tapper in columnNumber index of this.tappers
        String tapperColor = this.tapper_active[row][column].color;
        // Check color against GameConstants.indicator for color
        String indicatorColor = GameConstants.indicator.color;
        if (indicatorColor.equals(tapperColor)) {
            this.destroyRow(row);
            // Increment rows cleared variable and update text
            this.rowsCleared++;
            GameConstants.scoreCounter.updateText();
            return true;
        }
        // Else, check if is indicator row
        else if (this.isIndicatorRow(this.tapper_active[row]) != null) {
            GameConstants.indicator.color = this.tapper_active[row][0].color;
            this.destroyRow(row);
            // Increment rows cleared variable and update text
            this.rowsCleared++;
            GameConstants.scoreCounter.updateText();
            return true;
        }
        return false;
    }

    private String isIndicatorRow(GO_Tapper[] tapper_row) {
        // Check null
        if(tapper_row[0] == null){
            return null;
        }
        // Check if all COLORS in row match
        String color = tapper_row[0].color;
        for (GO_Tapper tapper : tapper_row) {
            if (!tapper.color.equals(color)) {
                return null;
            }
        }
        return color;
    }

    private void destroyRow(int rowNumber) {
        // Check if is indicator row
        if (this.isIndicatorRow(this.tapper_active[rowNumber]) != null) {
            // Change indicator color
            GameConstants.indicator.color = this.tapper_active[rowNumber][0].color;
            // Shake camera
            GameConstants.camera.startShake(50, 1500);
            // Increment global time
            if (this.type == ManagerType.CASUAL) {
                GameConstants.timeLeftInMilli += 1000;
            }
        }
        for (int columnNumber = 0; columnNumber < this.tapper_active[rowNumber].length; columnNumber++) {
            // Set falling animation and play
            this.tapper_active[rowNumber][columnNumber].setCurrentAnimation(this.tapper_active[rowNumber][columnNumber].fall);
            this.tapper_active[rowNumber][columnNumber].play();
            // Add to garbage
            this.addToGarbage(this.tapper_active[rowNumber][columnNumber]);
            // Set to null
            this.tapper_active[rowNumber][columnNumber] = null;
        }
        if (this.type == ManagerType.CASUAL) {
            this.moveDownTappers();
        }
    }

    private void moveDownTappers() {
        // Check mode for rushed
        if (this.type == ManagerType.RUSHED) {
            // Check if last row is filled
            if (this.tapper_active[0][0] != null) {
                // Check if there are lives
                if (GameConstants.timeLeftInMilli <= 3333) {
                    GameConstants.loseGame();
                    return;
                }
                // De-increment LIVES and remove row
                GameConstants.timeLeftInMilli -= 3334;
                for(int i  = 0; i < this.tapper_active.length; i++){
                    for(int u = 0; u < this.tapper_active[i].length; u++){
                        // Set falling animation and play
                        this.tapper_active[i][u].setCurrentAnimation(this.tapper_active[i][u].fall);
                        this.tapper_active[i][u].play();
                        // Add to garbage
                        this.addToGarbage(this.tapper_active[i][u]);
                        // Set to null
                        this.tapper_active[i][u] = null;
                    }
                }
                // this.destroyRow(0);
                return;
            }
        }
        for (int rowNum = 0; rowNum < this.tapper_active.length; rowNum++) {
            for (int columnNum = 0; columnNum < this.tapper_active[rowNum].length; columnNum++) {
                if (this.tapper_active[rowNum][columnNum] != null) {
                    this.tapper_active[rowNum - 1][columnNum] = this.tapper_active[rowNum][columnNum];
                    this.tapper_active[rowNum - 1][columnNum].startYEase(-Math.abs(
                            this.tapper_active[rowNum - 1][columnNum].position[1] - ((startHeight) + 1f * (rowNum - 1))), needed);
                    this.tapper_active[rowNum][columnNum] = null;
                }
            }
        }
        // Check if buffer is empty and is in unlimited mode
        if (this.bufferIsEmpty()) {
            //this.generateRandomBuffer();
            this.generateWaveBuffer();
        }
        if (!this.bufferIsEmpty()) {
            // Get row from buffer
            int topRow = this.tapper_active.length - 1;
            for (int columnNumber = 0; columnNumber < GameConstants.numberColumns; columnNumber++) {
                this.tapper_active[topRow][columnNumber] = this.tapper_buffer[0][columnNumber];
                this.tapper_active[topRow][columnNumber].position[1] = (-3.5f + (1f * this.active_tappers_height));
            }
            // Update buffer
            this.moveDownBuffer();
        }
    }

    private void moveDownBuffer() {
        // Check if only one row left' set buffer to empty
        if (this.tapper_buffer[1][0] == null) {
            for (int columnNum = 0; columnNum < this.tapper_buffer[0].length; columnNum++) {
                this.tapper_buffer[0][columnNum] = null;
            }
            return;
        }
        // Move buffer down
        for (int rowNum = 1; rowNum < this.tapper_buffer.length; rowNum++) {
            for (int columnNum = 0; columnNum < this.tapper_buffer[rowNum].length; columnNum++) {
                this.tapper_buffer[rowNum - 1][columnNum] = this.tapper_buffer[rowNum][columnNum];
                this.tapper_buffer[rowNum][columnNum] = null;
            }
        }
    }

    private boolean bufferIsEmpty() {
        for (GO_Tapper[] tappers : this.tapper_buffer) {
            for (GO_Tapper tapper : tappers) {
                if (tapper != null) {
                    return false;
                }
            }
        }
        return true;
    }

    // Clock stuff
    private long currentTick, lastTick = 0, total = 0, needed = 500;

    public void update() {
        for (GO_Tapper[] tappers : this.tapper_active) {
            for (GO_Tapper tapper : tappers) {
                if (tapper != null)
                    tapper.update();
            }
        }
        this.updateGarbage();
        // Move down stuff slowly
        if (this.type == ManagerType.RUSHED) {
            // Time stuff
            if (lastTick == 0) {
                lastTick = System.currentTimeMillis();
            }
            currentTick = System.currentTimeMillis();
            total += currentTick - lastTick;
            lastTick = System.currentTimeMillis();
            if (total > needed) {
                total = 0;
                // Move down stuff
                this.moveDownTappers();
            }
        }
    }

    public void draw() {
        for (GO_Tapper[] tappers : this.tapper_active) {
            for (GO_Tapper tapper : tappers) {
                if (tapper != null)
                    tapper.draw();
            }
        }
        for(GO_Tapper tappers : this.garbage){
            if(tappers == null){
                LOGE("Trying to draw null tapper");
                continue;
            }
            tappers.draw();
        }
    }

    private void addToGarbage(GO_Tapper tapper){
        // Make new array
        GO_Tapper[] newGarbage = new GO_Tapper[this.garbage.length + 1];
        // Copy to array
        System.arraycopy(this.garbage, 0, newGarbage, 0, this.garbage.length);
        newGarbage[this.garbage.length] = tapper;
        // Set new array
        this.garbage = newGarbage;
    }

    private void updateGarbage(){
        // Count number of done trash pieces
        int numberOTrash = 0;
        for(int i = 0; i < this.garbage.length; i++){
            if(this.garbage[i] == null || !this.garbage[i].currentAnimation.isPlaying){
                numberOTrash++;
                continue;
            }
            this.garbage[i].update();
        }
        if(numberOTrash == 0){
            return;
        }
        // Make new trash based off of that
        GO_Tapper[] newTrash = new GO_Tapper[this.garbage.length - numberOTrash];
        int currentIt = 0;
        for(int i = 0; i < this.garbage.length; i++){
            if(this.garbage[i] != null && this.garbage[i].currentAnimation.isPlaying){
                newTrash[currentIt++] = this.garbage[i];
            }
        }
        // Set new garbage
        this.garbage = newTrash;
    }
}
