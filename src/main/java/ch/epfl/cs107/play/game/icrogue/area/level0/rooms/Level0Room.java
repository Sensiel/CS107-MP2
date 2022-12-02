package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
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
        registerActor(new Cherry(this, Orientation.DOWN,new DiscreteCoordinates(6,3)));
        registerActor(new Staff(this,Orientation.DOWN,new DiscreteCoordinates(4,3)));
        //registerActor(new ICRoguePlayer(new Vector(20, 10), "ghost.2"));
    }


    public String getTitle(){
        return "icrogue/level0" + getRoomCoordinates().x + getRoomCoordinates().y;
    }
}
