package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0StaffRoom extends Level0ItemRoom{
    /**
     * Default Level0StaffRoom constructor
     * @param roomCoordinates ( DiscreteCoordinates ) : the coordinates of the room
     */
    public Level0StaffRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        Staff staff = new Staff(this, Orientation.UP, new DiscreteCoordinates(5,5));
        addItem(staff);
    }
}
