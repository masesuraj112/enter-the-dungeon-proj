import bagel.Font;
import bagel.Window;
import bagel.util.Point;
import bagel.Image;

/** Helper methods to display information for the player
 */
public class UserInterface {
    private static final int DIVISOR = 2;

    /** This method constantly renders all four player stats
     * @param coins inputs the number of coins that a player has
     * @param health inputs the current health of a player
     * @param keys  inputs the number of keys that a player has
     * @param weapon inputs current weapon level of players
     */
    public static void drawStats(double health, double coins, double keys, double weapon) {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        drawData(String.format("%s %.1f", ShadowDungeon.getMessageProps().getProperty("healthDisplay"), health), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("healthStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("coinDisplay"), coins), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("coinStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("keyDisplay"), keys), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("keyStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("weaponDisplay"), weapon), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("weaponStat")));
    }
    /** This method draws the starting messages of the game
     */
    public static void drawStartMessages() {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        drawTextCentered("title", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
        drawTextCentered("moveMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("moveMessage.y")));
        drawTextCentered("selectMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("selectMessage.y")));
        drawData(String.format("%s", ShadowDungeon.getMessageProps().getProperty("robotDescription")), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("robotMessage")));
        drawData(String.format("%s", ShadowDungeon.getMessageProps().getProperty("marineDescription")), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("marineMessage")));
        new Image("res/robot_sprite.png").draw(IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Robot")).x, IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Robot")).y);
        new Image("res/marine_sprite.png").draw(IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Marine")).x, IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Marine")).y);
    }
    /** This method draws an ending message depending on if a player has won or not
     * @param win this inputs if a player has won the game or not
     */
    public static void drawEndMessage(boolean win) {
        drawTextCentered(win ? "gameEnd.won" : "gameEnd.lost", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
    }
    /** This helper method centers the text
     * @param textPath inputs path from the message.properties file
     * @param fontSize inputs specific font size
     * @param posY inputs the y coordinate
     */
    public static void drawTextCentered(String textPath, int fontSize, double posY) {
        Font font = new Font("res/wheaton.otf", fontSize);
        String text = ShadowDungeon.getMessageProps().getProperty(textPath);
        double posX = (Window.getWidth() - font.getWidth(text)) / DIVISOR;
        font.drawString(text, posX, posY);
    }
    /** This helper method draws individual text
     * @param data inputs specific Strings that need to be drawn
     * @param fontSize inputs specific font size
     * @param location inputs the coordinates of where the text should be rendered
     */
    public static void drawData(String data, int fontSize, Point location) {
        Font font = new Font("res/wheaton.otf", fontSize);
        font.drawString(data, location.x, location.y);
    }
}
