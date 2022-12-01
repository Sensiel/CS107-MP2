package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Projectile extends ICRogueActor implements Consumable {
    public final static int DEFAULT_DAMAGE = 1;
    public final static int DEFAULT_MOVE_DURATION = 10;
    private int frameForMove;
    private int damage;
    private boolean isConsumed;

    private Sprite sprite;

    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        this(owner, orientation, coordinates, DEFAULT_DAMAGE, DEFAULT_MOVE_DURATION);
    }

    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates, int frameForMove, int damage) {
        super(owner, orientation, coordinates);
        this.frameForMove = frameForMove;
        this.damage = damage;
        this.isConsumed = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(!isDisplacementOccurs()){ // peut être mettre ça dans fire plutot que dans la classe abstract
            if(!move(frameForMove)){
                consume();
            }
        }
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
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

    public Sprite getSprite() {
        return sprite;
    }
}
