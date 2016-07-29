package org.jeromerodrigo.jth.entity;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.geometry.Mass.Type;
import org.dyn4j.geometry.Vector2;
import org.jeromerodrigo.lucidengine.ai.Actor;
import org.jeromerodrigo.lucidengine.animation.Animation;
import org.jeromerodrigo.lucidengine.animation.Frame;
import org.jeromerodrigo.lucidengine.entity.AbstractEntity;
import org.jeromerodrigo.lucidengine.graphics.texture.TextureRegion;
import org.jeromerodrigo.lucidengine.graphics.texture.TextureRegionStore;

public class Player extends AbstractEntity implements Actor {

    private static final int DECELERATION      = 5;

    private static final int IMPULSE_MAGNITUDE = 5000;

    private static final int MAX_SPD           = 250;

    private static final int SIZE              = 32;

    private static final int ANIM_SPD          = 15;

    protected static float   moveSpeed;

    protected Vector2        movement;

    private final Animation  up;
    private final Animation  down;
    private final Animation  left;
    private final Animation  right;

    public Player(final TextureRegion initialRegion, final float x,
            final float y) {
        super(initialRegion, x, y, SIZE, SIZE);

        movement = new Vector2(0.0, 0.0);

        final List<Frame> upFrames = new ArrayList<Frame>();
        upFrames.add(new Frame(ANIM_SPD, TextureRegionStore.get("moveup1")));
        upFrames.add(new Frame(ANIM_SPD, TextureRegionStore.get("moveup2")));
        up = new Animation(getSprite(), upFrames);

        final List<Frame> rightFrames = new ArrayList<Frame>();
        rightFrames.add(new Frame(ANIM_SPD, TextureRegionStore
                .get("moveright1")));
        rightFrames.add(new Frame(ANIM_SPD, TextureRegionStore
                .get("moveright2")));
        right = new Animation(getSprite(), rightFrames);

        final List<Frame> downFrames = new ArrayList<Frame>();
        downFrames.add(new Frame(ANIM_SPD, TextureRegionStore
                .get("movedown1")));
        downFrames.add(new Frame(ANIM_SPD, TextureRegionStore
                .get("movedown2")));
        down = new Animation(getSprite(), downFrames);

        final List<Frame> leftFrames = new ArrayList<Frame>();
        leftFrames.add(new Frame(ANIM_SPD, TextureRegionStore
                .get("moveleft1")));
        leftFrames.add(new Frame(ANIM_SPD, TextureRegionStore
                .get("moveleft2")));
        left = new Animation(getSprite(), leftFrames);

        putAnimation(up);
        putAnimation(right);
        putAnimation(down);
        putAnimation(left);
    }

    @Override
    protected Type getMassType() {
        return Type.NORMAL;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public void move(final Vector2 movement) {

        final Vector2 vVector = getLinearVelocity();

        this.movement = movement;

        // Apply deceleration when not moving in a component of velocity
        if (movement.x == 0) {

            if (vVector.x > 0) {
                vVector.subtract(DECELERATION, 0);

                if (vVector.x < 0) {
                    vVector.x = 0;
                }
            } else if (vVector.x < 0) {
                vVector.subtract(-DECELERATION, 0);

                if (vVector.x > 0) {
                    vVector.x = 0;
                }
            }
        }

        if (movement.y == 0) {
            if (vVector.y > 0) {
                vVector.subtract(0, DECELERATION);

                if (vVector.y < 0) {
                    vVector.y = 0;
                }

            } else if (vVector.y < 0) {
                vVector.subtract(0, -DECELERATION);

                if (vVector.y > 0) {
                    vVector.y = 0;
                }
            }
        }

        applyImpulse(new Vector2(movement.x * IMPULSE_MAGNITUDE, movement.y
                * IMPULSE_MAGNITUDE));

        // Speed Limiter
        if (vVector.x > MAX_SPD) {
            vVector.x = MAX_SPD;
        } else if (vVector.x < -MAX_SPD) {
            vVector.x = -MAX_SPD;
        }

        if (vVector.y > MAX_SPD) {
            vVector.y = MAX_SPD;
        } else if (vVector.y < -MAX_SPD) {
            vVector.y = -MAX_SPD;
        }
    }

    public void say(final String message) {

        // TODO Implement pop up text dialogs

    }

    public void update(final int delta) {
        
        if (movement.x > 0) {
            setSelectedAnimation(right);
        } else if (movement.x < 0) {
            setSelectedAnimation(left);
        }

        if (movement.y > 0) {
            setSelectedAnimation(down);
        } else if (movement.y < 0) {
            setSelectedAnimation(up);
        }

        if (movement.isZero()) {

            if (getSelectedAnimation() == right) {
                getSprite().setTexRegion(TextureRegionStore.get("right"));
            }

            if (getSelectedAnimation() == left) {
                getSprite().setTexRegion(TextureRegionStore.get("left"));
            }

            if (getSelectedAnimation() == up) {
                getSprite().setTexRegion(TextureRegionStore.get("up"));
            }

            if (getSelectedAnimation() == down) {
                getSprite().setTexRegion(TextureRegionStore.get("down"));
            }

            setSelectedAnimation(null);
        }

        // Update animation
        getSprite().update(delta);
    }

}
