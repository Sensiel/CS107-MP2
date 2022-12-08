package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

/**
 * Specific Item
 */
public class Key extends Item {
    //Id of the key
    private final int id;

    /**
     * Default Key constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the key. Not null
     * @param orientation (Orientation): Initial orientation of the key. Not null
     * @param id (int): Id of the key. Not null
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position,int id) {
        super(area, orientation, position);
        this.id = id;
        setSprite(new Sprite("icrogue/key", 0.6f, 0.6f, this)); //pas sure
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    /**
     * @return the id of this key
     */
    public int getId() {
        return id;
    }
}
