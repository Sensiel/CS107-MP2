package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;

public class Level0EnemyRoom extends Level0Room{
    private ArrayList<Enemy> enemies;

    public Level0EnemyRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        enemies = new ArrayList<>();
        Turret turret1 = new Turret(this, Orientation.UP,new DiscreteCoordinates(1,8), new Orientation[]{Orientation.DOWN, Orientation.RIGHT});
        Turret turret2 = new Turret(this, Orientation.UP,new DiscreteCoordinates(8,1), new Orientation[]{Orientation.UP, Orientation.LEFT});
        setEnemies(turret1,turret2);
    }

    protected void setEnemies(Enemy... listOfEnemies) {
        for(Enemy enemy : listOfEnemies){
            enemies.add(enemy);
        }
    }

    public boolean isOn() {
        return enemies.isEmpty();
    }

    @Override
    public boolean isOff() {
        return (!isOn());
    }

    @Override
    protected void createArea() {
        super.createArea();
        for( Enemy enemy : enemies){
            registerActor(enemy);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for( Enemy enemy : enemies){
            if(enemy.getIsDead()){
                unregisterActor(enemy);
            }
        }
    }
}
