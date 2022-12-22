package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.AngryBall;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0TurretRoom extends Level0EnemyRoom{
    /**
     * Default Level0TurretRoom constructor
     * @param roomCoordinates ( DiscreteCoordinates ) : the coordinates of the room
     */
    public Level0TurretRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        Turret turret1 = new Turret(this, Orientation.UP,new DiscreteCoordinates(1,8), new Orientation[]{Orientation.DOWN, Orientation.RIGHT});
        Turret turret2 = new Turret(this, Orientation.UP,new DiscreteCoordinates(8,1), new Orientation[]{Orientation.UP, Orientation.LEFT});
        AngryBall angryBall = new AngryBall(this,Orientation.DOWN,new DiscreteCoordinates(3,8));
        setEnemies(turret1,turret2,angryBall);

    }
}
