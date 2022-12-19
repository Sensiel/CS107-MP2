package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level0EnemyRoom extends Level0Room{
    private final List<Enemy> enemies;

    public Level0EnemyRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        enemies = new ArrayList<>();
    }

    protected void setEnemies(Enemy... listOfEnemies) {
        enemies.addAll(Arrays.stream(listOfEnemies).toList());
    }

    public boolean isOn() {
        if(!super.isOn()){
            return false;
        }
        for(Enemy enemy : enemies){
            if(!enemy.isDead()){
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
            enemy.enterArea(this, enemy.getCurrentCells().get(0)); // ne marche pas si il occupe plusieurs cell
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        List<Enemy> toRemove = new ArrayList<>();

        for(Enemy enemy : enemies){
            if(enemy.isDead()){
                toRemove.add(enemy);
            }
        }
        for(Enemy enemy: toRemove){
            enemies.remove(enemy);
        }
    }
}
