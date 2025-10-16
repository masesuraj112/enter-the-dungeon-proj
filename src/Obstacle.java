import bagel.*;
import bagel.util.Point;
/** This describes an Obstacle superclass which are types of objects that a player cannot pass through
 */
public class Obstacle {
    private Image obstacleImage;
    private Point currentPosition;
    private boolean isActive;
    /** This is an obstacle constructor
     * @param point inputs the point where the obstacle will be rendered
     */
    public Obstacle(Point point) {
        this.currentPosition = point;
    }
    /** This is a setter method for the active attribute
     * @param active inputs whether the obstacle will be rendered or not
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    /** This is a getter method for the active attribute
     * @return boolean returns whether the obstacle is active or not
     */
    public boolean isActive() {
        return isActive;
    }
    /** This is a setter method for the Image attribute
     * @param obstacleImage sets a specific image
     */
    public void setObstacleImage(Image obstacleImage) {
        this.obstacleImage = obstacleImage;
    }
    /** This is a getter method for the Image attribute
     * @return Image returns the obstacleImage
     */
    public Image getObstacleImage() {
        return obstacleImage;
    }
    /** This is a getter method for the position attribute
     * @return Point returns the coordinates of the obstacle
     */
    public Point getCurrentPosition() {
        return currentPosition;
    }
    /** This is an update method
     * if the obstacle is active, then keep drawing it
     */
    public void update() {
        if (isActive()) {
            getObstacleImage().draw(getCurrentPosition().x, getCurrentPosition().y);
        }
    }
    /** This checks if a bullet has shot at the obstacle
     * @param player inputs the specific player
     */
    public void collideWithPlayer(Player player) {
        // collision logic
        if (player.getCurrImage().getBoundingBoxAt(player.getPosition()).intersects(obstacleImage.getBoundingBoxAt(currentPosition))) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
        // checking if bullet has made contact with the obstacle
        for (Bullet bullet: player.getBulletArrayList()) {
            if (isActive() && bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()).intersects(getObstacleImage().getBoundingBoxAt(getCurrentPosition()))) {
                // this kills the enemy
                setActive(false);
            }
        }
    }
}
/** This describes a Table Class
 * A table class is a subclass of Obstacle
 */
class Table extends Obstacle {
    /** This is a constructor of the Table class
     * @param point inputs the coordinates of the obstacle
     */
    public Table(Point point) {
        super(point);
        setObstacleImage(new Image("res/table.png"));
    }
}
/** This describes a Basket Class
 * A table class is a subclass of Obstacle
 */
class Basket extends Obstacle {
    /** This is a constructor of the Basket class
     * @param point inputs the coordinates of the obstacle
     */
    public Basket(Point point) {
        super(point);
        setObstacleImage(new Image("res/basket.png"));
    }
    /** This is an overridden method of collideWithPlayer(Player player)
     * It contains extra functionality including the fact that when a basket
     * is eliminated, a player gains more coins
     */
    @Override
    public void collideWithPlayer(Player player) {
        // collision logic
        if (isActive() && player.getCurrImage().getBoundingBoxAt(player.getPosition()).intersects(getObstacleImage().getBoundingBoxAt(getCurrentPosition()))) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
        for (Bullet bullet: player.getBulletArrayList()) {
            if (isActive() && bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()).intersects(getObstacleImage().getBoundingBoxAt(getCurrentPosition()))) {
                setActive(false);
                // If a player shoots at the Basket, the player earns 20 coins
                player.earnCoins(Integer.parseInt(ShadowDungeon.gameProps.getProperty("basketCoin")));
            }
        }
    }
}
/** This represents a wall which inherits from the obstacle
 * superclass and is present in one of the BattleRooms
 */
class Wall extends Obstacle {
    /** This is a Wall Constructor
     * @param position inputs the coordinates of an individual wall unit
     */
    public Wall(Point position) {
        super(position);
        setObstacleImage(new Image("res/wall.png"));
    }
    /** This is an overridden method of collideWithPlayer(Player player)
     * Unlike other obstacles, when a bullet comes into contact, the wall units
     * do not disappear
     * @param player inputs a player
     */
    @Override
    public void collideWithPlayer(Player player) {
        if (hasCollidedWith(player)) {
            // set the player to its position prior to attempting to move through this wall
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
        if (player.getBulletArrayList().size() > 0) {
            for (Bullet bullet: player.getBulletArrayList()) {
                if (hasCollidedWithBullet(bullet)) {
                    bullet.setPresent(false);
                }
            }
        }
    }
    /** This method draws the wall
     */
    public void draw() {
        getObstacleImage().draw(getCurrentPosition().x, getCurrentPosition().y);
    }
    /** This method checks if a player has collided with a wall
     * @param player inputs a player
     * @return boolean returns true or false if a player is in contact with a wall
     */
    public boolean hasCollidedWith(Player player) {
        return getObstacleImage().getBoundingBoxAt(getCurrentPosition()).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /** This method checks if a player has collided with a bullet
     * @param bullet inputs a bullet
     * @return boolean returns true or false if a bullet is in contact with a wall
     */
    public boolean hasCollidedWithBullet(Bullet bullet) {
        return getObstacleImage().getBoundingBoxAt(getCurrentPosition()).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()));
    }
    /** This method checks if a player has collided with a fireball
     * @param fireball inputs a fireball
     * @return boolean returns true or false if a fireball is in contact with a wall
     */
    public boolean hasCollidedWithFireball(Fireball fireball) {
        return getObstacleImage().getBoundingBoxAt(getCurrentPosition()).intersects(fireball.getFireballImage().getBoundingBoxAt(fireball.getDrawPosition()));
    }
}
