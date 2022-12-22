package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
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

    //Movement time
    private final static int MOVE_DURATION = 8;

    //Duration of the motion animation
    private final static int ANIMATION_DURATION = MOVE_DURATION/2;

    //Maximum player life
    private final static float MAX_HEALTH = 20;

    //Key Id already collected
    private final List<Integer> keyIds;

    //Handler of the player
    private final ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();

    //Current animation
    private Animation currentAnimation;

    //Life bar
    private final TextGraphics hpMeter;

    //Array of different motion animations depending on the orientation
    private final Animation[] orientatedAnimation ;

    //Boolean of possession of magic stick
    private boolean ownStaff = false;

    //Boolean for room change
    private boolean isChangingRoom;

    //Name of the room the player enters
    private String nextArea = "";

    //Position of the player when entering the next room
    private DiscreteCoordinates nextAreaStartingPos;

    //player's current health
    private float hp;

    // boolean telling if the player is on ice
    private boolean onIce;


    //Old constructor for sprite ( if you uncomment this one please comment the other, and do the modification in the update method)
    /*

    // Sprite of respectively UP, RIGHT, DOWN, LEFT
    private final Sprite[] spriteOrientated;

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

    private int getSpriteIndexFromOrientation(Orientation orientation){
        return switch (orientation) {
            case UP -> 0;
            case RIGHT -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
        };
    }

    */

    /**
     * Default ICRogueRoom constructor
     * @param owner (Area) : the owner Area
     * @param orientation (Orientation) : the starting Orientation of the player
     * @param coordinates (DiscreteCoordinates) : the starting coordinates of the player
     * @param spriteName (String) : the sprite name of the player
     */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName) {
        super(owner, orientation, coordinates);


        //Setting up animation
        orientatedAnimation = loadPlayerAnimation(spriteName);
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(orientation)]);

        keyIds = new ArrayList<>();

        isChangingRoom = false;
        onIce = false;

        // Setting up health and health bar
        hp = MAX_HEALTH;
        hpMeter = new TextGraphics(getHealthBar(hp), 0.25f, getHealthColor(hp));
        hpMeter.setFontName("OpenSans-Bold");
        hpMeter.setParent(this);
        hpMeter.setAnchor(new Vector(-0.07f, 1.2f));
    }


    /**
     * Configurate the different animations used in the player's movements
     * @param spriteName spriteName :  Name of the file that contains the player's animations
     * @return Animation[] : possible animations of the Player
     */
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

        boolean canMove = true;
        if(onIce && !isDisplacementOccurs()){
            canMove = !move(MOVE_DURATION/2);
        }
        if(canMove){
            moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }

        //If the player is still, reset the animation
        if(!isDisplacementOccurs())
            currentAnimation.reset();
        else
            currentAnimation.update(deltaTime);

        //If the player is trying to do magic and he has a staff, make it create firebolt
        if(keyboard.get(Keyboard.X).isDown() && ownStaff()){
            Fire fire = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
            fire.enterArea(getOwnerArea(), getCurrentMainCellCoordinates());
        }


        hpMeter.setText(getHealthBar(hp));
        hpMeter.setFillColor(getHealthColor(hp));
    }

    /**
     * return the String representing the Health bar of the player, depending of its current health
     * @param hp : the health of the player
     * @return String healthbar
     */
    private String getHealthBar(float hp) {
        // If the player health is negative, return an empty health bar
        if(hp < 0){
            return "▁▁▁▁▁";
        }

        //get the percentage of the player's health compared with MAX_HEALTH
        float percentage = hp*100f/MAX_HEALTH;
        String result = "";

        // for each 20% add a full health square
        while(percentage >= 20f){
            result += "█";
            percentage -= 20f;
        }
        //if the life is total just return
        if(result.length() == 5)
            return result;

        //add the character relative to the rest's percentage
        String possibilities = "▁▁▂▂▂▂▃▃▄▄▄▄▅▅▆▆▆▆▇▇";
        result += possibilities.charAt(Integer.min((int)percentage,19));
        while(result.length() != 5)
            result += "▁";

        return result;
    }

    /**
     * Return the color of the health bar depending on the player's hp
     * @param hp : the health of the player
     * @return Color: the color of the healthbar
     */
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

    /**
     * Make the player move in a certain direction if he's currently still and the button is pressed
     * @param orientation : the Orientation of the movement
     * @param b : The button associated with the movement
     */
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

    /**
     * Return the index of the movement animation associated with a given Orientation
     * @param orientation : the given orientation
     * @return int index
     */
    private int getAnimationIndexFromOrientation(Orientation orientation){
        return switch (orientation){
            case DOWN -> 0;
            case LEFT -> 1;
            case UP -> 2;
            case RIGHT -> 3;
        };
    }

    /**
     * update the player health while keeping it non-negative and below MAX_HEALTH
     * @param deltaHP : the amount of hp to add to the player health
     */
    public void updateHp(float deltaHP){
        hp += deltaHP;
        hp = Float.max(hp,0f);
        hp = Float.min(hp, MAX_HEALTH);
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

    /**
     * @return ownStaff
     */
    private boolean ownStaff() {
        return ownStaff;
    }

    /**
     * @return if the player is currently changing room
     */
    public boolean isChangingRoom() {
        return isChangingRoom;
    }

    /**
     * set the room the player is changing to
     * @param changingRoom the room the player is changing to
     */
    public void setChangingRoom(boolean changingRoom) {
        isChangingRoom = changingRoom;
    }

    @Override
    public void enterArea(Area area, DiscreteCoordinates position) {
        super.enterArea(area, position);
        ((ICRogueRoom)getOwnerArea()).setRoomVisited(true);
    }

    /**
     * @return the starting position in the next room the player is changing to
     */
    public DiscreteCoordinates getNextAreaStartingPos() {
        return nextAreaStartingPos;
    }

    /**
     * @return the room the player is changing to
     */
    public String getNextArea() {
        return nextArea;
    }

    /**
     * @return if the player is ded
     */
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
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction){
            if(isCellInteraction){
                if(cell.getCellType().equals(ICRogueBehavior.ICRogueCellType.ICE)){
                    onIce = true;
                }
                else{
                    onIce = false;
                }
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
                updateHp(4.0f);
            }
        }
    }
}
