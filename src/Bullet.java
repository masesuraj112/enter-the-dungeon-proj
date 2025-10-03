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
        System.out.println("inside update");
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

    public void moveBullet(Input input) {
//        System.out.println("inside movebullet");
////        if (this.isPresent == true) {
////            this.bulletImage.draw(this.position.x, this.position.y);
////        }
////        input.getMouseX()
//        Vector2 startVector = new Vector2(position.x, position.y);
//        Vector2 endVector = new Vector2(input.getMouseX(), input.getMouseY());
//        Vector2 direction = endVector.sub(startVector).normalised();
//        Vector2 newPosition = startVector.add(direction.mul(10));
//        Point newPoint = newPosition.asPoint();
//        position = newPoint;
////        int secondsPassed = 0;
////        while (secondsPassed < 5) {
////            try
////            Thread.sleep(1000);
////            secondsPassed ++;
////
////        }
////        System.out.println(newPoint.x + " is x");
//        System.out.println(newPoint.y + " is y");
//
//        if (newPoint.x >= 1024 || newPoint.y >= 768) {
//            System.out.println("inside false");
//            setPresent(false);
//        }
//        if (this.isPresent)  {
//            System.out.println("is true");
//            bulletImage.draw(newPoint.x, newPoint.y);
//        }
////
//         }
//        bulletImage.draw(newPoint.x, newPoint.y);


//        int num = 0;
//        Point newPoint = new Point(0, 0);
//        // don't use while loop it uses too much memory
//        while (this.isPresent == true) {
//            Vector2 newPosition = startVector.add(direction.mul(num));
//            newPoint = newPosition.asPoint();
//            if (newPoint.x >= 1024 || newPoint.y >= 768) {
//                break;
//            }
//            bulletImage.draw(newPoint.x, newPoint.y);
//
//            num += 10;
//
//        }


        // get position of player
        // get position of mouse
        // work out the direction
        // make a method that moves based on the direction




    }

    

}
