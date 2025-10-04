import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;
import bagel.util.Vector2;


abstract public class Enemy {
    private double health;
    private Image enemyImage;
    private boolean active = false; // only true when the Battle Room has been activated
    private boolean dead = false;
    private Point position;

    public void draw() {
        enemyImage.draw(position.x, position.y);
    }
    public boolean isDead() {
        return dead;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean hasCollidedWith(Player player) {
        return enemyImage.getBoundingBoxAt(position).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }
    abstract public void update();
    public void setEnemyImage(Image image) {
        enemyImage = image;
    }
    public void setHealth(double anotherHealth) {
        health = anotherHealth;
    }
    public void setDead(boolean isDead) {
        dead = isDead;
    }
    public double getHealth() {
        return this.health;
    }

    public Image getEnemyImage() {
        return enemyImage;
    }
    public void setPosition(Point point) {
        position = point;
    }
    public Point getPosition() {
        return position;
    }

}





class NewKeyBulletKin extends Enemy {
    private ArrayList<Point> PointsArrayList;
    private int currentTarget;

    // for all other enemies have a start pos but not this one
    public NewKeyBulletKin(ArrayList<Point> points) {
        setEnemyImage(new Image("res/key_bullet_kin.png"));
        setHealth(Double.parseDouble(ShadowDungeon.gameProps.getProperty("keyBulletKinHealth")));
        setDead(false);
        PointsArrayList = points;
        currentTarget = 0;
        setPosition(points.get(0));


    }

   @Override
    public void update() {
        if (!isDead()) {
            if (currentTarget == PointsArrayList.size()) {
                currentTarget = 0;
            }
            System.out.println(getPosition().x);
            System.out.println(getPosition().y);

            Vector2 current = new Vector2(getPosition().x, getPosition().y);
            Vector2 target = new Vector2(PointsArrayList.get(currentTarget).x, PointsArrayList.get(currentTarget).y);
            Vector2 direction = target.sub(current);

            if (direction.length() < 5) {
                setPosition(target.asPoint());

                currentTarget += 1;

            } else {
                setPosition(current.add(direction.normalised().mul(5)).asPoint());
            }

            drawEnemy();

//
        }
//        if (hasCollidedWith(player)) {
//           setHealth(player.getHealth() - 0.2);
//        }

        if (getHealth() < 0) {
            setDead(true);
        }
    }

    public void drawEnemy() {
        System.out.println("inside");
        getEnemyImage().draw(getPosition().x, getPosition().y);
    }

//   for (Point point: PointsArrayList) {
////                setPosition(new Point(point.x, point.y));
////                System.out.println(getPosition().x);
////                drawEnemy();
//////                getEnemyImage().draw(point.x, point.y);
////            }






}