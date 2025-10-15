import bagel.Image;
import bagel.util.Point;
/** Door which can be locked or unlocked, allows the player to move to the room it's connected to
 */
public class Door {
    private final Point position;
    private Image image;
    /** This attribute specifies the next room from a specific room
     */
    public final String toRoomName;
    /** This attribute sets a specific BattleRoom
     */
    public BattleRoom battleRoom; // only set if this door is inside a Battle Room
    private boolean unlocked = false;
    private boolean justEntered = false; // when the player only just entered this door's room
    private boolean shouldLockAgain = false;
    private static final Image LOCKED = new Image("res/locked_door.png");
    private static final Image UNLOCKED = new Image("res/unlocked_door.png");
    /** This a constructor of Door
     * @param position Inputs the coordinates of the door
     * @param toRoomName Inputs the room that door opens to
     */
    public Door(Point position, String toRoomName) {
        this.position = position;
        this.image = LOCKED;
        this.toRoomName = toRoomName;
    }
    /** This a constructor of Door used when it exists inside a battleRoom
     * @param position Inputs the coordinates of the door
     * @param toRoomName Inputs the room that door opens to
     * @param battleRoom Inputs the specific battleRoom
     */
    public Door(Point position, String toRoomName, BattleRoom battleRoom) {
        this.position = position;
        this.image = LOCKED;
        this.toRoomName = toRoomName;
        this.battleRoom = battleRoom;
    }
    /** This method handles collisions between player and door
     * @param player This parameter inputs a player that is used to check collisions
     */
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            onCollideWith(player);
        } else {
            onNoLongerCollide();
        }
    }
    /** This method draws the door
     */
    public void draw() {
        image.draw(position.x, position.y);
    }
    /** This method unlocks the door
     * @param justEntered Checks if we have just entered the door for the first time
     */
    public void unlock(boolean justEntered) {
        unlocked = true;
        image = UNLOCKED;
        this.justEntered = justEntered;
    }
    /** This helper method checks if there is a collision between door and player
     * @param player Inputs a player where a collision is checked
     * @return boolean Returns true or false if there is a collision
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    private void onCollideWith(Player player) {
        // when the player only just entered this door's room, overlapping with the unlocked door shouldn't trigger room transition
        if (unlocked && !justEntered) {
            ShadowDungeon.changeRoom(toRoomName);
        }
        if (!unlocked) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }

    private void onNoLongerCollide() {
        // when the player only just moved away from the unlocked door after walking through it
        if (unlocked && justEntered) {
            justEntered = false;
            // Battle Room activation conditions
            if (shouldLockAgain && battleRoom != null && !battleRoom.isComplete()) {
                unlocked = false;
                image = LOCKED;
                // Activates all the Enemies
                battleRoom.activateEnemies();
                // Actives all the Obstacles
                battleRoom.activateObstacles();
            }
        }
    }
    /** This method locks a door
     */
    public void lock() {
        unlocked = false;
        image = LOCKED;
    }
    /** This is a getter of the isUnlocked attribute
     * @return boolean returns if the door is unlocked or not
     */
    public boolean isUnlocked() {
        return unlocked;
    }
    /** This method sets the shouldLockAgain attribute to true
     */
    public void setShouldLockAgain() {
        this.shouldLockAgain = true;
    }
    /** This method returns the coordinates of the door
     * @return Point This is the exact coordinates of the door
     */
    public Point getPosition() {
        return position;
    }
    /** This method returns the image of the door
     * @return Image This renders the image of the door
     */
    public Image getImage() {
        return image;
    }
}
