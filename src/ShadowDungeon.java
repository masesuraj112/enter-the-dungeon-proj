import bagel.*;
import bagel.util.Point;
import java.util.Properties;

/** Main game class that manages initialising the rooms and moving the player between rooms
 */
public class ShadowDungeon extends AbstractGame {
    /** attributes that correspond with the app.properties file
     */
    public static Properties gameProps;
    /** attributes that correspond with the message.properties file
     */
    public static Properties messageProps;
    /** the width of the game screen
     */
    public static double screenWidth;
    /** the height of the game string
     */
    public static double screenHeight;
    private static String currRoomName;
    private static PrepRoom prepRoom;
    private static BattleRoom battleRoomA;
    private static BattleRoom battleRoomB;
    private static EndRoom endRoom;
    private static Player player;
    private final Image background;
    /** prepRoom constant
     */
    public static final String PREP_ROOM_NAME = "prep";
    /** BATTLE_ROOM_A constant
     */
    public static final String BATTLE_ROOM_A_NAME = "A";
    /** BATTLE_ROOM_B constant
     */
    public static final String BATTLE_ROOM_B_NAME = "B";
    /** endRoom constant
     */
    public static final String END_ROOM_NAME = "end";
    /** identifies what character a player is
     */
    public static String chosenCharacter = "none";
    /** counts the number of frames
     */
    public static int frameCounter;
    /** This is the ShadowDungeon constructor
     * @param gameProps inputs the game properties
     * @param messageProps inputs the message properties
     */
    public ShadowDungeon(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Dungeon");

        ShadowDungeon.gameProps = gameProps;
        ShadowDungeon.messageProps = messageProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));
        this.background = new Image("res/background.png");
        frameCounter = 0;
        resetGameState(gameProps);
    }
    /** This is resets the state of the game
     * @param gameProps inputs the game properties
     */
    public static void resetGameState(Properties gameProps) {
        prepRoom = new PrepRoom();
        battleRoomA = new BattleRoom(BATTLE_ROOM_A_NAME, BATTLE_ROOM_B_NAME);
        battleRoomB = new BattleRoom(BATTLE_ROOM_B_NAME, END_ROOM_NAME);
        endRoom = new EndRoom();
        prepRoom.initEntities(gameProps);
        battleRoomA.initEntities(gameProps);
        battleRoomB.initEntities(gameProps);
        endRoom.initEntities(gameProps);
        currRoomName = PREP_ROOM_NAME;
        ShadowDungeon.player = new Player(IOUtils.parseCoords(gameProps.getProperty("player.start")));
        chosenCharacter = "none";
        prepRoom.setPlayer(player);
        frameCounter = 0;
    }

    /** Render the relevant screen based on the keyboard input given by the user and the status of the gameplay.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        frameCounter += 1;
        background.draw((double) Window.getWidth() / 2, (double) Window.getHeight() / 2);
        switch (currRoomName) {
            case PREP_ROOM_NAME:
                prepRoom.update(input);
                Store.operateStore(input);
                return;
            case BATTLE_ROOM_A_NAME:
                battleRoomA.update(input);
                Store.operateStore(input);
                return;
            case BATTLE_ROOM_B_NAME:
                battleRoomB.update(input);
                Store.operateStore(input);
                return;
            default:
                endRoom.update(input);
                Store.operateStore(input);
        }
    }
    /** Changes the character of the player
     * @param chosenCharacter a string of the specific character we want to change to
     * @return Player return a player
     */
    public static Player changePlayer(String chosenCharacter) {
        // change player to marine
        if (chosenCharacter.equals("marine")) {
            return new Marine(player.getPosition(), "res/marine.png", player.getSpeed(), player.getHealth(), player.getCoins(), player.getPrevPosition(), player.getKeys());
            // change player to robot
        } else if (chosenCharacter.equals("robot")) {
            return new Robot(player.getPosition(), "res/marine.png", player.getSpeed(), player.getHealth(), player.getCoins(), player.getPrevPosition(), player.getKeys());
        }
        return player;
    }
    /** Manages changing of rooms
     * @param roomName specifies which room we are dealing with
     */
    public static void changeRoom(String roomName) {
        Door nextDoor;
        switch (roomName) {
            case PREP_ROOM_NAME:
                nextDoor = prepRoom.findDoorByDestination();
                // assume that prep room can only be entered through Battle Room A
                if (currRoomName.equals(BATTLE_ROOM_A_NAME)) {
                    battleRoomA.stopCurrentUpdateCall();
                }
                currRoomName = PREP_ROOM_NAME;
                // move the player to the center of the next room's door
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                prepRoom.setPlayer(player);
                return;
            case BATTLE_ROOM_A_NAME:
                nextDoor = battleRoomA.findDoorByDestination(currRoomName);
                // assume that Battle Room A can only be entered through Prep Room or Battle Room B
                if (currRoomName.equals(BATTLE_ROOM_B_NAME)) {
                    battleRoomB.stopCurrentUpdateCall();
                } else if (currRoomName.equals(PREP_ROOM_NAME)) {
                    prepRoom.stopCurrentUpdateCall();
                }
                currRoomName = BATTLE_ROOM_A_NAME;
                // prepare the door to be able to activate the Battle Room
                if (!battleRoomA.isComplete()) {
                    nextDoor.setShouldLockAgain();
                }
                // move the player to the center of the next room's door
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                battleRoomA.setPlayer(player);
                return;
            case BATTLE_ROOM_B_NAME:
                nextDoor = battleRoomB.findDoorByDestination(currRoomName);
                // assume that Battle Room B can only be entered through Battle Room A or End Room
                if (currRoomName.equals(BATTLE_ROOM_A_NAME)) {
                    battleRoomA.stopCurrentUpdateCall();
                } else if (currRoomName.equals(END_ROOM_NAME)) {
                    endRoom.stopCurrentUpdateCall();
                }
                currRoomName = BATTLE_ROOM_B_NAME;
                // prepare the door to be able to activate the Battle Room
                if (!battleRoomB.isComplete()) {
                    nextDoor.setShouldLockAgain();
                }
                // move the player to the center of the next room's door
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                battleRoomB.setPlayer(player);
                return;
            default:
                nextDoor = endRoom.findDoorByDestination();
                // assume that end room can only be entered through Battle Room B
                if (currRoomName.equals(BATTLE_ROOM_B_NAME)) {
                    battleRoomB.stopCurrentUpdateCall();
                }
                currRoomName = END_ROOM_NAME;
                // move the player to the center of the next room's door
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                endRoom.setPlayer(player);
        }
    }

    /** Method that operates when a player loses the game
     */
    public static void changeToGameOverRoom() {
        switch (currRoomName) {
            case PREP_ROOM_NAME:
                prepRoom.stopCurrentUpdateCall();
            case BATTLE_ROOM_A_NAME:
                battleRoomA.stopCurrentUpdateCall();
            case BATTLE_ROOM_B_NAME:
                battleRoomB.stopCurrentUpdateCall();
            default:
        }
        endRoom.isGameOver();
        currRoomName = END_ROOM_NAME;
        Point startPos = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("player.start"));
        player.move(startPos.x, startPos.y);
        endRoom.setPlayer(player);
    }
    /** Setter method of player attribute
     * @param player inputs a specific player
     */
    public static void setPlayer(Player player) {
        ShadowDungeon.player = player;
    }
    /** Getter method of player attribute
     * @return Player outputs a specific player
     */
    public static Player getPlayer() {
        return player;
    }
    /** Getter method of game properties
     * @return Properties returns specific game properties
     */
    public static Properties getGameProps() {
        return gameProps;
    }
    /** Getter method of message properties
     * @return Properties returns specific message properties
     */
    public static Properties getMessageProps() {
        return messageProps;
    }
    /** Main method that handles the running of the game
     * @param args inputs command line arguments
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDungeon game = new ShadowDungeon(gameProps, messageProps);
        game.run();
    }
}
