import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
/** This represents the Fireball class
 * These are types of bullets that some of the enemies have
 */
public class Fireball {
    private final Image fireballImage = new Image("res/fireball.png");
    private Vector2 position;
    private Vector2 direction;
    private boolean isPresent;
    /** This represents the Fireball constructor
     * @param playerPosition inputs the current position of the player
     * @param enemyPosition inputs the current enemyPosition
     */
    public Fireball(Point playerPosition, Point enemyPosition) {
        this.position = new Vector2(playerPosition.x, playerPosition.y);
        Vector2 target = new Vector2(enemyPosition.x, enemyPosition.y);
        // works out the direction from player to enemy
        this.direction = target.sub(this.position).normalised();
        isPresent = true;
    }
    /** This is a getter method of the isPresent attribute
     * @return boolean returns if the fireball is present or not
     */
    public boolean isPresent() {
        return isPresent;
    }
    /** This method operates interactions between player and fireball
     * @param player inputs a player to handle interactions
     */
    public void update(Player player) {
        // generate fireball if present
        if (isPresent) {
            fireballImage.draw(this.position.asPoint().x, this.position.asPoint().y);
        }
        position = position.add(direction.mul(Double.parseDouble(ShadowDungeon.gameProps.getProperty("fireballSpeed"))));
        if (isPresent && fireballImage.getBoundingBoxAt(position.asPoint()).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()))) {
            // player receives damage if it comes in contact with the fireball
            player.receiveDamage(Double.parseDouble(ShadowDungeon.gameProps.getProperty("fireballDamage")));
            isPresent = false;
        }
    }
    /** This is a setter method for the present attribute
     * @param present inputs whether a fireball is active or not
     */
    public void setPresent(boolean present) {
        isPresent = present;
    }
    /** This is a getter method for the fireballImage attribute
     * @return Image returns an image of the fireball
     */
    public Image getFireballImage() {
        return fireballImage;
    }
    /** This is a getter method for the position attribute
     * @return Point returns the coordinates of the fireball
     */
    public Point getDrawPosition() {
        return position.asPoint();
    }

}
