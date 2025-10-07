import bagel.Image;
import bagel.util.Point;

/**
 * Obstacle that blocks the player from moving through it
 */
public class Wall {
    private final Point position;
    private final Image image;

    public Wall(Point position) {
        this.position = position;
        this.image = new Image("res/wall.png");
    }

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

    public void draw() {
        image.draw(position.x, position.y);
    }

    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    public boolean hasCollidedWithBullet(Bullet bullet) {
        return image.getBoundingBoxAt(position).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()));
    }

    public boolean hasCollidedWithFireball(Fireball fireball) {
        return image.getBoundingBoxAt(position).intersects(fireball.getFireballImage().getBoundingBoxAt(fireball.getDrawPosition()));
    }



}