package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0ItemRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {

    //Durée de mouvement
    private final static int MOVE_DURATION = 8;

    // Sprite de respectivement HAUT, DROITE, BAS, GAUCHE
    private Sprite[] spriteOrientated; // TODO ajouter des index reconnaissables ( genre HAUT = 0 etc ) ou alors changer de technique pour stocker les sprite
    private ArrayList<Integer> keyIds;

    private ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();

    private boolean ownStaff = false;

    private boolean isChangingRoom;
    private String nextArea = "";

    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) { // spriteName : zelda/player
        super(owner, orientation, coordinates);
        spriteOrientated = new Sprite[]{
            new Sprite(spriteName, .75f, 1.5f, this,
                new RegionOfInterest(0, 64, 16, 32), new Vector(.15f, -.15f)),
            new Sprite(spriteName, .75f, 1.5f, this,
                new RegionOfInterest(0, 32, 16, 32), new Vector(.15f, -.15f)),
            new Sprite(spriteName, .75f, 1.5f, this,
                new RegionOfInterest(0, 0, 16, 32), new Vector(.15f, -.15f)),
            new Sprite(spriteName, .75f, 1.5f, this,
                new RegionOfInterest(0, 96, 16, 32), new Vector(.15f, -.15f))
        };
        setSprite(spriteOrientated[getSpriteIndexFromOrientation(getOrientation())]);
        keyIds = new ArrayList<Integer>();
        isChangingRoom = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        if(keyboard.get(Keyboard.P).isPressed()){ // pour afficher debug
            System.out.println(getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells()));
        }

        if(keyboard.get(Keyboard.X).isDown() && ownStaff()){
            Fire fire = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
        }
    }

    private void moveIfPressed(Orientation orientation, Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
        setSprite(spriteOrientated[getSpriteIndexFromOrientation(getOrientation())]);
    }

    @Override
    public void draw(Canvas canvas) {
        getSprite().draw(canvas);
    }

    private int getSpriteIndexFromOrientation(Orientation orientation){
        return switch (orientation) {
            case UP -> 0;
            case RIGHT -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
        };
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
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
        Keyboard keyboard= getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.W).isDown();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    public boolean ownStaff() {
        return ownStaff;
    }

    public boolean isChangingRoom() {
        return isChangingRoom;
    }

    public void setChangingRoom(boolean changingRoom) {
        isChangingRoom = changingRoom;
    }

    public String getNextArea() {
        return nextArea;
    }

    @Override
    public void enterArea(Area area, DiscreteCoordinates position) {
        super.enterArea(area, position);
        ((ICRogueRoom)getOwnerArea()).setIsRoomVisited(true);
    }

    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if(isCellInteraction){
                cherry.collect();
            }
        }

        @Override
        public void interactWith(Staff staff, boolean isCellInteraction) {
            if(!isCellInteraction){
                ownStaff = true;
                staff.collect();
            }
        }

        @Override
        public void interactWith(Key key, boolean isCellInteraction) {
            if(isCellInteraction){
                key.collect();
                keyIds.add(key.getId());
            }
        }

        @Override
        public void interactWith(Connector connector, boolean isCellInteraction){
            if(isCellInteraction && !isDisplacementOccurs()){
                setChangingRoom(true);
                nextArea = connector.getDestTitle();
            }
            else if(!isCellInteraction){
                if(connector.getState().equals(Connector.State.LOCKED)  && ((Level0KeyRoom)getOwnerArea()).isOn()){
                    connector.setState(Connector.State.OPEN);
                }
                else if(connector.getState().equals(Connector.State.CLOSED)){
                    connector.setState(Connector.State.OPEN);
                }
            }
            //&& keyIds.contains(connector.getKeyID())
        }
    }
}
