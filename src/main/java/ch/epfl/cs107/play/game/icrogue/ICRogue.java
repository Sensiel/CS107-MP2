package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 11;

    private ICRoguePlayer player;

    private Level0Room currentRoom;
    private final String[] areas = {"icrogue/Level0Room"};
    private int areaIndex;



    @Override
    public String getTitle() {
        return "ICRogue";
    }
    private void initLevel(){
        currentRoom = new Level0Room(new DiscreteCoordinates(0,0));
        addArea(currentRoom);
        setCurrentArea(currentRoom.getTitle(), true);
        player= new ICRoguePlayer(currentRoom,Orientation.UP,new DiscreteCoordinates(2,2),"zelda/player");
        player.enterArea(currentRoom,new DiscreteCoordinates(2,2));
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            initLevel();
            areaIndex = 0;
            //initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = getWindow().getKeyboard();
        if (keyboard.get(Keyboard.R).isDown()) {
            initLevel();
        }
    }

    @Override
    public void end() {
    }
    /*private void initArea(String areaKey) {
        ICRogueRoom area = (ICRogueRoom) setCurrentArea(areaKey, true);
        //DiscreteCoordinates coords = area.getPlayerSpawnPosition();
       // player = new ICRoguePlayer(area, Orientation.DOWN, coords,"ghost.1");
        //player.enterArea(area, coords);
       // player.centerCamera();
    }*/
}

