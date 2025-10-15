import bagel.Image;
import bagel.util.Point;

/** This describes a Key class and is dropped when
 * a player kills the KeyBulletKin
 */
public class Key {
    private final Image keyImage = new Image("res/key.png");
    private Point keyPosition;
    private boolean isSelected;
    private final static int INCREASE = 1;
    /** Constructor of Key
     */
    public Key() {
        isSelected = false;
    }
    /** This is a setter method of isSelection attribute
     * @param selected input if a key is dropped or not
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
    /** This is a setter method of position attribute
     * @param position sets the position of the key when key is dropped
     */
    public void setKeyPosition(Point position) {
        this.keyPosition = position;
    }
    /** This is a upadate method handling interactions between player and key
     * @param player inputs the player
     */
    public void update(Player player) {
        if (!isSelected) {
            keyImage.draw(this.keyPosition.x, this.keyPosition.y);
            // if a player touches the key, increase key count by 1
            if (keyImage.getBoundingBoxAt(keyPosition).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()))) {
                player.setKeys(player.getKeys() + INCREASE);
                setSelected(true);
            }

        }




    }

}
