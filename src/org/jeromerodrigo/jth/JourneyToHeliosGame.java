package org.jeromerodrigo.jth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeromerodrigo.jth.scene.MainScene;
import org.jeromerodrigo.lucidengine.game.AbstractGame;
import org.jeromerodrigo.lucidengine.graphics.SpriteBatch;
import org.lwjgl.LWJGLException;

public class JourneyToHeliosGame extends AbstractGame {

    private static final Logger LOG = LogManager
            .getLogger(JourneyToHeliosGame.class);

    public JourneyToHeliosGame() {
        super();
        
        SpriteBatch batch = null;
        
        try {
            batch = new SpriteBatch();
        } catch (final LWJGLException e) {
            LOG.fatal(e);
        }
        
        final MainScene scene = new MainScene("mainScene", batch);
        putScene(scene);
        setCurrentScene(scene.getName());
    }

    public String getName() {
        return "JourneyToHelios";
    }

}
