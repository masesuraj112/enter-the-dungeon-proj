import bagel.Image;
import bagel.util.Point;

public class Key {
    private final Image keyImage = new Image("res/key.png");
    private Point keyPosition;
    private boolean isSelected;

    public Key() {
        isSelected = false;

    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
    public void setKeyPosition(Point position) {
        this.keyPosition = position;
    }

    public void update(Player player) {
        System.out.println(isSelected);
        if (!isSelected) {
            keyImage.draw(this.keyPosition.x, this.keyPosition.y);
            if (keyImage.getBoundingBoxAt(keyPosition).intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()))) {
                System.out.println("inside");
                player.setKeys(player.getKeys() + 1);
                setSelected(true);
            }
            System.out.println(player.getKeys());

        }




    }

}
