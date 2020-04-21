/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.rphstudio.procjamitchio.States;

import fr.rphstudio.procjamitchio.MainLauncher;
import fr.rphstudio.procjamitchio.rng.Prng;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StateBurger extends BasicGameState {
    public static final int ID = 300;

    public final int  MAIN_OFFSET_X     = 752;
    public final int  MAIN_OFFSET_Y     = 700;
    
    private final int INGR_BREAD_TOP_NB = 4;
    private final int INGR_BREAD_BOT_NB = 5;
    private final int INGR_CHEESE_NB    = 4;
    private final int INGR_SAUCE_NB     = 5;
    private final int INGR_SALAD_NB     = 4;
    private final int INGR_VEGETABLE_NB = 6;
    private final int INGR_MEAT_NB      = 7;
    
    private final int INGR_BREAD_TOP_OF7 = 50;
    private final int INGR_BREAD_BOT_OF7 = 30;
    private final int INGR_CHEESE_OF7    = 15;
    private final int INGR_SAUCE_OF7     = 15;
    private final int INGR_SALAD_OF7     = 25;
    private final int INGR_VEGETABLE_OF7 = 25;
    private final int INGR_MEAT_OF7      = 30;
    
    private final int INGR_NONE             = 0;
    private final int INGR_BREAD_TOP_START  = 1;
    private final int INGR_BREAD_TOP_END    = INGR_BREAD_TOP_START+INGR_BREAD_TOP_NB-1;
    private final int INGR_BREAD_BOT_START  = INGR_BREAD_TOP_END+1;
    private final int INGR_BREAD_BOT_END    = INGR_BREAD_BOT_START+INGR_BREAD_BOT_NB-1;
    private final int INGR_CHEESE_START     = INGR_BREAD_BOT_END+1;
    private final int INGR_CHEESE_END       = INGR_CHEESE_START+INGR_CHEESE_NB-1;
    private final int INGR_SAUCE_START      = INGR_CHEESE_END+1;
    private final int INGR_SAUCE_END        = INGR_SAUCE_START+INGR_SAUCE_NB-1;
    private final int INGR_SALAD_START      = INGR_SAUCE_END+1;
    private final int INGR_SALAD_END        = INGR_SALAD_START+INGR_SALAD_NB-1;
    private final int INGR_VEGETABLE_START  = INGR_SALAD_END+1;
    private final int INGR_VEGETABLE_END    = INGR_VEGETABLE_START+INGR_VEGETABLE_NB-1;
    private final int INGR_MEAT_START       = INGR_VEGETABLE_END+1;
    private final int INGR_MEAT_END         = INGR_MEAT_START+INGR_MEAT_NB-1;
    
    private final int INGR_NB_MIN           = 5;
    private final int INGR_NB_MAX           = 11;
    
    private final int KCAL_BREAD        = 150;
    private final int KCAL_CHEESE       = 80;
    private final int KCAL_SAUCE        = 30;
    private final int KCAL_SALAD        = 25;
    private final int KCAL_VEGETABLE    = 25;
    private final int KCAL_MEAT         = 200;
    
    private final double KCAL_COEF      = 0.2;
    
    private final int    MOVE_NB_BOUNCE   = 2;
    private final double MOVE_DESCENT     = 300.0; // ms
    private final double MOVE_DURATION    = MOVE_DESCENT * (0.5+MOVE_NB_BOUNCE); // ms
    
    private final long   MOVE_HEIGHT      = 50; 
    
    
    
    //------------------------------------------------
    private StateBasedGame gameObject;
    private GameContainer container;

    
    String version;

    // Properties
    Image     table;
    Image     plate;
    Image     bckgnd;
    Animation ingredients;
    int       burgerID;
    Prng      rng;
    int[]     burgerStack;
    int[]     burgerStackOff;
    int       calories;
    int       moveOffset;
    long      moveStartime;
    String    message;
    
    private void getVersion()
    {
        // Get display version
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader("info/version.txt"));
            String line;
            line = br.readLine();
            if(line != null)
            {
                this.version = line;
            }
            if (br != null)
            {
                br.close();
            }
        }
        catch (IOException e){ throw new Error(e); }
        finally
        {
            try{ if (br != null){ br.close(); } }
            catch (IOException ex){ throw new Error(ex); }
        }
    }
    
    //------------------------------------------------
    // CONSTRUCTOR
    //------------------------------------------------
    public StateBurger() {
    }

    //------------------------------------------------
    // INIT METHOD
    //------------------------------------------------
    @Override
    public void init(GameContainer container, StateBasedGame sbGame) throws SlickException
    {
        this.container = container;
        gameObject = sbGame;
    
        // Init properties
        this.burgerID = 1;
        this.rng      = new Prng();
        
        SpriteSheet ss   = new SpriteSheet("./sprites/ingredients.png",416 ,208 );
        this.ingredients = new Animation();
        
        // None ingredient
        this.ingredients.addFrame(ss.getSprite(6,6), 1000);
        // top bread
        this.ingredients.addFrame(ss.getSprite(0,0), 1000);
        this.ingredients.addFrame(ss.getSprite(0,1), 1000);
      //this.ingredients.addFrame(ss.getSprite(0,2), 1000); // To redesign
        this.ingredients.addFrame(ss.getSprite(0,3), 1000);
      //this.ingredients.addFrame(ss.getSprite(0,4), 1000); // to use with eggs only ? Add eggs ?
        this.ingredients.addFrame(ss.getSprite(0,5), 1000);
        // bottom bread
        this.ingredients.addFrame(ss.getSprite(1,0), 1000);
        this.ingredients.addFrame(ss.getSprite(1,1), 1000);
        this.ingredients.addFrame(ss.getSprite(1,2), 1000);
        this.ingredients.addFrame(ss.getSprite(1,3), 1000);
        this.ingredients.addFrame(ss.getSprite(1,4), 1000);
        // Cheese
        this.ingredients.addFrame(ss.getSprite(3,0), 1000);
        this.ingredients.addFrame(ss.getSprite(3,1), 1000);
        this.ingredients.addFrame(ss.getSprite(3,2), 1000);
        this.ingredients.addFrame(ss.getSprite(3,3), 1000);
        // Sauce
        this.ingredients.addFrame(ss.getSprite(6,0), 1000);
        this.ingredients.addFrame(ss.getSprite(6,1), 1000);
        this.ingredients.addFrame(ss.getSprite(6,2), 1000);
        this.ingredients.addFrame(ss.getSprite(6,3), 1000);
        this.ingredients.addFrame(ss.getSprite(6,4), 1000);
        // salad
        this.ingredients.addFrame(ss.getSprite(2,0), 1000);
        this.ingredients.addFrame(ss.getSprite(2,1), 1000);
        this.ingredients.addFrame(ss.getSprite(2,2), 1000);
        this.ingredients.addFrame(ss.getSprite(2,3), 1000);
        // vegetables
        this.ingredients.addFrame(ss.getSprite(5,0), 1000);
        this.ingredients.addFrame(ss.getSprite(5,1), 1000);
        this.ingredients.addFrame(ss.getSprite(5,2), 1000);
        this.ingredients.addFrame(ss.getSprite(5,3), 1000);
        this.ingredients.addFrame(ss.getSprite(5,4), 1000);
        this.ingredients.addFrame(ss.getSprite(5,5), 1000);
        // meat
        this.ingredients.addFrame(ss.getSprite(4,0), 1000);
        this.ingredients.addFrame(ss.getSprite(4,1), 1000);
        this.ingredients.addFrame(ss.getSprite(4,2), 1000);
        this.ingredients.addFrame(ss.getSprite(4,3), 1000);
        this.ingredients.addFrame(ss.getSprite(4,4), 1000);
        this.ingredients.addFrame(ss.getSprite(4,5), 1000);
        this.ingredients.addFrame(ss.getSprite(4,6), 1000);
        
        // background
        this.table  = new Image("./sprites/table.png");
        this.plate  = new Image("./sprites/plate.png");
        this.bckgnd = new Image("./sprites/background.png");
        
        // Create first burger
        this.newBurger();
        
        // Get version string
        this.getVersion();
    }

    
    private int getTopBreadID()
    {
        return (int)(this.rng.random()*INGR_BREAD_TOP_NB)+INGR_BREAD_TOP_START;
    }
    private int getBotBreadID()
    {
        return (int)(this.rng.random()*INGR_BREAD_BOT_NB)+INGR_BREAD_BOT_START;
    }
    private int getCheeseID()
    {
        return (int)(this.rng.random()*INGR_CHEESE_NB)+INGR_CHEESE_START;
    }
    private int getSauceID()
    {
        return (int)(this.rng.random()*INGR_SAUCE_NB)+INGR_SAUCE_START;
    }
    private int getSaladID()
    {
        return (int)(this.rng.random()*INGR_SALAD_NB)+INGR_SALAD_START;
    }
    private int getVegetableID()
    {
        return (int)(this.rng.random()*INGR_VEGETABLE_NB)+INGR_VEGETABLE_START;
    }
    private int getMeatID()
    {
        return (int)(this.rng.random()*INGR_MEAT_NB)+INGR_MEAT_START;
    }
    private int getCalories(int type)
    {
        return type+(int)( type * ((this.rng.random()*KCAL_COEF)-(KCAL_COEF/2)));
    }
    
    
    
    private void nextBurger()
    {
        this.burgerID++;
        this.newBurger();
    }
    private void prevBurger()
    {
        this.burgerID--;
        this.newBurger();
    }
    private int getSeed()
    {
        return ((this.burgerID+1)*123456789)+((this.burgerID+2)*987654321);
    }
    private void initSeed()
    {
        // Compute seed
        int seed = this.getSeed();
        this.rng.setSeed(seed);
    }
    private void newBurger()
    {
        // init seed
        this.initSeed();
        // Get number of Meat and ingredients
        int nbMeat = (int)(this.rng.random()*4)+1;
        int nbIng  = (4*nbMeat)+4;
        if(nbMeat == 3)
        {
            nbIng++;
        }
        // Allocate burger stack
        this.burgerStack    = new int[nbIng];
        this.burgerStackOff = new int[nbIng];
        
        
        int layer    = 0;
        // Fill Basement
        this.burgerStack[layer++] = this.getBotBreadID();
        this.calories = this.getCalories(KCAL_BREAD);
        // Fill meat layers
        for(int m=0;m<nbMeat;m++)
        {
            //----------- salad -----------
            this.burgerStackOff[layer] = INGR_SALAD_OF7;
            this.burgerStack[layer++]  = this.getSaladID();
            this.calories += this.getCalories(KCAL_SALAD);
            //---------- meat -----------
            this.burgerStackOff[layer] = INGR_MEAT_OF7;
            this.burgerStack[layer++]  = this.getMeatID();
            this.calories += this.getCalories(KCAL_MEAT);
            // If bacon, no sauce)
            if(this.burgerStack[layer-1] != (INGR_MEAT_START+2))
            {
                // If sauce, only 1 out of 2
                if(this.rng.random() >= 0.5)
                {
                    this.burgerStackOff[layer] = INGR_SAUCE_OF7;
                    this.burgerStack[layer++]  = this.getSauceID();
                    this.calories += this.getCalories(KCAL_SAUCE);
                }
            }
            else
            {
                // reduce meat offset because bacon is thinner
                this.burgerStackOff[layer-1] /= 2;
            }
            //----------- vegetable or cheese -----------
            if(this.rng.random() >= 0.5)
            {
                this.burgerStackOff[layer] = INGR_VEGETABLE_OF7;
                this.burgerStack[layer++]  = this.getVegetableID();
                this.calories += this.getCalories(KCAL_VEGETABLE);
            }
            else
            {
                this.burgerStackOff[layer] = INGR_CHEESE_OF7;
                this.burgerStack[layer++]  = this.getCheeseID();
                this.calories += this.getCalories(KCAL_CHEESE);
            }
            //----------- additional bread for 3 meat burger -----------
            if(m<=1 && nbMeat>=3)
            {
                this.burgerStackOff[layer] = INGR_BREAD_BOT_OF7;
                this.burgerStack[layer++]  = this.getBotBreadID();
                this.calories += this.getCalories(KCAL_BREAD);
            }
        }
        // top bread
        this.burgerStackOff[layer] = INGR_BREAD_TOP_OF7;
        this.burgerStack[layer++]  = this.getTopBreadID();
        this.calories += this.getCalories(KCAL_BREAD);
    
        // Get time to create the burger
        // Start moving
        this.moveStartime = System.currentTimeMillis();
         
        // Get message
        int idx = 0;
        idx = (int)(this.rng.random()*Testimonials.MESSAGE.length);
        this.message  = "<< " + Testimonials.MESSAGE[idx] + " >>";
        idx = (int)(this.rng.random()*Testimonials.USER.length);
        this.message += "\n\n - " + Testimonials.USER[idx];
        idx = (int)(this.rng.random()*Testimonials.USER.length);
        this.message += " " + Testimonials.USER[idx].charAt(0) + ".";
    }
    
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        
        // Fit Screen
        MainLauncher.fitScreen(container, g);
        
        
        // Draw background
        g.drawImage(this.bckgnd,0,0);
        g.drawImage(this.table,-20,MAIN_OFFSET_Y-100);
        g.drawImage(this.plate,MAIN_OFFSET_X-100,MAIN_OFFSET_Y+35);
        
        // Init seed for random X position
        this.initSeed();
        // Init y offset and display each burger layer
        int offY = MAIN_OFFSET_Y;
        int offX = 0;
        for(int i=0;i<this.burgerStack.length;i++)
        {
            offX  = (int)(this.rng.random()*20)-10;
            offY -= this.burgerStackOff[i];
            g.drawImage(this.ingredients.getImage(this.burgerStack[i]), MAIN_OFFSET_X+offX, offY-(this.moveOffset*(i+1)));
        }
        
        // Draw calorie line + text
        g.setColor(new Color(0.0f,0.0f,0.0f,0.4f));
        int refX = MAIN_OFFSET_X+416;
        int refY = MAIN_OFFSET_Y;
        g.drawLine(refX, refY-50, refX, refY+50);
        g.drawLine(refX, refY, refX+100, refY-100);
        g.drawLine(refX+100, refY-100, refX+200, refY-100);
        g.drawString(Integer.toString(this.calories)+" KCalories", refX+210, refY-110);
        
        // Draw message
        refX = MAIN_OFFSET_X-430;
        refY = MAIN_OFFSET_Y-150;
        g.drawLine(refX, refY-5, refX+250, refY-5);
        g.drawLine(refX, refY-5, refX, refY+25);
        
        g.drawString(this.message, refX,refY);
        
        // Draw burger seed
        g.setColor(Color.black);
        g.drawString("Burger seed = "+ String.format("0x%08X", this.getSeed()), 1920-250, 1080-30);
        
        // Render version number
        g.setColor(Color.black);
        g.drawString(this.version, 0, 1080-30);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {
        // Move burger if needed
        if (this.moveStartime > 0)
        {
            long deltaTime = System.currentTimeMillis()-this.moveStartime;
            if (deltaTime <= MOVE_DURATION)
            {
                this.moveOffset = (int)( Math.abs(Math.cos(Math.PI*deltaTime/MOVE_DESCENT)*MOVE_HEIGHT*(MOVE_DURATION-deltaTime)/MOVE_DURATION) );
            }
            else
            {
                // Stop moving
                this.moveOffset   = 0;
                this.moveStartime = -1;
            }
        }
    }

    @Override
    public void keyPressed(int key, char c)
    {
        
    }


    @Override
    public void keyReleased(int key, char c)
    {
        switch (key)
        {
            case Input.KEY_ESCAPE:
                this.container.exit();
                break;
            case Input.KEY_LEFT:
                this.prevBurger();
                break;
            case Input.KEY_RIGHT:
                this.nextBurger();
                break;
        }
    }

    @Override
    public int getID()
    {
        return ID;
    }
}