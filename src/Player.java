import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * Player character that can move around and between rooms, defeat enemies, collect coins
 */
public class Player {
    private Point prevPosition;
    private Point position;
    private Image currImage;
    private double health;
    private double speed;
    private double coins = 0;
    private double keys = 0;
    private double weapon = 0;
    private ArrayList<Bullet> bulletArrayList;

//    public static String chosenCharacter;

    private boolean faceLeft = false;

    private static final Image RIGHT_IMAGE = new Image("res/player_right.png");
    private static final Image LEFT_IMAGE = new Image("res/player_left.png");

    public Player(Point position) {
        this.position = position;
        this.currImage = RIGHT_IMAGE;
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed"));
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth"));
//        this.chosenCharacter = "null";
        ShadowDungeon.chosenCharacter = "null";
        this.bulletArrayList = new ArrayList<>();


    }

    public Player(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys) {
        this.position = position;
        this.currImage = new Image(image);
        this.speed = speed;
        this.health = health;
        this.prevPosition = prevPosition;
        this.coins = coins;
        this.keys = keys;
        this.bulletArrayList = new ArrayList<>();

    }

    public Player() {
//        this.currImage = new Image("res/player_right.png");

//        System.out.println("Inside no-arg constructor");
    }

    public void setHealth(double health) {
        this.health = health;
    }




    public void update(Input input) {
        // check movement keys and mouse cursor
        double currX = position.x;
        double currY = position.y;

        if (input.isDown(Keys.A)) {
            currX -= speed;
        }
        if (input.isDown(Keys.D)) {
            currX += speed;
        }
        if (input.isDown(Keys.W)) {
            currY -= speed;
        }
        if (input.isDown(Keys.S)) {
            currY += speed;
        }

        faceLeft = input.getMouseX() < currX;

        // update the player position accordingly and ensure it can't move past the game window
        Rectangle rect = currImage.getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth() && topLeft.y >= 0 && bottomRight.y <= Window.getHeight()) {
            move(currX, currY);
        }


        if ((ShadowDungeon.chosenCharacter.equals("robot") || ShadowDungeon.chosenCharacter.equals("marine")) && wasAnyMousePressed(input)) {
            Bullet newBullet = new Bullet(getPosition(), input);
            this.bulletArrayList.add(newBullet);
        }

        if (this.bulletArrayList.size() > 0) {
            for (Bullet bullet: bulletArrayList) {
                bullet.setPresent(true);
                bullet.update();
                Point drawPos = bullet.getDrawPosition();
                bullet.getBulletImage().draw(drawPos.x, drawPos.y);
            }

        }





    }

    public void clearBulletArray() {
        for (Bullet bullet: bulletArrayList) {
            bulletArrayList.remove(bullet);
        }
    }

    public boolean wasAnyMousePressed(Input input) {
        return input.wasPressed(MouseButtons.LEFT) ||  input.wasPressed(MouseButtons.MIDDLE) || input.wasPressed(MouseButtons.RIGHT);
    }
    
    public void move(double x, double y) {
        prevPosition = position;
        position = new Point(x, y);
    }

    public void draw() {
        currImage = faceLeft ? LEFT_IMAGE : RIGHT_IMAGE; // NOTE: this is an example of using the ternary operator
        currImage.draw(position.x, position.y);
        UserInterface.drawStats(health, coins, keys, weapon);
    }

    public void earnCoins(double coins) {
        this.coins += coins;
    }

    public void receiveDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            ShadowDungeon.changeToGameOverRoom();
        }
    }

    public Point getPosition() {
        return position;
    }
    public double getSpeed() {
        return speed;
    }
    public double getHealth() {
        return health;
    }
    public double getWeapon() {
        return weapon;
    }
    public double getKeys() {
        return keys;
    }
    public double getCoins() {
        return coins;
    }
    public boolean getFaceLeft() {return faceLeft;}

    public Image getCurrImage() {
        return currImage;
    }




    public Point getPrevPosition() {
        return prevPosition;
    }
    public Point getCurrPosition() { return position;}

    public void  setImage(String image) {
        this.currImage = new Image(image);
    }
}


class mainCharacter extends Player {


}

class Robot extends Player {

//    Player(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys)
    public Robot(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys) {
        super(position, image, speed, health, coins, prevPosition, keys);
//        super.setChosenCharacter("robot");




    }

    @Override
    public void draw() {
        if (getFaceLeft()) {
            setImage("res/robot_left.png");
        } else {
            setImage("res/robot_right.png");

        }

        getCurrImage().draw(getCurrPosition().x, getCurrPosition().y);
        UserInterface.drawStats(getHealth(), getCoins(), getKeys(), getWeapon());
    }

}

class Marine extends Player {
    public Marine(Point position, String image, double speed, double health, double coins, Point prevPosition, double keys) {
        super(position, image, speed, health, coins, prevPosition, keys);


    }

    @Override
    public void draw() {
        if (getFaceLeft()) {
            setImage("res/marine_left.png");
        } else {
            setImage("res/marine_right.png");

        }
        getCurrImage().draw(getCurrPosition().x, getCurrPosition().y);
        UserInterface.drawStats(getHealth(), getCoins(), getKeys(), getWeapon());
    }



}