package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Heart;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Positionable;
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
    private final static float MAX_HEALTH = 20;

    // Sprite de respectivement HAUT, DROITE, BAS, GAUCHE
    //private final Sprite[] spriteOrientated;
    private final List<Integer> keyIds;
    private final ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();
    private Animation currentAnimation;

    private final TextGraphics hpMeter;

    private final Animation[] orientatedAnimation ;
    private boolean ownStaff = false;
    private boolean isChangingRoom;
    private String nextArea = "";
    private DiscreteCoordinates nextAreaStartingPos;

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

        orientatedAnimation = loadPlayerAnimation(spriteName);
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(orientation)]);
        keyIds = new ArrayList<>();

        isChangingRoom = false;

        hp = MAX_HEALTH;
        hpMeter = new TextGraphics(getHealthBar(hp), 0.25f, getHealthColor(hp));
        hpMeter.setFontName("OpenSans-Bold");
        hpMeter.setParent(this);
        hpMeter.setAnchor(new Vector(-0.07f, 1.2f));
    }

    private Animation[] loadPlayerAnimation(String spriteName) {
        Sprite[] spritesDOWN = new Sprite[4], spritesLEFT = new Sprite[4], spritesUP= new Sprite[4], spritesRIGHT = new Sprite[4];
        Vector anchor = new Vector(.15f, -.15f);
        for(int iFrame = 0; iFrame < 4; ++iFrame) {
            spritesDOWN[iFrame] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(iFrame * 16,  0, 16, 32), anchor);
            spritesRIGHT[iFrame] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(iFrame * 16, 32, 16, 32), anchor);
            spritesUP[iFrame] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(iFrame * 16,  64, 16, 32), anchor);
            spritesLEFT[iFrame] = new Sprite(spriteName, .75f, 1.5f, this, new RegionOfInterest(iFrame * 16, 96 , 16, 32), anchor);
        }
        Animation animationsUP, animationsDOWN, animationsLEFT, animationsRIGHT;
        animationsDOWN = new Animation(ANIMATION_DURATION,spritesDOWN);
        animationsLEFT = new Animation(ANIMATION_DURATION,spritesLEFT);
        animationsUP = new Animation(ANIMATION_DURATION,spritesUP);
        animationsRIGHT = new Animation(ANIMATION_DURATION,spritesRIGHT);

         return new Animation[]{animationsDOWN, animationsLEFT, animationsUP, animationsRIGHT};
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        if(!isDisplacementOccurs())
            currentAnimation.reset();
        else
            currentAnimation.update(deltaTime);

        if(keyboard.get(Keyboard.X).isDown() && ownStaff()){
            Fire fire = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(), getCurrentMainCellCoordinates());
        }


        hpMeter.setText(getHealthBar(hp));
        hpMeter.setFillColor(getHealthColor(hp));
    }

    private String getHealthBar(float hp) {
        if(hp < 0){
            return "▁▁▁▁▁";
        }
        float percentage = hp*100f/MAX_HEALTH;
        String result = "";

        while(percentage >= 20f){
            result += "█";
            percentage -= 20f;
        }
        if(result.length() == 5)
            return result;
        String possibilities = "▁▁▂▂▂▂▃▃▄▄▄▄▅▅▆▆▆▆▇▇";
        result += possibilities.charAt(Integer.min((int)percentage,19));
        while(result.length() != 5)
            result += "▁";

        return result;
    }

    private Color getHealthColor(float hp){
        if(hp < 0){
            return Color.BLACK;
        }
        float hpNormalized = hp / MAX_HEALTH;
        float H = hpNormalized * 0.4f;
        float S = 0.9f;
        float B = 0.9f;

        return Color.getHSBColor(H, S, B);
    }

    private void setCurrentAnimation(Animation currentAnimation){
        this.currentAnimation = currentAnimation;
    }

    private void moveIfPressed(Orientation orientation, Button b){
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(getOrientation())]);
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                if(!orientation.equals(getOrientation()))
                    currentAnimation.reset();
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
        //setSprite(spriteOrientated[getSpriteIndexFromOrientation(getOrientation())]);
    }

    @Override
    public void draw(Canvas canvas) {
        //getSprite().draw(canvas);
        hpMeter.draw(canvas);
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

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {
            if(isCellInteraction){
                heart.collect();
                hp += 2.0f;
            }

        }
    }
}
