package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class Level0Room extends ICRogueRoom {
    public Level0Room(DiscreteCoordinates roomCoordinates){
        super("icrogue/Level0Room",roomCoordinates);
    }
    protected void createArea() {
        // Base

        registerActor(new Background(this, getBehaviorName()));
        //registerActor(new ICRoguePlayer(new Vector(20, 10), "ghost.2"));
    }


    public String getTitle(){
        return "icrogue/level0" + getRoomCoordinates().x + getRoomCoordinates().y;
    }
}
