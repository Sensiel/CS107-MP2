package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.AngryBall;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Bomb extends Projectile {

    // Handler of the Bomb

    private final Bomb.ICRogueBombInteractionHandler handler = new Bomb.ICRogueBombInteractionHandler();

    /**
     * Default Bomb constructor
     * @param owner (Area): Owner area. Not null
     * @param coordinates (Coordinate): Initial position of the bomb. Not null
     * @param orientation (Orientation): Initial orientation of the bomb. Not null
     */
    public Bomb(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        setSprite(new Sprite("zelda/bomb",0.75f,0.75f,this,new RegionOfInterest(16,0,16,16)));
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


    private class ICRogueBombInteractionHandler implements ICRogueInteractionHandler {
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if(!isCellInteraction) {
                if (cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.WALL) || cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.HOLE)) {
                    consume();
                }
            }
        }
        public void interactWith(AngryBall angryBall, boolean isCellInteraction) {
            if(isCellInteraction && !isConsumed()){
                consume();
                angryBall.killEnemy();
            }
        }
    }
}


