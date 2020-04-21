

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio;

import fr.rphstudio.procjamitchio.States.StateBurger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainLauncher extends StateBasedGame
{
    
    /**
     * Width of the graphic work area.
     */
    public static final int WIDTH  = 1920;

    /**
     * Height of the graphic work area.
     */
    public static final int HEIGHT = 1080;
    
    /**
     * Ratio used when the computer resolution is not HD (1920x1080)
     */
    public static float RATIO_XY = 1.0f;
    
    /**
     * X-axis Offset used when the computer resolution is not HD (1920x1080)
     */
    public static float OFFSET_X = 0.0f;
    
    /**
     * Y-axis Offset used when the computer resolution is not HD (1920x1080)
     */
    public static float OFFSET_Y = 0.0f;
    
    
    
    public static void fitScreen(GameContainer container, Graphics g)
    {
        // rendering is done : now try to scale it to fit the full screen
        float sx  = ((AppGameContainer)container).getScreenWidth()/(float)WIDTH;
        float sy  = ((AppGameContainer)container).getScreenHeight()/(float)HEIGHT;
        MainLauncher.RATIO_XY = Math.min(sx,sy); 
        MainLauncher.OFFSET_X = (((AppGameContainer)container).getScreenWidth() -(WIDTH*MainLauncher.RATIO_XY))/2;
        MainLauncher.OFFSET_Y = (((AppGameContainer)container).getScreenHeight()-(HEIGHT*MainLauncher.RATIO_XY))/2;
        // Scale and translate to the center of screen
        g.translate(MainLauncher.OFFSET_X, MainLauncher.OFFSET_Y);
        g.scale(MainLauncher.RATIO_XY, MainLauncher.RATIO_XY);
        // Set black color for the background
        g.setBackground(Color.black);
    }
    
    public static void main(String[] args) throws SlickException
    {
        // Full game HD
        AppGameContainer appGame = new AppGameContainer(new MainLauncher());
        appGame.setDisplayMode(appGame.getScreenWidth(), appGame.getScreenHeight(), true);
        appGame.start();
    }  
    
    public MainLauncher()
    {
        super("Proc Jam ITCH.IO - RPH Studio - November 2017");
    }
     
    @Override
    public void initStatesList(GameContainer container) throws SlickException
    {
        // Remove or Display FPS
        container.setShowFPS(true);
        // Modify Title and Icon
        AppGameContainer appContainer = (AppGameContainer) container;
        appContainer.setVSync(true);
           
        // First set icons
        if(!container.isFullscreen())
        {
            String[] icons = { "icon32x32.png", "icon16x16.png"};
            container.setIcons(icons);
        }
        
        // Add Main menu state
        this.addState(new StateBurger() );
        
    }
  
}

