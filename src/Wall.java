import bagel.Image;
import bagel.util.Point;

/** This is a Wall Class where
 * it is an obstacle that blocks the player from moving through it
 */
public class Wall {
    private final Point position;
    private final Image image;
    /** This is a Wall Constructor
     * @param position inputs the coordinates of an individual wall unit
     */
    public Wall(Point position) {
        this.position = position;
        this.image = new Image("res/wall.png");
    }
    /** This method handles interactions between wall and player
     * @param player inputs a player
     */
    public void update(Player player) {
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
        image.draw(position.x, position.y);
    }
    /** This method checks if a player has collided with a wall
     * @param player inputs a player
     * @return boolean returns true or false if a player is in contact with a wall
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /** This method checks if a player has collided with a bullet
     * @param bullet inputs a bullet
     * @return boolean returns true or false if a bullet is in contact with a wall
     */
    public boolean hasCollidedWithBullet(Bullet bullet) {
        return image.getBoundingBoxAt(position).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()));
    }
    /** This method checks if a player has collided with a fireball
     * @param fireball inputs a fireball
     * @return boolean returns true or false if a fireball is in contact with a wall
     */
    public boolean hasCollidedWithFireball(Fireball fireball) {
        return image.getBoundingBoxAt(position).intersects(fireball.getFireballImage().getBoundingBoxAt(fireball.getDrawPosition()));
    }
}