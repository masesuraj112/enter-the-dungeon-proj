import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;

public class Bullet {
    private final Image bulletImage = new Image("res/bullet.png");
    private Vector2 position;
    private Vector2 direction;
    private boolean isPresent;
    public Bullet(Point position, Input input) {
        this.position = new Vector2(position.x, position.y);
        Vector2 target = new Vector2(input.getMouseX(), input.getMouseY());
        this.direction = target.sub(this.position).normalised();

    }

    public boolean isPresent() {
        return isPresent;
    }

    public void update() {
        position = position.add(direction.mul(5));
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
