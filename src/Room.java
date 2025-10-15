import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

abstract public class Room {
    abstract public void initEntities(Properties gameProperties);
    abstract public void update(Input input);
    private Player player;

    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        stopCurrentUpdateCall = false;
    }

    public boolean isStopCurrentUpdateCall() {
        return stopCurrentUpdateCall;
    }

    public void setStopCurrentUpdateCall(boolean stopCurrentUpdateCall) {
        this.stopCurrentUpdateCall = stopCurrentUpdateCall;
    }
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }
}


class PrepRoom extends Room  {
        private RestartArea restartArea;
        private Door door;



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

        public void update(Input input) {
            UserInterface.drawStartMessages();

            // update and draw all game objects in this room
            door.update(getPlayer());
            door.draw();
            if (stopUpdatingEarlyIfNeeded()) {
                return;
            }

            restartArea.update(input, getPlayer());
            restartArea.draw();

            if (getPlayer() != null) {
                getPlayer().update(input);
                getPlayer().draw();
            }


            if (input.wasPressed(Keys.R) && !findDoor().isUnlocked()) {
                findDoor().unlock(false);
    //            Player(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys)
                setPlayer(new Robot(getPlayer().getPosition(), "res/robot.png", getPlayer().getSpeed(), getPlayer().getHealth(), getPlayer().getCoins(), getPlayer().getPrevPosition(), getPlayer().getKeys()));

            }
            if (input.wasPressed(Keys.R)) {
                ShadowDungeon.chosenCharacter = "robot";
                Player newPlayer = ShadowDungeon.changePlayer("robot");
                ShadowDungeon.setPlayer(newPlayer);
                setPlayer(newPlayer);
    //            player = new Robot(player.getPosition(), "res/robot.png", player.getSpeed(), player.getHealth(), player.getCoins(), player.getPrevPosition(), player.getKeys());

            }

            if (input.wasPressed(Keys.M)) {
                ShadowDungeon.chosenCharacter = "marine";
                Player newPlayer = ShadowDungeon.changePlayer("marine");
                ShadowDungeon.setPlayer(newPlayer);
                setPlayer(newPlayer);




    //            player = new Marine(player.getPosition(), "res/marine.png", player.getSpeed(), player.getHealth(), player.getCoins(), player.getPrevPosition(), player.getKeys());
            }


        }

    public void stopCurrentUpdateCall() {
//            stopCurrentUpdateCall = true;
            setStopCurrentUpdateCall(true);
        }

        public Door findDoor() {
            return door;
        }

        public Door findDoorByDestination() {
            return door;
        }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (isStopCurrentUpdateCall()) {
             setPlayer(null);
            setStopCurrentUpdateCall(false);
            return true;
        }
        return false;
    }



}

class EndRoom extends Room {
    private Door door;
    private RestartArea restartArea;
    private boolean isGameOver = false;

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

        restartArea.update(input, getPlayer());
        restartArea.draw();

        if (getPlayer() != null) {
            getPlayer().update(input);
            getPlayer().draw();
        }
    }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (isStopCurrentUpdateCall()) {
//            player = null;
            setPlayer(null);
            setStopCurrentUpdateCall(false);
//            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }


    public void stopCurrentUpdateCall() {
        setStopCurrentUpdateCall(false);
    }

    public Door findDoor() {
        return door;
    }

    public Door findDoorByDestination() {
        return door;
    }

    public void isGameOver() {
        isGameOver = true;
    }

}

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
                            // remove later
//                            keyBulletKin = new KeyBulletKin(IOUtils.parseInitialMultipleCoords(propertyValue));
                            newKeyBulletKin = new NewKeyBulletKin(IOUtils.parseMultipleCoords(propertyValue));
//                            newKeyBulletKin.update(IOUtils.parseMultipleCoords(propertyValue));

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

//        if (keyBulletKin.isActive()) {
//            keyBulletKin.update(player);
//            keyBulletKin.draw();
//        }


            newKeyBulletKin.update(getPlayer());



            for (int i = 0; i < bulletKinArrayList.size(); i ++) {
                bulletKinArrayList.get(i).update(getPlayer());

                bulletKinArrayList.get(i).fireballDoorCollision(primaryDoor, secondaryDoor, bulletKinArrayList.get(i).getFireBallArrayList());



                if (bulletKinArrayList.get(i).isDead() && bulletKinArrayList.get(i).isActive()) {
                    if (ShadowDungeon.chosenCharacter.equals("robot")) {
                       getPlayer().earnCoins(15);
                    } else if (ShadowDungeon.chosenCharacter.equals("marine")) {
                        getPlayer().earnCoins(10);

                    }
                    bulletKinArrayList.remove(bulletKinArrayList.get(i));
                }

            }

            for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
                ashenBulletKinArrayList.get(i).update(getPlayer());
                ashenBulletKinArrayList.get(i).fireballDoorCollision(primaryDoor, secondaryDoor, ashenBulletKinArrayList.get(i).getFireBallArrayList() );
                if (ashenBulletKinArrayList.get(i).isDead() && ashenBulletKinArrayList.get(i).isActive()) {
                    if (ShadowDungeon.chosenCharacter.equals("robot")) {
                        getPlayer().earnCoins(25);
                    } else if (ShadowDungeon.chosenCharacter.equals("marine")) {
                        getPlayer().earnCoins(20);

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

    public Door findDoorByDestination(String roomName) {
        if (primaryDoor.toRoomName.equals(roomName)) {
            return primaryDoor;
        } else {
            return secondaryDoor;
        }
    }

    private void unlockAllDoors() {
        primaryDoor.unlock(false);
        secondaryDoor.unlock(false);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public void activateEnemies() {
//        keyBulletKin.setActive(true);
        newKeyBulletKin.setActive(true);
        for (int i = 0; i < bulletKinArrayList.size(); i ++) {
            bulletKinArrayList.get(i).setActive(true);
        }
        for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
            ashenBulletKinArrayList.get(i).setActive(true);
        }
    }

    public void activateObstacles() {
        for (int i = 0; i < tables.size(); i ++) {
            tables.get(i).setActive(true);
            baskets.get(i).setActive(true);
        }
    }

    // include all enemies

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
