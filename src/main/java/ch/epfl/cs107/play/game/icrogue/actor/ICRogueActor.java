package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class ICRogueActor extends MovableAreaEntity {

    //The actor's Sprite
    private Sprite sprite;

    /**
     * Default ICRogueActor constructor
     * @param owner : The Area owner
     * @param orientation : The starting orientation of the actor
     * @param coordinates : The starting coordinates of the actor
     */
    protected ICRogueActor(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        resetMotion();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Make the actor leave its Area
     */
    public void leaveArea(){
        getOwnerArea().unregisterActor(this);
    }


    /**
     * Make the actor enter a given Area
     * @param area : the Area in which the actor enter
     * @param position :  the position of the actor in this Area
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }


    /**
     * @return the actor's sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Set a specific sprite to the actor
     * @param sprite (Sprite) : the sprite to set
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}

