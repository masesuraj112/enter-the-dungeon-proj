import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;
import bagel.util.Vector2;
/** This represents the Enemy abstract superclass
 * which is responsible for engaging with the player
 */
abstract public class Enemy {
    private double health;
    private Image enemyImage;
    private boolean active = false; // only true when the Battle Room has been activated
    private boolean dead = false;
    private Point position;
    private boolean isStoreOpen;
    /** This method draws the enemy
     */
    public void draw() {
        enemyImage.draw(position.x, position.y);
    }
    /** This is a getter of the dead attribute
     * @return boolean return if the enemy has been
     *                killed by the player
     */
    public boolean isDead() {
        return dead;
    }
    /** This is a getter of the active attribute
     * @return boolean returns if the enemy is still alive
     */
    public boolean isActive() {
        return active;
    }
    /** This is a setter of the active attribute
     * @param active Sets if the enemy is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    /** This method checks if the enemy has collided with the player
     * @param player inputs a player to check for collisions
     * @return boolean Returns if an enemy collides with a player
     */
    public boolean hasCollidedWith(Player player) {
        return enemyImage.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /** This method checks if the enemy has collided with bullets from a player
     * @param bullet inputs a specific bullet
     * @return boolean Returns if the enemy has collided with a bullet
     */
    public boolean collidedWithBullet(Bullet bullet) {
        return getEnemyImage().getBoundingBoxAt(getPosition()).intersects(bullet.getDrawPosition());
    }
    /** This is an abstract method that handles
     * logic between enemy and player
     * @param player inputs a player
     */
    abstract public void update(Player player);
    /** This sets a specific image of an enemy
     * @param image inputs an image of the enemy that will be rendered
     */
    public void setEnemyImage(Image image) {
        enemyImage = image;
    }
    /** This is sets a specific health of an enemy
     * @param anotherHealth inputs specific starting health of enemy
     */
    public void setHealth(double anotherHealth) {
        health = anotherHealth;
    }
    /** Sets the "killed" status of the enemy
     * @param isDead inputs whether enemy is dead or not
     */
    public void setDead(boolean isDead) {
        dead = isDead;
    }
    /** This is a getter method for the health of the enemy
     * @return double Returns the health of an enemy
     */
    public double getHealth() {
        return this.health;
    }
    /** This is a getter method for the image of an enemy
     * @return Image returns the image of an Enemy
     */
    public Image getEnemyImage() {
        return enemyImage;
    }
    /** This is a setter method for the position of an enemy
     * @param point This inputs a specific coordinate for the enemy
     */
    public void setPosition(Point point) {
        position = point;
    }
    /** This is a getter method for the position of an enemy
     * @return point This returns the specific coordinate for the enemy
     */
    public Point getPosition() {
        return position;
    }
    /** This is a setter method for the storeOpen attribute
     * @param storeOpen inputs if the store is open or not
     */
    public void setStoreOpen(boolean storeOpen) {
        isStoreOpen = storeOpen;
    }
}
/** This represents the KeyBulletKin class
 * This class extends from the Enemy superclass
 */
class NewKeyBulletKin extends Enemy {
    private ArrayList<Point> PointsArrayList;
    private int currentTarget;
    private Key newKey;
    private static int SPEED = 4;
    private static double PLAYER_HEALTH_LOSS = 0.2;
    /** This is the constructor of the NewKeyBulletKin class
     * @param points inputs an array of points where the NewKeyBulletKin class
     *               travels in a loop
     */
    public NewKeyBulletKin(ArrayList<Point> points) {
        setEnemyImage(new Image("res/key_bullet_kin.png"));
        setHealth(Double.parseDouble(ShadowDungeon.gameProps.getProperty("keyBulletKinHealth")));
        setDead(false);
        PointsArrayList = points;
        currentTarget = 0;
        setPosition(points.get(0));
        setActive(false);
        this.newKey = new Key();
        setStoreOpen(false);
    }
    /** This is the implementation of the abstract update method
     * Tt handles interactions with a player including when it
     * comes into contact and its constant movement
     * @param player inputs a player to account for its interactions
     */
    @Override
    public void update(Player player) {
        if (!isDead() && isActive()) {
            // Ensures the loop is maintained
            if (currentTarget == PointsArrayList.size()) {
                currentTarget = 0;
            }
            Vector2 current = new Vector2(getPosition().x, getPosition().y);
            Vector2 target = new Vector2(PointsArrayList.get(currentTarget).x, PointsArrayList.get(currentTarget).y);
            // determines the direction where the enemy has to move
            Vector2 direction = target.sub(current);
            // iterates through the loop
            if (direction.length() < PointsArrayList.size()) {
                setPosition(target.asPoint());
                if (!player.isStoreOpen()) {
                    currentTarget += 1;
                }
            } else {
                setPosition(current.add(direction.normalised().mul(SPEED)).asPoint());
            }
            draw();
        }
        // if a player has collided with KeyBulletKin
        if (hasCollidedWith(player) && !isDead()) {
            player.setHealth(player.getHealth() - PLAYER_HEALTH_LOSS);
        }
        // If a bullet is in contact with the enemy
        if (player.getBulletArrayList().size() > 0) {
            for (Bullet bullet: player.getBulletArrayList()) {
                if (collidedWithBullet(bullet)) {
                    setHealth(getHealth() - player.giveDamage());
                }
            }
        }
        // when the enemy is killed
        if (getHealth() < 0 && isActive()) {
            setDead(true);
            newKey.setKeyPosition(getPosition());
            // drop a key
            newKey.update(player);
        }
    }
}
/** This represents the BulletKinType class
 * This class extends from the Enemy superclass
 * This also acts as a superclass
 */
abstract class BulletKinType extends Enemy implements Weapon {
    private int currentTarget;
    private ArrayList<Fireball> fireBallArrayList;
    /** This represents a getter method of a fireBallArrayList
     * @return ArrayList<Fireball> returns the current fireBallArrayList
     */
    public ArrayList<Fireball> getFireBallArrayList() {
        return fireBallArrayList;
    }
    /** This represents a getter method of the currentTarget attribute
     * @return int returns the currentTarget attribute
     */
    public int getCurrentTarget() {
        return currentTarget;
    }
    /** This represents a setter method of the fireBallArrayList attribute
     * @param fireBallArrayList This inputs a fireBallArrayList for the enemy
     */
    public void setFireBallArrayList(ArrayList<Fireball> fireBallArrayList) {
        this.fireBallArrayList = fireBallArrayList;
    }
    /** This represents a setter method of the currentTarget attribute
     * @param currentTarget inputs the currentTarget as an integer
     */
    public void setCurrentTarget(int currentTarget) {
        this.currentTarget = currentTarget;
    }
    /** This represents a method that checks if the fireballs of the enemies
     * are in contact with closedDoors of all doors in BattleRoom
     * @param primaryDoor inputs the primaryDoor of a BattleRoom to check for collisions
     * @param secondaryDoor inputs the secondaryDoor of a BattleRoom to check for colllisions
     * @param fireBallArrayList inputs the fireBallArrayList of an enemy
     */
    public void fireballDoorCollision (Door primaryDoor, Door secondaryDoor, ArrayList<Fireball> fireBallArrayList) {
        for (Fireball fireball: fireBallArrayList) {
            // make the fireball disappear if it comes in contact with the closed door
            if (!primaryDoor.isUnlocked() && primaryDoor.getImage().getBoundingBoxAt(primaryDoor.getPosition()).intersects(fireball.getFireballImage().getBoundingBoxAt(fireball.getDrawPosition()))) {
                fireball.setPresent(false);
            }
            if (!secondaryDoor.isUnlocked() && secondaryDoor.getImage().getBoundingBoxAt(secondaryDoor.getPosition()).intersects(fireball.getFireballImage().getBoundingBoxAt(fireball.getDrawPosition()))) {
                fireball.setPresent(false);
            }
        }
    }
    /** This represents a method that checks if the fireballs of the enemies
     * are in contact with the walls
     * @param wall inputs a specific wall tile to check for collisions
     */
    public void collideWithWall(Wall wall) {
        for (int i = 0; i < fireBallArrayList.size(); i ++) {
            if (wall.hasCollidedWithFireball(fireBallArrayList.get(i))) {
                // makes the fireball disappear if it collides with a wall
                fireBallArrayList.get(i).setPresent(false);
            }
        }
    }
}
/** This represents the BulletKin class
 * This inherits from the BulletKinType class
 * It also implements the Weapon interface
 */
class BulletKin extends BulletKinType implements Weapon {
    /** This represents the constructor of the BulletKin class
     * @param position returns the coordinates where the Enemy will be rendered
     */
    public BulletKin(Point position) {
        setEnemyImage(new Image("res/bullet_kin.png"));
        setHealth(Double.parseDouble(ShadowDungeon.gameProps.getProperty("bulletKinHealth")));
        setDead(false);
        setCurrentTarget(0);
        setPosition(position);
        setActive(false);
        setFireBallArrayList(new ArrayList<>());
    }
    /** This represents the interaction between enemy and player
     * This is the specific implementation of Weapon interface
     * @param player inputs a player to determine interaction logic
     */
    public void update(Player player) {
        // releases a fireball every 360 frames
        if (ShadowDungeon.frameCounter % Integer.parseInt(ShadowDungeon.gameProps.getProperty("bulletKinShootFrequency")) == 0 && isActive() && !isDead()) {
            Fireball fireball= new Fireball(getPosition(), player.getPosition());
            getFireBallArrayList().add(fireball);
        }
        // if the enemy is still alive
        if (!isDead() && isActive()) {
            draw();
        }
        // if the enemy is dead
        if (getHealth() < 0 && isActive()) {
            setDead(true);
        }
        // if a fireball interacts with a player
        if (getFireBallArrayList().size() > 0 && !player.isStoreOpen()) {
            for (Fireball fireball1 : getFireBallArrayList()) {
                fireball1.update(player);
            }
        }
        // If a bullet interacts with the enemy
        if (player.getBulletArrayList().size() > 0) {
            for (Bullet bullet: player.getBulletArrayList()) {
                if (getEnemyImage().getBoundingBoxAt(getPosition()).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()))) {
                    setHealth(getHealth() - player.giveDamage());
                }
            }
        }

    }
}
/** This represents the AshenBulletKin class
 * This inherits from the BulletKinType class
 * It also implements the Weapon interface
 */
class AshenBulletKin extends BulletKinType implements Weapon {
    /** This represents the constructor of the AshenBulletKin class
     * @param position returns the coordinates where the Enemy will be rendered
     */
    public AshenBulletKin(Point position) {
        setEnemyImage(new Image("res/ashen_bullet_kin.png"));
        setHealth(Double.parseDouble(ShadowDungeon.gameProps.getProperty("ashenBulletKinHealth")));
        setDead(false);
        setCurrentTarget(0);
        setPosition(position);
        setActive(false);
        setFireBallArrayList(new ArrayList<>());
    }
    /** This is an implementation of the update method
     * in the Weapon Interface
     * @param player inputs a player to work out interactions
     *               between enemy and player
     */
    public void update(Player player) {
        // shoots fireballs every 240 frames
        if (ShadowDungeon.frameCounter % Integer.parseInt(ShadowDungeon.gameProps.getProperty("ashenBulletKinShootFrequency")) == 0 && isActive() && !isDead()) {
            Fireball fireball= new Fireball(getPosition(), player.getPosition());
            getFireBallArrayList().add(fireball);
        }
        // if the enemy is still alive
        if (!isDead() && isActive()) {
            draw();

        }
        // if the enemy is dead
        if (getHealth() < 0 && isActive()) {
            setDead(true);
        }
        // if a fireball interacts with a player
        if (getFireBallArrayList().size() > 0 && !player.isStoreOpen()) {
            for (Fireball fireball1 : getFireBallArrayList()) {
                fireball1.update(player);
            }
        }
        // If a bullet interacts with the enemy
        if (player.getBulletArrayList().size() > 0) {
            for (Bullet bullet: player.getBulletArrayList()) {
                if (getEnemyImage().getBoundingBoxAt(getPosition()).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()))) {
                    setHealth(getHealth() - player.giveDamage());
                }
            }
        }

    }
}