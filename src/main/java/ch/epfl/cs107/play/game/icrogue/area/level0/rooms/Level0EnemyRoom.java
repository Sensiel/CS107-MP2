package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level0EnemyRoom extends Level0Room{
    //list of the enemies presents in the room
    private final List<Enemy> enemies;

    /**
     * Default Level0EnemyRoom constructor
     * @param roomCoordinates ( DiscreteCoordinates ) : the coordinates of the room
     */
    protected Level0EnemyRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        enemies = new ArrayList<>();
    }

    /**
     * Set the room's enemies
     * @param listOfEnemies ( Enemy[] ) : the list of enemies that need to be added to the room
     */
    protected void setEnemies(Enemy... listOfEnemies) {
        enemies.addAll(Arrays.stream(listOfEnemies).toList());
    }

    @Override
    public boolean isOn() {
        // if the generic criteria are not resolved
        if(!super.isOn()){
            return false;
        }

        for(Enemy enemy : enemies){
            if(!enemy.isDead()){
                return false;
            }
        }

        // if the enemies are all dead ( or the list is empty )
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
            enemy.enterArea(this, enemy.getCurrentCells().get(0)); // doesn't work if the enemy is taking multiple cells
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        List<Enemy> toRemove = new ArrayList<>();

        // remove the dead enemies from the list

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
