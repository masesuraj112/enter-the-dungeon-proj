import bagel.Input;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import bagel.util.Point;

/**
 * Room with doors that are locked until the player defeats all enemies
 */
public class BattleRoom {
    private Player player;
    private Door primaryDoor;
    private Door secondaryDoor;
//    private KeyBulletKin keyBulletKin;
    private NewKeyBulletKin newKeyBulletKin;
    private ArrayList <BulletKin> bulletKinArrayList;
    private ArrayList<TreasureBox> treasureBoxes;
    private ArrayList <AshenBulletKin> ashenBulletKinArrayList;
    private ArrayList<Basket> baskets;
    private ArrayList<Table> tables;
    private ArrayList<Wall> walls;
    private ArrayList<River> rivers;
    private boolean stopCurrentUpdateCall = false; // this determines whether to prematurely stop the update execution
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
        for (Map.Entry<Object, Object> entry: gameProperties.entrySet()) {
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
                for (String coords: propertyValue.split(";")) {
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
                            for (Point point: IOUtils.parseMultipleCoords(coords)) {
                                bulletKinArrayList.add(new BulletKin(point));
                            }
                            break;
                        case "ashenBulletKin":
                            for (Point point: IOUtils.parseMultipleCoords(coords)) {
                                System.out.println("inside ashen");
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



                        default:
                    }
                }
            }
        }
    }

    public void update(Input input) {
        // update and draw all active game objects in this room

        primaryDoor.update(player);
        primaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

        secondaryDoor.update(player);
        secondaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) {
            return;
        }

//        if (keyBulletKin.isActive()) {
//            keyBulletKin.update(player);
//            keyBulletKin.draw();
//        }


        newKeyBulletKin.update(player);



        for (int i = 0; i < bulletKinArrayList.size(); i ++) {
            bulletKinArrayList.get(i).update(player);

            bulletKinArrayList.get(i).fireballDoorCollision(primaryDoor, secondaryDoor, bulletKinArrayList.get(i).getFireBallArrayList());



            if (bulletKinArrayList.get(i).isDead() && bulletKinArrayList.get(i).isActive()) {
                if (ShadowDungeon.chosenCharacter.equals("robot")) {
                    player.earnCoins(15);
                } else if (ShadowDungeon.chosenCharacter.equals("marine")) {
                    player.earnCoins(10);

                }
                bulletKinArrayList.remove(bulletKinArrayList.get(i));
            }

        }

        for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
            ashenBulletKinArrayList.get(i).update(player);
            ashenBulletKinArrayList.get(i).fireballDoorCollision(primaryDoor, secondaryDoor, ashenBulletKinArrayList.get(i).getFireBallArrayList() );
            if (ashenBulletKinArrayList.get(i).isDead() && ashenBulletKinArrayList.get(i).isActive()) {
                if (ShadowDungeon.chosenCharacter.equals("robot")) {
                    player.earnCoins(25);
                } else if (ShadowDungeon.chosenCharacter.equals("marine")) {
                    player.earnCoins(20);

                }
                ashenBulletKinArrayList.remove(ashenBulletKinArrayList.get(i));

            }
        }



        for (Wall wall: walls) {
            wall.update(player);
            wall.draw();
            for (int i = 0; i < bulletKinArrayList.size(); i ++) {
                bulletKinArrayList.get(i).collideWithWall(wall);
            }
            for (int i = 0; i < ashenBulletKinArrayList.size(); i ++) {
                ashenBulletKinArrayList.get(i).collideWithWall(wall);
            }



        }

        for (River river: rivers) {
            river.update(player);
            river.draw();
        }

        for (TreasureBox treasureBox: treasureBoxes) {
            if (treasureBox.isActive()) {
                treasureBox.update(input, player);
                treasureBox.draw();
            }
        }

        for (Table table: tables) {
            table.update();
            table.collideWithPlayer(player);
        }

        if (player != null) {
            player.update(input);
            player.draw();
            player.checkDoorCollision(primaryDoor);
            player.checkDoorCollision(secondaryDoor);

        }




        if (noMoreEnemies() && !isComplete()) {
            setComplete(true);
            unlockAllDoors();
        }
    }

    private boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            player = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }

    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }

    public void setPlayer(Player player) {
        this.player = player;
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


//        return keyBulletKin.isDead();
    }
}
