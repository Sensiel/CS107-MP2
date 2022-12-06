package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0StaffRoom extends Level0ItemRoom{
    public Level0StaffRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        staff=new Staff(this, Orientation.UP, new DiscreteCoordinates(5,5));
        addItem(staff);
        // TODO maybe utiliser une constante pour la position de creation
    }
    private Staff staff;
    /*


    @Override
    public boolean isOn() {
        if(staff.isCollected()&&super.isOn()){
            return true;
        } else { return false; }
    }
    @Override
    public boolean isOff() {
        return (!isOn());
    }

     */ // jsp si on garde psk celui de la superclasse fait le taff en + c'est pas demande de coder ca ds l'énoncé

}
