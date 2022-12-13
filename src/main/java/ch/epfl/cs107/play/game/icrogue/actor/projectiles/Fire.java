package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Specific projectile
 */
public class Fire extends Projectile{

    private final ICRogueFireInteractionHandler handler = new ICRogueFireInteractionHandler();

    /**
     * Default Fire constructor
     * @param owner (Area): Owner area. Not null
     * @param coordinates (Coordinate): Initial position of the fire. Not null
     * @param orientation (Orientation): Initial orientation of the fire. Not null
     */
    public Fire(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates, 5, 1);
        setSprite(new Sprite("zelda/fire", 1f, 1f, this ,
                    new RegionOfInterest(0, 0, 16, 16),
                    new Vector(0, 0)));
    }

    @Override
    public void draw(Canvas canvas) {
        getSprite().draw(canvas);
    }


    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(!isCellInteraction) {
                if (cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.WALL) || cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.HOLE)) {
                    consume();
                }
            }
        }
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(isCellInteraction && !isConsumed()){
                consume();
                turret.killEnemy();
            }
        }
    }
}
