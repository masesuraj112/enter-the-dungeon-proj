import bagel.Image;
import bagel.util.Point;

/**
 * Hazard that applies damage for as long as the player is on it
 */
public class River{
    private final Point position;
    private final Image image;
    private final double damagePerFrame;
    private boolean isCollided;
    private boolean isFirstContact;

    public River(Point position) {
        this.position = position;
        this.image = new Image("res/river.png");
        damagePerFrame = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
        isFirstContact = false;

    }

    public void update(Player player) {
//        if (hasCollidedWith(player)) {
////            player.receiveDamage(damagePerFrame);
//            isCollided = true;
//        } else {
//            isCollided = false;
////            isFirstContact = false;
//        }

        if (hasCollidedWith(player) && ShadowDungeon.chosenCharacter.equals("robot")) {
            isCollided = true;
            isFirstContact = false;
        } else if (hasCollidedWith(player) && ShadowDungeon.chosenCharacter.equals("marine") && !isFirstContact) {
            player.receiveDamage(damagePerFrame);
            isFirstContact = true;
            isCollided = false;
        } else {
            isCollided = false;
        }

        if (isCollided) {
            player.receiveDamage(damagePerFrame);

        }

//        if (isCollided && ShadowDungeon.chosenCharacter.equals("robot")) {
//            player.receiveDamage(damagePerFrame);
//        } else if (isCollided && ShadowDungeon.chosenCharacter.equals("marine")) {
//            player.receiveDamage(damagePerFrame);
//            isCollided = false;
//            isFirstContact = true;
//
//        }
    }

    public void draw() {
        image.draw(position.x, position.y);
    }

    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
}