package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {
    private DiscreteCoordinates roomCoordinates;
    private String behaviorName;
    private ICRogueBehavior behavior;
    private ArrayList<Connector> tab;

    public ICRogueRoom(List<DiscreteCoordinates> connectorsCoordinates, List<Orientation> orientations,
                       String behaviorName, DiscreteCoordinates roomCoordinates){
        tab= new ArrayList<Connector>();
        for(int i =0; i<connectorsCoordinates.size();++i){
            tab.add(i,new Connector(this,orientations.get(i),connectorsCoordinates.get(i)));
        }
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;

    }



    public String getBehaviorName(){return behaviorName;}

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, getBehaviorName());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    protected void createArea() {
        for (int i = 0; i < tab.size(); ++i) {
            registerActor(tab.get(i));
        }
    }
    public final float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }

    public DiscreteCoordinates getRoomCoordinates() {
        return roomCoordinates;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Keyboard keyboard = this.getKeyboard();

        if (keyboard.get(Keyboard.O).isDown()){

            for(int i =0;i<tab.size();++i){
                tab.get(i).setState(Connector.State.OPEN);
            }

        } else if(keyboard.get(Keyboard.L).isDown()){
            tab.get(0).setState(Connector.State.LOCKED);


        } else if (keyboard.get(Keyboard.T).isDown()) {

            for(int i =0;i<tab.size();++i){

                if(tab.get(i).getState().equals(Connector.State.CLOSED)){
                    tab.get(i).setState(Connector.State.OPEN);

                } else if (tab.get(i).getState().equals(Connector.State.OPEN)) {
                    tab.get(i).setState(Connector.State.CLOSED);
                }
            }
            
        }


    }
}

