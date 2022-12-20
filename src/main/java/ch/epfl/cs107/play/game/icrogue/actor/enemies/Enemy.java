package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Enemy extends ICRogueActor {
    private boolean isDead;

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

    public boolean isDead(){
        return isDead;
    }
    public void killEnemy(){
        isDead = true;
    }
}
