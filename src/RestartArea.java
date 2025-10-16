import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/** Area in Prep or End Room where the player can trigger a game reset
 */
public class RestartArea {
    private final Point position;
    private final Image image;
    /** This is a RestartArea constructor
     * @param position initialises the coordinates of the RestartArea object
     */
    public RestartArea(Point position) {
        this.position = position;
        this.image = new Image("res/restart_area.png");
    }
    /** This is an update method which is called constantly
     * @param input inputs keyboard logic
     * @param player inputs a player
     */
    public void update(Input input, Player player) {
        // checks if player in contact with restart are and Enter is pressed
        if (hasCollidedWith(player) && input.wasPressed(Keys.ENTER)) {
            // resets the game state
            ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
        }
    }
    /** This draws the restartArea
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /** This is a helper function which determines if a player is in contact
     *  which restartArea
     * @param player inputs a player
     * @return boolean returns true or false if there is a collision
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
}
