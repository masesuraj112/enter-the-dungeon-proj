import bagel.Image;
import bagel.*;
import bagel.util.Point;
/** Store class which enables purchasing of health or weapons or re-starting the game
 */
public class Store {
    private static final Image storeImage = new Image("res/store.png");
    private static boolean isOpen = false;
    private static final int DIVISOR = 2;

    /** This is a constructor of the Store class
     */
    public Store() {
        isOpen = false;
    }
    /** This method operates the Store by rendering it
     * when it is open and closing the store when the store is not active
     * @param input inputs keyboard logic
     */
    public static void operateStore(Input input) {
        if (input.wasPressed(Keys.SPACE) && !isOpen) {
            isOpen = true;
        } else if (input.wasPressed(Keys.SPACE) && isOpen) {
            isOpen = false;
        }
        // renders the store
        if (isOpen) {
            storeImage.draw(Double.parseDouble(ShadowDungeon.gameProps.getProperty("window.width")) / DIVISOR, Double.parseDouble(ShadowDungeon.gameProps.getProperty("window.height")) / DIVISOR);
            // restarts the game if P key is pressed
         if (input.wasPressed(Keys.P)) {
             ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
             isOpen = false;
         }
        }
    }
}