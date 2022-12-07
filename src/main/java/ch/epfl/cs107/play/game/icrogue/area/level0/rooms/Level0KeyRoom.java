package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0KeyRoom extends Level0ItemRoom{
    public Level0KeyRoom(DiscreteCoordinates roomCoordinates, int keyID) {
        super(roomCoordinates);
        Key key = new Key(this, Orientation.UP, new DiscreteCoordinates(5,5), keyID);
        addItem(key);
        // TODO maybe utiliser une constante pour la position de creation
    }
}
