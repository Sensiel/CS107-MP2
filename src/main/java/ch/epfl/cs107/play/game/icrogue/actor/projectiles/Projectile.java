package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable, Interactor {
    //Default damage attribute
    public final static int DEFAULT_DAMAGE = 1;

    //Default move duration attribute
    public final static int DEFAULT_MOVE_DURATION = 10;

    //Move duration of the projectile
    private int frameForMove;

    //Damage of the projectile
    private int damage;

    //Boolean of the current state of the projectile
    private boolean isConsumed;

    /**
     * Default Projectile constructor
     * @param owner (Area): Owner area. Not null
     * @param coordinates (Coordinate): Initial position of the projectile. Not null
     * @param orientation (Orientation): Initial orientation of the projectile. Not null
     */
    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        this(owner, orientation, coordinates, DEFAULT_DAMAGE, DEFAULT_MOVE_DURATION);
    }


    /**
     * Alternative Projectile constructor
     * @param owner (Area): Owner area. Not null
     * @param coordinates (Coordinate): Initial position of the projectile. Not null
     * @param orientation (Orientation): Initial orientation of the projectile. Not null
     * @param frameForMove (int) : Move duration of the projectile.
     * @param damage (int) : Damage amount of the projectile.
     */
    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates, int frameForMove, int damage) {
        super(owner, orientation, coordinates);
        enterArea(owner, coordinates);
        this.frameForMove = frameForMove;
        this.damage = damage;
        this.isConsumed = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(isConsumed())
            leaveArea();
        if(!isDisplacementOccurs()){
            move(frameForMove);
        }
    }

    @Override
    public void consume() {
        isConsumed = true;
    }
    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }
}
