package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends AreaEntity implements Interactable {

    public enum State{
        OPEN(),
        CLOSED(),
        LOCKED(),
        INVISIBLE();


    }
    private State state;
    private String destinationTitle;
    private DiscreteCoordinates positionArrivee;
    private int identificateur;
    private final static int NO_KEY_ID = 0 ;//0?

    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        this.state = State.INVISIBLE;
        identificateur= NO_KEY_ID;
    }
    /*
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates,int identificateur) {
        super(owner, orientation, coordinates);
        this.state = State.INVISIBLE;
        this.identificateur= identificateur;
    }
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates,State state) {
        super(owner, orientation, coordinates);
        this.state = state;
        identificateur= NO_KEY_ID;
    }
    */
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates,State state, String destinationTitle, DiscreteCoordinates positionArrivee,int identificateur){
        super(owner,orientation,coordinates);
        this.state=state;
        this.destinationTitle=destinationTitle;
        this.positionArrivee=positionArrivee;
        this.identificateur=identificateur;
    }
    /*
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String destinationTitle, DiscreteCoordinates positionArrivee,int identificateur){
        super(owner,orientation,coordinates);
        state=State.INVISIBLE;
        this.destinationTitle=destinationTitle;
        this.positionArrivee=positionArrivee;
        this.identificateur=identificateur;
    }
     */

    public void setState(State state){
        this.state=state;
    }
    public State getState(){
        return state;
    }

    public void setDestinationTitle(String areaTitle){
        destinationTitle=areaTitle;
    }

    public void setPositionArrivee(DiscreteCoordinates pos){
        positionArrivee=pos;
    }

    public void setIdentificateur(int id){
        identificateur=id;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates(); return List.of(coord, coord.jump(new
                Vector((getOrientation().ordinal()+1)%2, getOrientation().ordinal()%2)));    }

    @Override
    public boolean takeCellSpace() {
        if(state.equals(State.OPEN)){
            return false;
        } else { return true; }
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }


    @Override
    public void draw(Canvas canvas) {
        switch(state){
            case INVISIBLE ->
                    (new Sprite("icrogue/invisibleDoor_"+getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this)).draw(canvas);
            case CLOSED ->
                    (new Sprite("icrogue/door_"+getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this)).draw(canvas);
            case LOCKED ->
                    (new Sprite("icrogue/lockedDoor_"+getOrientation().ordinal(), (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1,
                            this)).draw(canvas);

        }// je sais pas si c'est une bonne encapsulation ?

    }

}

