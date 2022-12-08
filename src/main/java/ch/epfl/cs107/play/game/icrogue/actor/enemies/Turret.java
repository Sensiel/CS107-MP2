package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Turret extends Enemy {
    private Turret.ICRogueTurretInteractionHandler handler = new Turret.ICRogueTurretInteractionHandler();
    private Orientation[] orientations;
    private final static float COOLDOWN = 2.f;
    private int compteur; //jsp quoi mettre comme nom


    public Turret(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Orientation[] orientations) {
        super(owner, orientation, coordinates);
        setSprite(new Sprite("icrogue/static_npc", 1.5f, 1.5f, this, null, new Vector(-0.25f, 0)));
        this.orientations = orientations;
        compteur = 0;
    }

    private void initializeCompteur() {
        compteur = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!getIsDead()) {
            getSprite().draw(canvas);

        }
    }

    @Override
    public void killEnemy() {
        super.killEnemy();
        leaveArea(); // je pense c pas necessaire vu que jai mis la condition dans draw ?
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor c, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) c).interactWith(this, isCellInteraction);
    }

    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        compteur += deltaTime;
        if (compteur == COOLDOWN) {
            attack();
            initializeCompteur();
        }
    }

    public void attack() {
        /*
        for (Orientation orientation : orientations) {
            Arrow arrow = new Arrow(getOwnerArea(), orientation, getCurrentMainCellCoordinates());
            getOwnerArea().registerActor(arrow);

        } // idk

         */
        Arrow arrow1 = new Arrow(getOwnerArea(), orientations[0], getCurrentMainCellCoordinates());
        Arrow arrow2 = new Arrow(getOwnerArea(), orientations[1], getCurrentMainCellCoordinates());
        getOwnerArea().registerActor(arrow2);
        getOwnerArea().registerActor(arrow1);
    }

    private class ICRogueTurretInteractionHandler implements ICRogueInteractionHandler {
        @Override
        public void interactWith(Fire fire, boolean isCellInteraction) {
            if (isCellInteraction) {
                killEnemy();
            }
        }
    }
}
