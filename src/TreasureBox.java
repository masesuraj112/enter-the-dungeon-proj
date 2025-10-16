import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/** Chest that can be unlocked by the player to earn coins
 */
public class TreasureBox {
    private final Point position;
    private final Image image;
    private final double coinValue;
    private boolean active = true;
    /** This is a constructor of the TreasureChest class
     * @param position inputs the specific coordinates of the treasure box
     * @param coinValue inputs the specific amount of coins inside the treasure box
     */
    public TreasureBox(Point position, double coinValue) {
        this.position = position;
        this.coinValue = coinValue;
        this.image = new Image("res/treasure_box.png");
    }
    /** This is the update method of a treasure box which handles its state
     * @param input inputs keyboard logic
     * @param player inputs a player
     */
    public void update(Input input, Player player) {
        // checks if player has sufficient number of keys
        if (hasCollidedWith(player) && input.wasPressed(Keys.K) && player.getKeys() >= 1) {
            player.earnCoins(coinValue);
            player.setKeys(player.getKeys() - 1);
            active = false;
        }
    }
    /** This method draws the Treasure Box
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /** This is a helper method to check if a player is in contact with a treasure box
     * @param player inputs a player
     * @return boolean returns true or false if a player is in contact with a treasure box
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    /** This is a getter method for the active attribute
     * @return boolean returns true or false if a treasure box should be rendered or not
     */
    public boolean isActive() {
        return active;
    }
}