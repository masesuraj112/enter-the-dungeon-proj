import bagel.*;
import bagel.util.Point;

abstract public class Obstacle {
    private Image obstacleImage;
    private Point currentPosition;
    private boolean isActive;
    public Obstacle(Point point) {
        this.currentPosition = point;
    }
    public Obstacle() {

    }


    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setObstacleImage(Image obstacleImage) {
        this.obstacleImage = obstacleImage;
    }

    public Image getObstacleImage() {
        return obstacleImage;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    abstract public void update();

    public void collideWithPlayer(Player player) {
        if (player.getCurrImage().getBoundingBoxAt(player.getPosition()).intersects(obstacleImage.getBoundingBoxAt(currentPosition))) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);

        }
    }


}

class Table extends Obstacle {
    public Table(Point point) {
        super(point);
        setObstacleImage(new Image("res/table.png"));

    }

    public void update() {
        if (isActive()) {
            getObstacleImage().draw(getCurrentPosition().x, getCurrentPosition().y);
        }
    }

}


class Basket extends Obstacle {
    public Basket(Point point) {
        super(point);
        setObstacleImage(new Image("res/basket.png"));

    }
    public void update() {
        System.out.println("e");
    }

}
