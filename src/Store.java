import bagel.Image;
import bagel.*;
import bagel.util.Point;

public class Store {
    private static final Image storeImage = new Image("res/store.png");
    private static boolean isOpen = false;
    public Store() {
        isOpen = false;
    }

    public static void operateStore(Input input) {
        System.out.println(isOpen);
        if (input.wasPressed(Keys.SPACE) && !isOpen) {
            isOpen = true;
        } else if (input.wasPressed(Keys.SPACE) && isOpen) {
            isOpen = false;
        }

        if (isOpen) {
            storeImage.draw(Double.parseDouble(ShadowDungeon.gameProps.getProperty("window.width")) / 2, Double.parseDouble(ShadowDungeon.gameProps.getProperty("window.height")) / 2);

         if (input.wasPressed(Keys.P)) {
             ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
             isOpen = false;
         }




        }
    }



    public static void changePlayerStats(Player player) {

    }


}