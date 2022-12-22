package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Heart;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0IceRoom extends Level0ItemRoom{

    /**
     * Default Level0IceRoom constructor
     * @param roomCoordinates ( DiscreteCoordinates ) : the coordinates of the room
     */
    public Level0IceRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        Heart heart = new Heart(this, Orientation.LEFT,new DiscreteCoordinates(2,3));
        addItem(heart);
    }

    @Override
    protected String getBehaviorName() {
        return "icrogue/Level0IceRoom";
    }
}
