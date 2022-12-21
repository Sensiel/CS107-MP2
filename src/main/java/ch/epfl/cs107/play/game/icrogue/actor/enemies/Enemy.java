package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Enemy extends ICRogueActor {

    //Boolean indicating if the enemy is dead
    private boolean isDead;

    /**
     * Default Enemy constructor
     * @param owner (Area): Owner area. Not null
     * @param coordinates (Coordinate): Initial position of the Enemy. Not null
     * @param orientation (Orientation): Initial orientation of the Enemy. Not null
     */
    protected Enemy(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        isDead = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(isDead()){
            leaveArea();
        }
    }


    /**
     * @return if the enemy is ded
     */
    public boolean isDead(){
        return isDead;
    }

    /**
     * Kill the enemy
     */
    public void killEnemy(){
        isDead = true;
    }
}
