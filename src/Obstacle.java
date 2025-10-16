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
     * It contains extra functionality including the fact that when a Basket
     * is eliminated, a player gains more
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
