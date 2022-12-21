package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 11f;

    private ICRoguePlayer player;

    //The level the player is currently in
    private Level0 currentLevel;

    @Override
    public String getTitle() {
        return "ICRogue";
    }


    /**
     * Method initialising the current Level
     */
    private void initLevel(){
        currentLevel = new Level0();
        currentLevel.addAreas(this);
        setCurrentArea(currentLevel.getStartRoomTitle(), true);
        player = new ICRoguePlayer(getCurrentArea(), Orientation.UP, currentLevel.getPlayerStartingPos(),"zelda/player");
        player.enterArea(getCurrentArea(), currentLevel.getPlayerStartingPos());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            initLevel();
            return true;
        }
        return false;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getWindow().getKeyboard();
        // If the R key is pressed, the level is reset
        if (keyboard.get(Keyboard.R).isPressed()) {
            initLevel();
        }
        // If the player is changing rooms
        if(player.isChangingRoom()){
            switchRoom();
            player.setChangingRoom(false);
        }
        //If the game is over (by level resolution or player death)
        if(currentLevel.isOn()){
            System.out.println("Win");
        }
        else if(player.isDead()){
            System.out.println("Game Over");
        }
    }

    /**
     * Utility method making the player switching current Room
     */
    protected void switchRoom() {
        player.leaveArea();
        //We get the next room the player will enter
        ICRogueRoom nextRoom = (ICRogueRoom) setCurrentArea(player.getNextArea(), false);
        player.enterArea(nextRoom, player.getNextAreaStartingPos());
    }

    @Override
    public void end() {
    }
}

