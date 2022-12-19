package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Heart;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0HeartRoom extends Level0ItemRoom{
    public Level0HeartRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        Heart heart = new Heart(this, Orientation.LEFT,new DiscreteCoordinates(2,4));
        addItem(heart);
    }
}
