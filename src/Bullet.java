import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;

/** This represents the Bullet class
 * When a player shoots a bullet the class handles its movement and collision logic
 */
public class Bullet {
    private final Image bulletImage = new Image("res/bullet.png");
    private Vector2 position;
    private Vector2 direction;
    private boolean isPresent;
    private static final int SPEED = 5;
    /** This is the constructor of a Bullet
     * When a player shoots a bullet the class handles its movement and collision logic
     * @param position This specifies the exact coordinates of where the bullet should be released from
     * @param input This passes through an input parameter which is helpful
     *             for providing the starting coordinates of the bullet as well as
     *              the mouse direction
     */
    public Bullet(Point position, Input input) {
        this.position = new Vector2(position.x, position.y);
        // Works out the coordinates of the mouse
        Vector2 target = new Vector2(input.getMouseX(), input.getMouseY());
        // Works out the direction of the mouse
        this.direction = target.sub(this.position).normalised();
        setPresent(true);
    }
    /** This is a getter of the isPresent attribute
     * @return boolean This returns if the bullet is present or not
     */
    public boolean isPresent() {
        return isPresent;
    }
    /** This method ensures that a bullet travels constantly in
     * same direction that the mouse points if it is active
     */
    public void update() {
        position = position.add(direction.mul(SPEED));
        if (this.isPresent) {
            draw();
        }
    }
    /** This method checks if a bullet has collided with a closed door
     * If there is a collision, make the bullet disappear
     * @param door This parameter is a specific door that is checked
     */
    public void bulletDoorCollision(Door door) {
        if (!door.isUnlocked() && bulletImage.getBoundingBoxAt(position.asPoint()).intersects(door.getImage().getBoundingBoxAt(door.getPosition()))) {
            isPresent = false;
        }
    }
    /** This a setter of the isPresent attribute
     * @param present Sets isPresent to either true or false
     */
    public void setPresent(boolean present) {
        this.isPresent = present;
    }
    /** This a getter of the Bullet image attribute
     * @return Image returns the bullet Image
     */
    public Image getBulletImage() {
        return bulletImage;
    }
    private void draw() {
        // Draws the bullet at a specified position
        bulletImage.draw(position.x, position.y);
    }
    /** This a getter the current position of a bullet
     * @return Point returns the coordinates of the bullet
     */
    public Point getDrawPosition() {
        return position.asPoint();
    }


    

}
