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
    private Orientation[] orientations;
    private final static float COOLDOWN = 2.f;
    private float currentCooldown;


    public Turret(Area owner, Orientation orientation, DiscreteCoordinates coordinates, Orientation[] orientations) {
        super(owner, orientation, coordinates);
        setSprite(new Sprite("icrogue/static_npc", 1.5f, 1.5f, this, null, new Vector(-0.25f, 0)));
        this.orientations = orientations;
        currentCooldown = 0f;
    }

    private void resetCooldown() {
        currentCooldown = 0f;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!getIsDead()) {
            getSprite().draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor c, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) c).interactWith(this, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentCooldown += deltaTime;

        if (currentCooldown >= COOLDOWN) {
            attack();
            resetCooldown();
        }
    }

    public void attack() {
        for (Orientation orientation : orientations) {
            Arrow arrow = new Arrow(getOwnerArea(), orientation, getCurrentMainCellCoordinates());
            arrow.enterArea(getOwnerArea(), getCurrentMainCellCoordinates());
        }
    }
}
