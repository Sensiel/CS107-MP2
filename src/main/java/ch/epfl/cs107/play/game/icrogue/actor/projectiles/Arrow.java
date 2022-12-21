package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile{
    //Arrow damage amount
    private final static float ARROW_DAMAGE = 1;

    //Arrow's handler
    private final Arrow.ICRogueArrowInteractionHandler handler = new Arrow.ICRogueArrowInteractionHandler();

    /**
     * Default Arrow constructor
     * @param owner (Area): Owner area. Not null
     * @param coordinates (Coordinate): Initial position of the fire. Not null
     * @param orientation (Orientation): Initial orientation of the fire. Not null
     */
    public Arrow(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates, 5, ARROW_DAMAGE);
        setSprite(new Sprite("zelda/arrow", 1f, 1f, this,
                new RegionOfInterest(32*orientation.ordinal(), 0, 32, 32),
                new Vector(0, 0)));
    }

    @Override
    public void draw(Canvas canvas) {
        getSprite().draw(canvas);
    }
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class ICRogueArrowInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(!isCellInteraction) {
                if (cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.WALL) || cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.HOLE)) {
                    consume();
                }
            }
        }
        @Override
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction) {
            if(isCellInteraction && !isConsumed()) {
                player.updateHp(-ARROW_DAMAGE);
                consume();
            }
        }
    }
}
