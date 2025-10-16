import bagel.Image;
import bagel.util.Point;

/** This is a river class
 */
public class River{
    private final Point position;
    private final Image image;
    private final double damagePerFrame;
    private boolean isCollided;
    private boolean isFirstContact;
    /** This is a river constructor
     * @param position inputs where the river tile should be rendered
     */
    public River(Point position) {
        this.position = position;
        this.image = new Image("res/river.png");
        damagePerFrame = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
        isFirstContact = false;

    }
    /** This is the update method that handles interactions between player and river
     * @param player inputs a player
     */
    public void update(Player player) {
        // if character is robot, health loss should be continuous
        if (hasCollidedWith(player) && ShadowDungeon.chosenCharacter.equals("robot")) {
            isCollided = true;
            isFirstContact = false;
            // if character is marine, health loss shouldn't be continuous
        } else if (hasCollidedWith(player) && ShadowDungeon.chosenCharacter.equals("marine") && !isFirstContact) {
            player.receiveDamage(damagePerFrame);
            isFirstContact = true;
            isCollided = false;
        } else {
            isCollided = false;
        }
        // implements continuous health loss
        if (isCollided) {
            player.receiveDamage(damagePerFrame);
        }
    }
    /** Draws an individual river tile
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /** Helper method that checks if river tile has collided with player
     * @param player inputs a player
     * @return boolean outputs true or false if there is a collision
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
}