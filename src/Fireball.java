import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;

public class Fireball {
    private final Image bulletImage = new Image("res/fireball.png");
    private Vector2 position;
    private Vector2 direction;
    private boolean isPresent;
    public Fireball(Point playerPosition, Point enemyPosition) {
        this.position = new Vector2(playerPosition.x, playerPosition.y);
        Vector2 target = new Vector2(enemyPosition.x, enemyPosition.y);
        this.direction = target.sub(this.position).normalised();

    }

    public boolean isPresent() {
        return isPresent;
    }

    public void update() {
        position = position.add(direction.mul(2));
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public Image getBulletImage() {
        return bulletImage;
    }

    public Point getDrawPosition() {
        return position.asPoint();
    }






}
