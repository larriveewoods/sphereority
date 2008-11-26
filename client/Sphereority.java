package  client;
  
import common.*;
import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import javax.swing.*;
 
/**
 * This is the main client application
 * @author smaboshe
 */
public class Sphereority extends Thread implements Constants {
	// SINGLETONS
	public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);

  public static final String[] MAP_LIST = new String[]
    { "circles", "mercury", "random_1", "sample-map", "widefield" };
  
  private GameEngine game;
    private ClientConnection connection;
  private static JDialog gameWindow;
  private static ClientLogonDialog loginWindow;
  
  public Sphereority(GameEngine game, ClientConnection connection)
  {
    this.game = game;
        this.connection = connection;
  }
  
  public static void main(String[] args) {
    // Create and display the LoginDialog
    //loginWindow = new ClientLogonDialog(null);
    
    // If the user quit the dialog, we must quit
    //if (!loginWindow.show())
    //  System.exit(0);
    // Else play the game
        String serverName = "localhost";
 
        if (args.length == 1) {
            serverName = args[0];
        }    
 
    Map map;
    GameEngine game;
        ClientConnection connection = null;
    do
    {
      // This grabs a random map on startup
      map = new Map(MAP_LIST[4]);
            Random random = new Random();
            byte playerId = (byte) random.nextInt(6);
            System.out.println(playerId);
            game = new GameEngine(map, playerId, "User" + playerId, null);
            try {
                connection = new ClientConnection(InetAddress.getByName(MCAST_ADDRESS),MCAST_PORT,game);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            
      // Set up the game gameWindow
      gameWindow = new JDialog();
      gameWindow.setTitle(CLIENT_WINDOW_NAME);
      gameWindow.setModal(true);
      
      gameWindow.getContentPane().add(game.getGameViewArea(), BorderLayout.CENTER);
      
      gameWindow.pack();
      gameWindow.setLocationRelativeTo(null);
      
            Sphereority s = new Sphereority(game,connection);
            try {
                //connection.loginToServer(serverName,"Bob");  
            }
            catch (Exception ex) {
            }
 
            s.start();
      
      game.registerActionListeners(gameWindow);
      // Play the game once:
      gameWindow.setVisible(true);
      game.unregisterActionListeners(gameWindow);
      
      // Show the login dialog again
    }
    while (loginWindow != null && loginWindow.show());
    // If quit, don't loop
    
    // TEMP: this is for testing only:
    gameWindow.dispose();
    
    System.exit(0);
  }
  
  public void run()
  {
    game.play();
        try {
            connection.Start();
            connection.StartSendingMessages();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
  }
}
