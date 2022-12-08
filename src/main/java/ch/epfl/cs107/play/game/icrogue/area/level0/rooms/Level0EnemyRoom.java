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
    }

    protected void setEnemies(Enemy... listOfEnemies) {
        for(Enemy enemy : listOfEnemies){
            enemies.add(enemy);
        }
    }

    public boolean isOn() {
        if(!super.isOn()){
            return false;
        }
        for(Enemy enemy : enemies){
            if(!enemy.getIsDead()){
                return false;
            }
        }
        return true;
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
        ArrayList<Enemy> toRemove = new ArrayList<>();
        for(Enemy enemy : enemies){
            if(enemy.getIsDead()){
                enemy.leaveArea();
                toRemove.add(enemy);
            }
        }
        for(Enemy enemy: toRemove){
            enemies.remove(enemy);
        }
    }
}
