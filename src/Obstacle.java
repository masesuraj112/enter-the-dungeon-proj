import bagel.*;
import bagel.util.Point;

public class Obstacle {
    private Image obstacleImage;
    private Point currentPosition;
    public Obstacle(Point point) {
        this.currentPosition = point;
    }
    public Obstacle() {

    }

    public void setObstacleImage(Image obstacleImage) {
        this.obstacleImage = obstacleImage;
    }
}

class Table extends Obstacle {
    public Table(Point point) {
        super(point);
        setObstacleImage(new Image("res/table.png"));


    }

}

class Basket extends Obstacle {
    public Basket(Point point) {
        super(point);
        setObstacleImage(new Image("res/table.png"));


    }

}
