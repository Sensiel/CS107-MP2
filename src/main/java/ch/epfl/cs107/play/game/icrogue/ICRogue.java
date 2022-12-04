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

    public final static float CAMERA_SCALE_FACTOR = 11;

    private ICRoguePlayer player;
    private Level0 currentLevel;

    @Override
    public String getTitle() {
        return "ICRogue";
    }
    private void initLevel(){
        currentLevel = new Level0();
        currentLevel.addAreas(this);
        setCurrentArea(currentLevel.getStartRoomTitle(), true);
        player = new ICRoguePlayer(getCurrentArea(), Orientation.UP, currentLevel.getGlobalPosBeginning(),"zelda/player");
        //player.enterArea(getCurrentArea(), currentLevel.getGlobalPosBeginning()); // Il entre déjà avec le constructeur
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
        if (keyboard.get(Keyboard.R).isPressed()) {
            initLevel();
        }
        if(player.isChangingRoom()){
            switchRoom();
            player.setChangingRoom(false);
        }
    }

    protected void switchRoom() {
        player.leaveArea();
        ICRogueRoom nextRoom = (ICRogueRoom) setCurrentArea(player.getNextArea(), false);
        player.enterArea(nextRoom, currentLevel.getGlobalPosBeginning());
    }

    @Override
    public void end() {
    }
}

