package org.jeromerodrigo.jth.entity;

import org.jeromerodrigo.lucidengine.ai.Controllable;
import org.jeromerodrigo.lucidengine.ai.Controller;
import org.jeromerodrigo.lucidengine.graphics.texture.TextureRegion;

public class FakePlayer extends Player implements Controllable {
    
    public FakePlayer(final TextureRegion initialRegion, final float x,
            final float y) {
        super(initialRegion, x, y);
        
    }
    
    public void accept(final Controller controller) {
        controller.control(this);
        
    }
    
}
