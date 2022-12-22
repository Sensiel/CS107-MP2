package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class AngryBall extends Enemy  {
    //AngryBall Damage amount
    public final static float ANGRYBALL_DAMAGE =0.5f;

    //Time needed to move from a Wall to another
    private final static float TIME_NEEDED = 3f;

    // Counter used at AngryBall::update
    private static float counter;

    //Movement time

    private final static int MOVE_DURATION = 8;

    //Duration of the motion animation

    private final static int ANIMATION_DURATION = MOVE_DURATION / 2;

    //Array of different motion animations depending on the orientation
    private Animation[] orientatedAnimation;

    //Current Animation
    private Animation currentAnimation;

    public AngryBall(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        Sprite[] spritesDOWN = new Sprite[4], spritesLEFT = new Sprite[4], spritesUP = new Sprite[4], spritesRIGHT = new Sprite[4];
        Vector anchor = new Vector(.15f, -.15f);
        for (int iFrame = 0; iFrame < 4; ++iFrame) {
            spritesDOWN[iFrame] = new Sprite("max.old.surf", .90f, 1.f, this, new RegionOfInterest(0, iFrame * 21, 16, 21), anchor);
            spritesLEFT[iFrame] = new Sprite("max.old.surf", .90f, 1.f, this, new RegionOfInterest(16, iFrame * 21, 16, 21), anchor);
            spritesUP[iFrame] = new Sprite("max.old.surf", .90f, 1.f, this, new RegionOfInterest(32, iFrame * 21, 16, 21), anchor);
            spritesRIGHT[iFrame] = new Sprite("max.old.surf", .90f, 1.f, this, new RegionOfInterest(48, iFrame * 21, 16, 21), anchor);
        }
        Animation animationsUP, animationsDOWN, animationsLEFT, animationsRIGHT;
        animationsDOWN = new Animation(ANIMATION_DURATION, spritesDOWN);
        animationsLEFT = new Animation(ANIMATION_DURATION, spritesLEFT);
        animationsUP = new Animation(ANIMATION_DURATION, spritesUP);
        animationsRIGHT = new Animation(ANIMATION_DURATION, spritesRIGHT);
        orientatedAnimation = new Animation[]{animationsDOWN, animationsLEFT, animationsUP, animationsRIGHT};
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(orientation)]);

    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);}

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        counter += deltaTime;
        if (counter <= TIME_NEEDED) {
            move(Orientation.DOWN);
        } else {
            move(Orientation.UP);
        }
        if (counter > 2*TIME_NEEDED) {
            counter = 0f;
        }
    }

    /**
     * Makes the AngryBall move in the given Orientation whitout needing to press any button
     * @param orientation : : the given orientation
     */
    private void move(Orientation orientation) {
        setCurrentAnimation(orientatedAnimation[getAnimationIndexFromOrientation(getOrientation())]);
            if (!isDisplacementOccurs()) {
                if (!orientation.equals(getOrientation()))
                    currentAnimation.reset();
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    /**
     * Return the index of the movement animation associated with a given Orientation
     * @param orientation : the given orientation
     * @return int index
     */
    private int getAnimationIndexFromOrientation(Orientation orientation) {
        return switch (orientation) {
            case DOWN -> 0;
            case LEFT -> 1;
            case UP -> 2;
            case RIGHT -> 3;
        };
    }

    /**
     * set the current Animation of the Angry Ball
     * @param a : the animation that the Angry Ball is switching to
     */
    private void setCurrentAnimation(Animation a) {
        currentAnimation = a;
    }


    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }


    @Override
    public void acceptInteraction(AreaInteractionVisitor c, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) c).interactWith(this, isCellInteraction);
    }

}



