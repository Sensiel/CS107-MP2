package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends AreaEntity implements Interactable {

    public enum State{
        OPEN(true, false, ""),
        CLOSED(false, true, "icrogue/door_"),
        LOCKED(false, true, "icrogue/lockedDoor_"),
        INVISIBLE(false, true, "icrogue/invisibleDoor_");

        //The sprite name of the connector state
        private final String spriteName;

        //Boolean indicating if the player can walk through a connector in this state
        private final boolean isWalkable;

        //Boolean indicating if a connector in this state has to be drawn
        private final boolean isDrawn;

        State(boolean isWalkable, boolean isDrawn, String spriteName){
            this.isWalkable = isWalkable;
            this.isDrawn = isDrawn;
            this.spriteName = spriteName;
        }
    }

    //Default id for a door with no key id given
    private final static int NO_KEY_ID = 0;

    //State of the connector
    private State state;

    // Title of the connector's destination
    private String destTitle;

    //Position in the destination room
    private DiscreteCoordinates posDest;

    // Key id of the connector
    private int keyID;

    /**
     * Default Connector constructor
     * @param owner (Area) : the owner Area
     * @param orientation (Orientation) : The orientation of the connector
     * @param coordinates (DiscreteCoordinates) : the coordinates of the connector
     */
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        this.state = State.INVISIBLE;
        keyID = NO_KEY_ID;
    }

    /**
     * Set the connector's state
     * @param state : the state to set
     */
    public void setState(State state){
        this.state = state;
    }


    /**
     * @return the connector's state
     */
    public State getState(){
        return state;
    }

    /**
     * Set the connector's destination title
     * @param areaTitle (String) : the title of the destination
     */
    public void setDestTitle(String areaTitle){
        destTitle = areaTitle;
    }

    /**
     * Set the destination position in the connector's destination room
     * @param pos (DiscreteCoordinates) : the coordinates of the destination position
     */
    public void setPosDest(DiscreteCoordinates pos){
        posDest = pos;
    }

    /**
     * @return the coordinates of the destination position
     */
    public DiscreteCoordinates getPosDest() {
        return posDest;
    }

    /**
     * @return the connector's destination title
     */
    public String getDestTitle() {
        return destTitle;
    }

    /**
     * Set the key ID
     * @param id : the id to set
     */
    public void setKeyID(int id){
        keyID = id;
    }

    /**
     * @return the key ID
     */
    public int getKeyID() {
        return keyID;
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates();
        return List.of(coord, coord.jump(new Vector((getOrientation().ordinal()+1)%2, getOrientation().ordinal()%2)));
    }

    @Override
    public boolean takeCellSpace() {
        return !state.isWalkable;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void draw(Canvas canvas) {
        if(getState().isDrawn){
            getSprite().draw(canvas);
        }
    }

    /**
     * @return Sprite : the current sprite of the connector. May be null if the connector doesn't need to be drawn
     */
    public Sprite getSprite(){
        if(!getState().isDrawn){
            return null;
        }

        return new Sprite(getState().spriteName + (getOrientation().ordinal() % 4),
                        (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this);
    }
}

