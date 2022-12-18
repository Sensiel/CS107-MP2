package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor {

    //Durée de mouvement
    private final static int MOVE_DURATION = 8;
    private final static int ANIMATION_DURATION = MOVE_DURATION/2;

    // Sprite de respectivement HAUT, DROITE, BAS, GAUCHE
    //private final Sprite[] spriteOrientated; // TODO ajouter des index reconnaissables ( genre HAUT = 0 etc ) ou alors changer de technique pour stocker les sprite
    private final ArrayList<Integer> keyIds;
    private final ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();
    private Animation currentAnimation;

    private TextGraphics message;

    private final Animation[] orientatedAnimation ;
    private boolean ownStaff = false;

    private boolean isChangingRoom;
    private String nextArea = "";
    private DiscreteCoordinates nextAreaStartingPos;
    private final Animation animationsUP, animationsDOWN, animationsLEFT, animationsRIGHT;

    private float hp;
/*
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
        keyIds = new ArrayList<>();
        isChangingRoom = false;
        hp = 10;
    }

 */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) { // spriteName : zelda/player
        super(owner, orientation, coordinates);
        Sprite[] spritesDOWN = new Sprite[4],spritesLEFT = new Sprite[4],spritesUP= new Sprite[4],spritesRIGHT = new Sprite[4];
        Vector anchor = new Vector(.15f, -.15f);
        for(int i=0;i<4;++i) {
            spritesDOWN[i] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(i*16,  0, 16, 32), anchor);
            spritesLEFT[i] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(i*16,  16, 16, 32), anchor);
            spritesUP[i] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(i*16, 32, 16, 32), anchor);
            spritesRIGHT[i] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(i*16, 48 , 16, 32), anchor);
        }
        animationsDOWN = new Animation(ANIMATION_DURATION,spritesDOWN);
        animationsLEFT = new Animation(ANIMATION_DURATION,spritesLEFT);
        animationsUP = new Animation(ANIMATION_DURATION,spritesUP);
        animationsRIGHT = new Animation(ANIMATION_DURATION,spritesRIGHT);

        orientatedAnimation = new Animation[]{animationsDOWN, animationsLEFT, animationsUP, animationsRIGHT};
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(orientation)]);
        keyIds = new ArrayList<>();
        isChangingRoom = false;
        hp = 10;
        message = new TextGraphics(Integer.toString((int)hp), 0.4f, Color.LIGHT_GRAY);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
    }

    @Override
    public void update(float deltaTime) {
        currentAnimation.update(deltaTime);

        super.update(deltaTime);
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        /*if(keyboard.get(Keyboard.P).isPressed()){ // pour afficher debug
            System.out.println(getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells()));
        }*/

        if(keyboard.get(Keyboard.X).isDown() && ownStaff()){
            Fire fire = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(), getCurrentMainCellCoordinates());
        }
        message.setText(Integer.toString((int)hp));



    }
    private void setCurrentAnimation(Animation currentAnimation){
        this.currentAnimation=currentAnimation;
    }

    private void moveIfPressed(Orientation orientation, Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
                currentAnimation.reset();

            }
        }
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(orientation)]);
        //setSprite(spriteOrientated[getSpriteIndexFromOrientation(getOrientation())]);
    }

    @Override
    public void draw(Canvas canvas) {
        //getSprite().draw(canvas);
        message.draw(canvas);
        getCurrentAnimation().draw(canvas);
    }
    private Animation getCurrentAnimation(){ return currentAnimation;}
/*
    private int getSpriteIndexFromOrientation(Orientation orientation){
        return switch (orientation) {
            case UP -> 0;
            case RIGHT -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
        };
    }

 */
    private int getAnimationIndexFromOrientation(Orientation orientation){
        return switch (orientation){
            case DOWN -> 0;
            case LEFT -> 1;
            case UP -> 2;
            case RIGHT -> 3;
        };
    }
    public void updateHp(float damage){
        hp -= damage;
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

    @Override
    public void enterArea(Area area, DiscreteCoordinates position) {
        super.enterArea(area, position);
        ((ICRogueRoom)getOwnerArea()).setRoomVisited(true);
    }

    public DiscreteCoordinates getNextAreaStartingPos() {
        return nextAreaStartingPos;
    }

    public String getNextArea() {
        return nextArea;
    }

    public boolean isDead(){
        return (hp <= 0);
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
                System.out.println(connector.getPosDest());
                nextAreaStartingPos = connector.getPosDest();
            }
            else if(!isCellInteraction){
                if(connector.getState().equals(Connector.State.LOCKED) && keyIds.contains(connector.getKeyID())){
                    connector.setState(Connector.State.OPEN);
                }
            }
        }

        @Override
        public void interactWith(Turret turret, boolean isCellInteraction) {
            if(isCellInteraction ){
                turret.killEnemy();
            }
        }
    }
}
