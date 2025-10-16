import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
/** This describes an abstract Room superclass
 */
abstract public class Room {
    private Player player;
    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution
    /** abstract method that initialises features in a specific room
     * @param gameProperties inputting specific gameProperties
     */
    abstract public void initEntities(Properties gameProperties);
    /** abstract method that updates the state of the room
     * @param input inputting keyboard logic
     */
    abstract public void update(Input input);
    /** This is a getter method of the player attribute
     * @return Player returns a player
     */
    public Player getPlayer() {
        return player;
    }
    /** This is a setter method of the player attribute
     * @param player inputs a player
     */
    public void setPlayer(Player player) {
        this.player = player;
        stopCurrentUpdateCall = false;
    }
    /** This is a getter method of the stopCurrentUpdateCall attribute
     * @return boolean returns either true or false
     */
    public boolean isStopCurrentUpdateCall() {
        return stopCurrentUpdateCall;
    }
    /** This is a setter method of the stopCurrentUpdateCall attribute
     * @param stopCurrentUpdateCall sets either true or false
     */
    public void setStopCurrentUpdateCall(boolean stopCurrentUpdateCall) {
        this.stopCurrentUpdateCall = stopCurrentUpdateCall;
    }
    /** This method sets the stopCurrentUpdateCall attribute to true
     */
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }
}
/** This describes the PrepRoom class which inherits from
 * the room superclass
 */
class PrepRoom extends Room  {
        private RestartArea restartArea;
        private Door door;
    /** This initialises the entities for the prepRoom class
     * @param gameProperties inputs specific game properties of prepRoom
     */
        public void initEntities(Properties gameProperties) {
            // find the configuration of game objects for this room
            for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
                String roomSuffix = String.format(".%s", ShadowDungeon.PREP_ROOM_NAME);
                if (entry.getKey().toString().contains(roomSuffix)) {
                    String objectType = entry.getKey().toString().substring(0, entry.getKey().toString().length() - roomSuffix.length());
                    String propertyValue = entry.getValue().toString();
                    switch (objectType) {
                        case "door":
                            String[] coordinates = propertyValue.split(",");
                            door = new Door(IOUtils.parseCoords(propertyValue), coordinates[2]);
                            break;
                        case "restartarea":
                            restartArea = new RestartArea(IOUtils.parseCoords(propertyValue));
                            break;
                        default:
                    }
                }
            }
        }
    /** This is an implementation of the abstract update method
     * which changes the state of the Room
     * @param input inputs keyboard logic
     */
        public void update(Input input) {
            UserInterface.drawStartMessages();
            // update and draw all game objects in this room
            door.update(getPlayer());
            door.draw();
            if (stopUpdatingEarlyIfNeeded()) {
                return;
            }
            // renders restartArea
            restartArea.update(input, getPlayer());
            restartArea.draw();
            if (getPlayer() != null) {
                getPlayer().update(input);
                getPlayer().draw();
            }
            if (input.wasPressed(Keys.R) && !findDoor().isUnlocked()) {
                findDoor().unlock(false);
                setPlayer(new Robot(getPlayer().getPosition(), "res/robot.png", getPlayer().getSpeed(), getPlayer().getHealth(), getPlayer().getCoins(), getPlayer().getPrevPosition(), getPlayer().getKeys()));
            }
            // changes character to robot
            if (input.wasPressed(Keys.R)) {
                ShadowDungeon.chosenCharacter = "robot";
                Player newPlayer = ShadowDungeon.changePlayer("robot");
                ShadowDungeon.setPlayer(newPlayer);
                setPlayer(newPlayer);
            }
            // changes character to marine
            if (input.wasPressed(Keys.M)) {
                ShadowDungeon.chosenCharacter = "marine";
                Player newPlayer = ShadowDungeon.changePlayer("marine");
                ShadowDungeon.setPlayer(newPlayer);
                setPlayer(newPlayer);
            }
        }
    /** This method sets the stopCurrentUpdateCall attribute
     * to true
     */
    public void stopCurrentUpdateCall() {
            setStopCurrentUpdateCall(true);
        }
    /** This method finds a door
     * @return Door returns a specific door
     */
        public Door findDoor() {
            return door;
        }
    /** This method finds a door by destination
     * @return Door returns a specific door
     */
        public Door findDoorByDestination() {
            return door;
        }
    /*
    Handles logic for updating early
    */
    private boolean stopUpdatingEarlyIfNeeded() {
        if (isStopCurrentUpdateCall()) {
             setPlayer(null);
            setStopCurrentUpdateCall(false);
            return true;
        }
        return false;
    }
}
/** This describes the EndRoom class which inherits from
 * the room superclass
 */
class EndRoom extends Room {
    private Door door;
    private RestartArea restartArea;
    private boolean isGameOver = false;
    /** This initialises the entities for the EndRoom class
     * @param gameProperties inputs specific game properties of endRoom
     */
    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", ShadowDungeon.END_ROOM_NAME);
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString().substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();
                switch (objectType) {
                    case "door":
                        String[] coordinates = propertyValue.split(",");
                        door = new Door(IOUtils.parseCoords(propertyValue), coordinates[2]);
                        break;
                    case "restartarea":
                        restartArea = new RestartArea(IOUtils.parseCoords(propertyValue));
                        break;
                    default:
                }
            }
        }
    }
    /** This is an implementation of the abstract update method
     * which changes the state of the Room
     * @param input inputs keyboard logic
     */
    public void update(Input input) {
        UserInterface.drawEndMessage(!isGameOver);
        // door should be locked if player got to this room by dying
        if (isGameOver) {
            findDoor().lock();
        }
        // update and draw all game objects in this room
        door.update(getPlayer());
        door.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }
        // renders restartArea
        restartArea.update(input, getPlayer());
        restartArea.draw();
        // renders player
        if (getPlayer() != null) {
            getPlayer().update(input);
            getPlayer().draw();
        }
    }
    /*
     Handles logic for updating early
     */
    private boolean stopUpdatingEarlyIfNeeded() {
        if (isStopCurrentUpdateCall()) {
            setPlayer(null);
            setStopCurrentUpdateCall(false);
            return true;
        }
        return false;
    }
    /** This method sets the stopCurrentUpdateCall attribute
     * to false
     */
    public void stopCurrentUpdateCall() {
        setStopCurrentUpdateCall(false);
    }
    /** This method finds a door
     * @return Door returns a specific door
     */
    public Door findDoor() {
        return door;
    }
    /** This method finds a door by destination
     * @return Door returns a specific door
     */
    public Door findDoorByDestination() {
        return door;
    }
    /** This method sets the attribute isGameOver to true
     * which represents when a player loses the game
     */
    public void isGameOver() {
        isGameOver = true;
    }

}
/** This describes the BattleRoom class which inherits from
 * the room superclass
 */
class BattleRoom extends Room {
    private Door primaryDoor;
    private Door secondaryDoor;
    private NewKeyBulletKin newKeyBulletKin;
    private ArrayList<BulletKin> bulletKinArrayList;
    private ArrayList<TreasureBox> treasureBoxes;
    private ArrayList <AshenBulletKin> ashenBulletKinArrayList;
    private ArrayList<Basket> baskets;
    private ArrayList<Table> tables;
    private ArrayList<Wall> walls;
    private ArrayList<River> rivers;
    private boolean isComplete = false;
    private final String nextRoomName;
    private final String roomName;
    /** This is a BattleRoom constructor
     * @param roomName inputs the specific BattleRoom name
     * @param nextRoomName inputs the next Room after a specific BattleRoom
     */
    public BattleRoom(String roomName, String nextRoomName) {
        walls = new ArrayList<>();
        rivers = new ArrayList<>();
        treasureBoxes = new ArrayList<>();
        bulletKinArrayList = new ArrayList<>();
        ashenBulletKinArrayList = new ArrayList<>();
        tables = new ArrayList<>();
        baskets = new ArrayList<>();
        this.roomName = roomName;
        this.nextRoomName = nextRoomName;
    }
    /** This initialises the entities for the BattleRoom class
     * renders all the associated objects inside BattleRoom class
     * @param gameProperties inputs specific game properties of endRoom
     */
    public void initEntities(Properties gameProperties) {
        // find the configuration of game objects for this room
        for (Map.Entry<Object, Object> entry : gameProperties.entrySet()) {
            String roomSuffix = String.format(".%s", roomName);
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();
                // ignore if the value is 0
                if (propertyValue.equals("0")) {
                    continue;
                }
                String[] coordinates;
                for (String coords : propertyValue.split(";")) {
                    switch (objectType) {
                        case "primarydoor":
                            coordinates = propertyValue.split(",");
                            primaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this);
                            break;
                        case "secondarydoor":
                            coordinates = propertyValue.split(",");
                            secondaryDoor = new Door(IOUtils.parseCoords(propertyValue), coordinates[2], this);
                            break;
                        case "keyBulletKin":
                            newKeyBulletKin = new NewKeyBulletKin(IOUtils.parseMultipleCoords(propertyValue));
                            break;
                        case "bulletKin":
                            for (Point point : IOUtils.parseMultipleCoords(coords)) {
                                bulletKinArrayList.add(new BulletKin(point));
                            }
                            break;
                        case "ashenBulletKin":
                            for (Point point : IOUtils.parseMultipleCoords(coords)) {
                                ashenBulletKinArrayList.add(new AshenBulletKin(point));
                            }
                            break;
                        case "wall":
                            Wall wall = new Wall(IOUtils.parseCoords(coords));
                            walls.add(wall);
                            break;
                        case "treasurebox":
                            TreasureBox treasureBox = new TreasureBox(IOUtils.parseCoords(coords),
                                    Double.parseDouble(coords.split(",")[2]));
                            treasureBoxes.add(treasureBox);
                            break;
                        case "river":
                            River river = new River(IOUtils.parseCoords(coords));
                            rivers.add(river);
                            break;
                        case "table":
                            Table table = new Table(IOUtils.parseCoords(coords));
                            tables.add(table);
                            break;
                        case "basket":
                            Basket basket = new Basket(IOUtils.parseCoords(coords));
                            baskets.add(basket);
                            break;
                        default:
                    }
                }
            }
        }
    }
    /** This is an implementation of the abstract update method
     * which changes the state of the Room
     * this helps render of the relevant attributes inside a BattleRoom class
     * @param input inputs keyboard logic
     */
        public void update(Input input) {
            // update and draw all active game objects in this room
            primaryDoor.update(getPlayer());
            primaryDoor.draw();
            if (isStopCurrentUpdateCall()) {
                return;
            }
            secondaryDoor.update(getPlayer());
            secondaryDoor.draw();
            if (isStopCurrentUpdateCall()) {
                return;
            }
            newKeyBulletKin.update(getPlayer());
            for (int i = 0; i < bulletKinArrayList.size(); i ++) {
                bulletKinArrayList.get(i).update(getPlayer());
                bulletKinArrayList.get(i).fireballDoorCollision(primaryDoor, secondaryDoor, bulletKinArrayList.get(i).getFireBallArrayList());
                if (bulletKinArrayList.get(i).isDead() && bulletKinArrayList.get(i).isActive()) {
                    // robot earns a little extra when it kills a bulletKin
                    if (ShadowDungeon.chosenCharacter.equals("robot")) {
                       getPlayer().earnCoins(Integer.parseInt(ShadowDungeon.gameProps.getProperty("bulletKinCoin")) + Integer.parseInt(ShadowDungeon.gameProps.getProperty("robotExtraCoin")));
                    } else if (ShadowDungeon.chosenCharacter.equals("marine")) {
                        getPlayer().earnCoins(Integer.parseInt(ShadowDungeon.gameProps.getProperty("bulletKinCoin")));
                    }
                    bulletKinArrayList.remove(bulletKinArrayList.get(i));
                }
            }
            for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
                ashenBulletKinArrayList.get(i).update(getPlayer());
                ashenBulletKinArrayList.get(i).fireballDoorCollision(primaryDoor, secondaryDoor, ashenBulletKinArrayList.get(i).getFireBallArrayList() );
                if (ashenBulletKinArrayList.get(i).isDead() && ashenBulletKinArrayList.get(i).isActive()) {
                    // robot earns a little extra when it kills an ashenBulletKin
                    if (ShadowDungeon.chosenCharacter.equals("robot")) {
                        getPlayer().earnCoins(Integer.parseInt(ShadowDungeon.gameProps.getProperty("ashenBulletKinCoin")) + Integer.parseInt(ShadowDungeon.gameProps.getProperty("robotExtraCoin")));
                    } else if (ShadowDungeon.chosenCharacter.equals("marine")) {
                        getPlayer().earnCoins(Integer.parseInt(ShadowDungeon.gameProps.getProperty("ashenBulletKinCoin")));
                    }
                    ashenBulletKinArrayList.remove(ashenBulletKinArrayList.get(i));
                }
            }
            for (Wall wall: walls) {
                wall.update(getPlayer());
                wall.draw();
                for (int i = 0; i < bulletKinArrayList.size(); i ++) {
                    bulletKinArrayList.get(i).collideWithWall(wall);
                }
                for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
                    ashenBulletKinArrayList.get(i).collideWithWall(wall);
                }
            }
            for (River river: rivers) {
                river.update(getPlayer());
                river.draw();
            }
            for (TreasureBox treasureBox: treasureBoxes) {
                if (treasureBox.isActive()) {
                    treasureBox.update(input, getPlayer());
                    treasureBox.draw();
                }
            }
            for (Table table: tables) {
                table.update();
                table.collideWithPlayer(getPlayer());
            }
            for (Basket basket: baskets) {
                basket.update();
                basket.collideWithPlayer(getPlayer());
            }
            if (getPlayer() != null) {
                getPlayer().update(input);
                getPlayer().draw();
                getPlayer().checkDoorCollision(primaryDoor);
                getPlayer().checkDoorCollision(secondaryDoor);
            }
            if (noMoreEnemies() && !isComplete()) {
                setComplete(true);
                unlockAllDoors();
            }
        }
    /** This method finds a door based on the room name
     * @param roomName inputs the specific BattleRoom name
     * @return Door returns a specific door
     */
    public Door findDoorByDestination(String roomName) {
        if (primaryDoor.toRoomName.equals(roomName)) {
            return primaryDoor;
        } else {
            return secondaryDoor;
        }
    }
    /*
    unlocks all doors
     */
    private void unlockAllDoors() {
        primaryDoor.unlock(false);
        secondaryDoor.unlock(false);
    }
    /** This is a getter method of the isComplete attribute
     * @return boolean returns either true or false
     */
    public boolean isComplete() {
        return isComplete;
    }
    /** This is a setter method of the isComplete attribute
     * @param complete inputs either true or false
     */
    public void setComplete(boolean complete) {
        isComplete = complete;
    }
    /** This method activates all enemies
     */
    public void activateEnemies() {
        newKeyBulletKin.setActive(true);
        for (int i = 0; i < bulletKinArrayList.size(); i ++) {
            bulletKinArrayList.get(i).setActive(true);
        }
        for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
            ashenBulletKinArrayList.get(i).setActive(true);
        }
    }
    /** This method activates all obstacles
     */
    public void activateObstacles() {
        for (int i = 0; i < tables.size(); i ++) {
            tables.get(i).setActive(true);
            baskets.get(i).setActive(true);
        }
    }
    /** Checks if all the enemies have been killed
     * @return boolean returns true if all enemies are dead, otherwise return false
     */
    public boolean noMoreEnemies() {
        for (AshenBulletKin ashenBulletKin: ashenBulletKinArrayList) {
            if (!ashenBulletKin.isDead()) {
                return false;
            }
        }
        for (BulletKin bulletKin: bulletKinArrayList) {
            if (!bulletKin.isDead()) {
                return false;
            }
        }
        if (!newKeyBulletKin.isDead()) {
            return false;
        }
        return true;
    }
}
