package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.window.Window;

public class ICRogueBehavior extends AreaBehavior{

    /**
     * An enum representing the different types a cell can have
     */
    public enum ICRogueCellType {
        NONE(0,false), // Should never been used except in the toType method
        GROUND(-16777216, true), // traversable
        WALL(-14112955, false), // non traversable
        HOLE(-65536, true),
        ICE(-12390401, true);

        //The type of the Cell, represented by an integer
        final int type;

        // Boolean telling if actors can be on this cell
        final boolean isWalkable;

        /**
         * Default ICRogueCellType constructor
         * @param type : the type of the cell
         * @param isWalkable : boolean telling if actors can be on this cell
         */
        ICRogueCellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }

        /**
         * @param type : the type of the cell ( represented by an integer )
         * @return CellType : the enum having this integer as type
         */
        private static ICRogueBehavior.ICRogueCellType toType(int type){
            for(ICRogueBehavior.ICRogueCellType ict : ICRogueBehavior.ICRogueCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;
        }
    }

    /**
     * Default ICRogueCellType constructor
     * @param window : the window in which ICRogueBehavior is
     * @param name : the name of the behavior file
     */
    public ICRogueBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICRogueBehavior.ICRogueCellType color = ICRogueBehavior.ICRogueCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICRogueBehavior.ICRogueCell(x,y,color));
            }
        }
    }


    public class ICRogueCell extends AreaBehavior.Cell {
        /// Type of the cell following the enum
        private final ICRogueBehavior.ICRogueCellType type;

        /**
         * Default ICRogueCell constructor
         * @param x : the x-coordinate of the cell
         * @param y : the y-coordinate of the cell
         * @param type : the type of the cell
         */
        private ICRogueCell(int x, int y, ICRogueBehavior.ICRogueCellType type){
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            //if the cell is not Walkable no one can enter
            if(!type.isWalkable)
                return false;
            //If entity does not take any space, it can always enter a walkable cell
            if(!entity.takeCellSpace()) {
                return true;
            }
            //If entity does take space, we have to check if there's already an entity taking the cell space already in the cell
            for(Interactable currEntity : entities) {
                if (currEntity.takeCellSpace()) {
                    return false;
                }
            }

            return true;
        }


        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler) v).interactWith(this, isCellInteraction);
        }

        /**
         * @return the CellType of the cell
         */
        public ICRogueCellType getCellType(){
            return type;
        }
    }
}





