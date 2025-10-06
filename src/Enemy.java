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

    public void drawEnemy() {
        getEnemyImage().draw(getPosition().x, getPosition().y);
    }

    public boolean collidedWithBullet(Bullet bullet) {
        return getEnemyImage().getBoundingBoxAt(getPosition()).intersects(bullet.getDrawPosition());
    }
    abstract public void update(Player player);
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
    private Key newKey;

    public NewKeyBulletKin(ArrayList<Point> points) {
        setEnemyImage(new Image("res/key_bullet_kin.png"));
        setHealth(Double.parseDouble(ShadowDungeon.gameProps.getProperty("keyBulletKinHealth")));
        setDead(false);
        PointsArrayList = points;
        currentTarget = 0;
        setPosition(points.get(0));
        setActive(false);
        this.newKey = new Key();
    }

   @Override
    public void update(Player player) {
        if (!isDead() && isActive()) {
            if (currentTarget == PointsArrayList.size()) {
                currentTarget = 0;
            }


            Vector2 current = new Vector2(getPosition().x, getPosition().y);
            Vector2 target = new Vector2(PointsArrayList.get(currentTarget).x, PointsArrayList.get(currentTarget).y);
            Vector2 direction = target.sub(current);

            if (direction.length() < 5) {
                setPosition(target.asPoint());

                currentTarget += 1;

            } else {
                setPosition(current.add(direction.normalised().mul(4)).asPoint());
            }
            drawEnemy();
        }
        if (hasCollidedWith(player) && !isDead()) {
            player.setHealth(player.getHealth() - 0.2);
        }

        if (player.getBulletArrayList().size() > 0) {
            for (Bullet bullet: player.getBulletArrayList()) {
                if (collidedWithBullet(bullet)) {

                    if (player.getWeapon() == 0) {
                        setHealth(getHealth() - Double.parseDouble(ShadowDungeon.gameProps.getProperty("weaponStandardDamage")));
                    }
                }

            }
        }
        if (getHealth() < 0 && isActive()) {
            setDead(true);
            newKey.setKeyPosition(getPosition());

            newKey.update(player);

        }
    }



}

class BulletKin extends Enemy {
    private int currentTarget;
    private static int frameCounter;
    private ArrayList<Fireball> fireBallArrayList;


    public BulletKin(Point position) {
        setEnemyImage(new Image("res/bullet_kin.png"));
        setHealth(Double.parseDouble(ShadowDungeon.gameProps.getProperty("bulletKinHealth")));
        setDead(false);
        currentTarget = 0;
        setPosition(position);
        setActive(false);
        frameCounter = 0;
        this.fireBallArrayList = new ArrayList<>();

    }

    @Override
    public void update(Player player) {
        if (frameCounter % Integer.parseInt(ShadowDungeon.gameProps.getProperty("bulletKinShootFrequency")) == 0 && isActive()) {
            Fireball fireball= new Fireball(getPosition(), player.getPosition());
            this.fireBallArrayList.add(fireball);


        }

            if (!isDead() && isActive()) {
                drawEnemy();

            }

            if (getHealth() < 0 && isActive()) {
                setDead(true);
            }

        if (this.fireBallArrayList.size() > 0) {
            for (Fireball fireball1 : fireBallArrayList) {
                fireball1.setPresent(true);
                fireball1.update();
                Point drawPos = fireball1.getDrawPosition();
                fireball1.getBulletImage().draw(drawPos.x, drawPos.y);
            }
        }

        if (player.getBulletArrayList().size() > 0) {
            for (Bullet bullet: player.getBulletArrayList()) {
//                return image.getBoundingBoxAt(position).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()));

                if (getEnemyImage().getBoundingBoxAt(getPosition()).intersects(bullet.getBulletImage().getBoundingBoxAt(bullet.getDrawPosition()))) {
                    setHealth(getHealth() - player.giveDamage());
                }
            }
        }

        frameCounter += 1;


        }

}