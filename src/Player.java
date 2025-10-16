import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/** Player character that can move around and between rooms, defeat enemies, collect coins
 */
public class Player {
    private Point prevPosition;
    private Point position;
    private Image currImage;
    private double health;
    private double speed;
    private double coins = 0;
    private double keys = 0;
    private double weapon = 0;
    private ArrayList<Bullet> bulletArrayList;
    private boolean isStoreOpen;
    private boolean faceLeft = false;
    private static final Image RIGHT_IMAGE = new Image("res/player_right.png");
    private static final Image LEFT_IMAGE = new Image("res/player_left.png");
    private static final int NUM_COINS = 50;
    private static final int STANDARD = 0;
    private static final int ADVANCED = 1;



    /** Player constructor with only one parameter
     * @param position initialises player with a specific coordinate
     */
    public Player(Point position) {
        this.position = position;
        this.currImage = RIGHT_IMAGE;
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed"));
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth"));
        ShadowDungeon.chosenCharacter = "null";
        this.bulletArrayList = new ArrayList<>();
        isStoreOpen = false;

    }
    /** Player constructor with multiple parameters
     * This is used when we switch from initial player to marine or robot
     * @param position initialises player with a specific coordinate
     * @param image initialises a specific image
     * @param speed initialises the speed of a player
     * @param health sets the health of the player which is rendered on screen
     * @param coins sets the number of coins a player has which is rendered on screen
     * @param prevPosition sets the previousPosition of a player which is useful for collision logic
     * @param keys sets the number of keys a player has which is rendered on a screen
     */
    public Player(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys) {
        this.position = position;
        this.currImage = new Image(image);
        this.speed = speed;
        this.health = health;
        this.prevPosition = prevPosition;
        this.coins = coins;
        this.keys = keys;
        this.bulletArrayList = new ArrayList<>();
    }
    /** No-arg constructor of Player
     */
    public Player() {
    }
    /** The setter method of a health attribute
     * @param health Sets a specific health value
     */
    public void setHealth(double health) {
        this.health = health;
    }
    /** The getter method of the BulletArrayList attribute
     * @return ArrayList <Bullet> returns the bulletArrayList
     */
    public ArrayList<Bullet> getBulletArrayList() {
        return bulletArrayList;
    }
    /** The update method of player
     * useful for determining player movement and change of character
     * @param input inputs the mouse and keyboard logic to determine movement
     */
    public void update(Input input) {
        // check movement keys and mouse cursor
        double currX = position.x;
        double currY = position.y;
        if (input.isDown(Keys.A) && !isStoreOpen) {
            currX -= speed;
        }
        if (input.isDown(Keys.D) && !isStoreOpen) {
            currX += speed;
        }
        if (input.isDown(Keys.W) && !isStoreOpen) {
            currY -= speed;
        }
        if (input.isDown(Keys.S) && !isStoreOpen) {
            currY += speed;
        }
        // store purchasing logic
        if (isStoreOpen) {
            if (getCoins() >= NUM_COINS) {
                // health purchase
                if (input.wasPressed(Keys.E)) {
                    health += NUM_COINS;
                    coins -= NUM_COINS;
                }
                // weapons purchase
                if (input.wasPressed(Keys.L) && weapon <= 1 && getCoins() >= NUM_COINS) {
                    weapon += 1;
                    coins -= NUM_COINS;
                }
            }
        }
        // move the player with a mouse
        faceLeft = input.getMouseX() < currX;
        // update the player position accordingly and ensure it can't move past the game window
        Rectangle rect = currImage.getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth() && topLeft.y >= 0 && bottomRight.y <= Window.getHeight()) {
            move(currX, currY);
        }
        // opening store logic
        if (input.wasPressed(Keys.SPACE) && isStoreOpen == false && health > 0) {
            isStoreOpen = true;
        } else if (input.wasPressed(Keys.SPACE) && isStoreOpen == true) {
            isStoreOpen = false;
        }
        // change characters
        if ((ShadowDungeon.chosenCharacter.equals("robot") || ShadowDungeon.chosenCharacter.equals("marine")) && wasAnyMousePressed(input)) {
            Bullet newBullet = new Bullet(getPosition(), input);
            this.bulletArrayList.add(newBullet);
        }
        // bullet logic
        if (this.bulletArrayList.size() > 0) {
            for (int i = 0; i < bulletArrayList.size(); i ++) {
                if (!bulletArrayList.get(i).isPresent()) {
                    bulletArrayList.remove(bulletArrayList.get(i));
                } else {
                    bulletArrayList.get(i).update();
                }
            }

        }
    }
    /** checks if there is collisions between bullet and closed door
     * @param door inputs the specific door
     */
    public void checkDoorCollision(Door door) {
        for (Bullet bullet: bulletArrayList) {
            bullet.bulletDoorCollision(door);
        }
    }
    /** checks if any part of the mouse is clicked
     * @param input inputs mouse logic
     * @return boolean returns true or false if any part of the mouse is clicked
     */
    public boolean wasAnyMousePressed(Input input) {
        return input.wasPressed(MouseButtons.LEFT) ||  input.wasPressed(MouseButtons.MIDDLE) || input.wasPressed(MouseButtons.RIGHT);
    }
    /** tracks specific movement logic of player
     * @param x inputs the x coordinate of the position
     * @param y inputs the y coordinate of the position
     */
    public void move(double x, double y) {
        prevPosition = position;
        position = new Point(x, y);
    }
    /** draws the player and its stats
     */
    public void draw() {
        currImage = faceLeft ? LEFT_IMAGE : RIGHT_IMAGE; // NOTE: this is an example of using the ternary operator
        currImage.draw(position.x, position.y);
        UserInterface.drawStats(health, coins, keys, weapon);
    }
    /** increases the number of coins that a player has
     * @param coins inputs how many coins we are adding to player
     */
    public void earnCoins(double coins) {
        this.coins += coins;
    }
    /** This method decreases the health of a player
     * @param damage inputs how much health the player list
     */
    public void receiveDamage(double damage) {
        health -= damage;
        // player loses when health goes below or equals zero
        if (health <= 0) {
            ShadowDungeon.changeToGameOverRoom();
        }
    }
    /** This method sets the weapon damage to the player
     */
    public int giveDamage() {
        if (weapon == STANDARD) {
            return Integer.parseInt(ShadowDungeon.gameProps.getProperty("weaponStandardDamage"));
        } else if (weapon == ADVANCED) {
            return Integer.parseInt(ShadowDungeon.gameProps.getProperty("weaponAdvanceDamage"));
        } else {
            return Integer.parseInt(ShadowDungeon.gameProps.getProperty("weaponEliteDamage"));
        }
    }
    /** This is a getter method of the position attribute
     * @return Point returns the current coordinates of the player
     */
    public Point getPosition() {
        return position;
    }
    /** This is a getter method of the speed attribute
     * @return double returns the current speed of the player
     */
    public double getSpeed() {
        return speed;
    }
    /** This is a getter method of the health attribute
     * @return double returns the current health of the player
     */
    public double getHealth() {
        return health;
    }
    /** This is a getter method of the weapon attribute
     * @return double returns the current weapon level of player
     */
    public double getWeapon() {
        return weapon;
    }
    /** This is a getter method of the keys attribute
     * @return double returns the number of keys that a player has
     */
    public double getKeys() {
        return keys;
    }
    /** This is a getter method of the coins attribute
     * @return double returns the number of coins that a player has
     */
    public double getCoins() {
        return coins;
    }
    /** This is a getter method of the faceLeft attribute
     * @return boolean returns if the player is facing left or not
     */
    public boolean getFaceLeft() {return faceLeft;}
    /** This is a getter method of the image attribute
     * @return Image returns the Image of a player
     */
    public Image getCurrImage() {
        return currImage;
    }
    /** This is a getter method of the prevPosition attribute
     * @return Point returns the coordinates of the previousPosition
     *               of the player
     */
    public Point getPrevPosition() {
        return prevPosition;
    }
    /** This is a getter method of the position attribute
     * @return Point returns the coordinates of the current Position
     *               of the player
     */
    public Point getCurrPosition() { return position;}
    /** This is a setter method of the image attribute
     * @param image inputs the specific image
     */
    public void setImage(String image) {
        this.currImage = new Image(image);
    }
    /** This is a setter method of the keys attribute
     * @param numKeys inputs the changed number of keys
     */
    public void setKeys(double numKeys) {
        this.keys = numKeys;
    }
    /** This is a setter method of the position attribute
     * @param position inputs the changed position of a player
     */
    public void setPosition(Point position) {
        this.position = position;
    }
    /** This is a getter method of the isStoreOpen attribute
     * @return boolean returns if the store is open or not
     */
    public boolean isStoreOpen() {
        return isStoreOpen;
    }
    /** This is a setter method of the isStoreOpen attribute
     * @param storeOpen inputs if the store is open or not
     */
    public void setStoreOpen(boolean storeOpen) {
        isStoreOpen = storeOpen;
    }
}
/** Robot class which inherits from player
 */
class Robot extends Player {
    /** Robot constructor with multiple parameters
     * inherits from the Player superclass
     * This is used when we switch from initial player to marine or robot
     * @param position initialises player with a specific coordinate
     * @param image initialises a specific image
     * @param speed initialises the speed of a player
     * @param health sets the health of the player which is rendered on screen
     * @param coins sets the number of coins a player has which is rendered on screen
     * @param prevPosition sets the previousPosition of a player which is useful for collision logic
     * @param keys sets the number of keys a player has which is rendered on a screen
     */
    public Robot(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys) {
        super(position, image, speed, health, coins, prevPosition, keys);
    }
    /** Overriding of draw method from superclass
     */
    @Override
    public void draw() {
        if (getFaceLeft()) {
            setImage("res/robot_left.png");
        } else {
            setImage("res/robot_right.png");
        }
        getCurrImage().draw(getCurrPosition().x, getCurrPosition().y);
        UserInterface.drawStats(getHealth(), getCoins(), getKeys(), getWeapon());
    }

}
/** Marine class which inherits from player
 */
class Marine extends Player {
    /** Marine constructor with multiple parameters
     * inherits from the Player superclass
     * This is used when we switch from initial player to marine or robot
     * @param position initialises player with a specific coordinate
     * @param image initialises a specific image
     * @param speed initialises the speed of a player
     * @param health sets the health of the player which is rendered on screen
     * @param coins sets the number of coins a player has which is rendered on screen
     * @param prevPosition sets the previousPosition of a player which is useful for collision logic
     * @param keys sets the number of keys a player has which is rendered on a screen
     */
    public Marine(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys) {
        super(position, image, speed, health, coins, prevPosition, keys);
    }
    /** Overriding of draw method from superclass
     */
    @Override
    public void draw() {
        if (getFaceLeft()) {
            setImage("res/marine_left.png");
        } else {
            setImage("res/marine_right.png");
        }
        getCurrImage().draw(getCurrPosition().x, getCurrPosition().y);
        UserInterface.drawStats(getHealth(), getCoins(), getKeys(), getWeapon());
    }
}