import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;

public class Fireball {
    private final Image fireballImage = new Image("res/fireball.png");
    private Vector2 position;
    private Vector2 direction;
    private boolean isPresent;
    public Fireball(Point playerPosition, Point enemyPosition) {
        this.position = new Vector2(playerPosition.x, playerPosition.y);
        Vector2 target = new Vector2(enemyPosition.x, enemyPosition.y);
        this.direction = target.sub(this.position).normalised();
        isPresent = true;

    }

    public boolean isPresent() {
        return isPresent;
    }

    public void update(Player player) {

        if (isPresent) {
            fireballImage.draw(this.position.asPoint().x, this.position.asPoint().y);
        }

        position = position.add(direction.mul(Double.parseDouble(ShadowDungeon.gameProps.getProperty("fireballSpeed"))));

        if (isPresent && fireballImage.getBoundingBoxAt(position.asPoint()).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()))) {
            player.receiveDamage(Double.parseDouble(ShadowDungeon.gameProps.getProperty("fireballDamage")));
        }


    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public Image getFireballImage() {
        return fireballImage;
    }

    public Point getDrawPosition() {
        return position.asPoint();
    }






}
