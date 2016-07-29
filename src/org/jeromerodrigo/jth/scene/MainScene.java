package org.jeromerodrigo.jth.scene;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import org.jeromerodrigo.jth.entity.FakePlayer;
import org.jeromerodrigo.jth.entity.Player;
import org.jeromerodrigo.lucidengine.Camera;
import org.jeromerodrigo.lucidengine.ai.Actor;
import org.jeromerodrigo.lucidengine.audio.OpenALAudio;
import org.jeromerodrigo.lucidengine.audio.OpenALAudioLoader;
import org.jeromerodrigo.lucidengine.audio.OpenALAudioPlayer;
import org.jeromerodrigo.lucidengine.game.AbstractScene;
import org.jeromerodrigo.lucidengine.graphics.SpriteBatch;
import org.jeromerodrigo.lucidengine.graphics.texture.Texture;
import org.jeromerodrigo.lucidengine.graphics.texture.TextureRegionStore;
import org.jeromerodrigo.lucidengine.input.InputManager;
import org.jeromerodrigo.lucidengine.tiledmap.OrthogonalMapRenderer;
import org.jeromerodrigo.lucidengine.tiledmap.TiledMap;
import org.jeromerodrigo.lucidengine.tiledmap.XMLMapLoader;
import org.jeromerodrigo.lucidengine.util.ResourceLoader;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class MainScene extends AbstractScene {
    
    private static final Logger LOG = LogManager.getLogger(MainScene.class);
    
    private Actor              mainCharacter;
    private final World         world;
    private final Camera        camera;
    private final OpenALAudio   backgroundMusic;

    public MainScene(final String name, final SpriteBatch spriteBatch) {
        super(name, spriteBatch);
        
        world = new World();
        final Settings physicsSettings = new Settings();
        physicsSettings.setAutoSleepingEnabled(true);
        physicsSettings.setSleepTime(5.0);
        world.setSettings(physicsSettings);
        world.setGravity(new Vector2(0, 0));
        
        final TiledMap map = XMLMapLoader.INSTANCE
                .loadMap("res/world/level_1.tmx");

        Texture tex = null;

        try {
            tex = new Texture(ResourceLoader.getResource("res/chars.png"));
        } catch (final IOException e) {
            LOG.fatal(e.getMessage());
        }
        
        final int TILE_SZ = 32;
        
        TextureRegionStore.putById("movedown1", tex, 0, 0, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("down", tex, 1, 0, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("movedown2", tex, 2, 0, TILE_SZ, TILE_SZ);

        TextureRegionStore.putById("moveleft1", tex, 0, 1, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("left", tex, 1, 1, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("moveleft2", tex, 2, 1, TILE_SZ, TILE_SZ);

        TextureRegionStore.putById("moveright1", tex, 0, 2, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("right", tex, 1, 2, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("moveright2", tex, 2, 2, TILE_SZ, TILE_SZ);

        TextureRegionStore.putById("moveup1", tex, 0, 3, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("up", tex, 1, 3, TILE_SZ, TILE_SZ);
        TextureRegionStore.putById("moveup2", tex, 2, 3, TILE_SZ, TILE_SZ);

        TextureRegionStore.putById("soldierdown", tex, 4, 4, TILE_SZ, TILE_SZ);

        final Player player = new Player(TextureRegionStore.get("down"), 0, 0);

        // TODO Need to dynamically get display width and height
        camera = new Camera(player, map, 800, 600);
        
        XMLMapLoader.INSTANCE.loadMapToPhysics(map, world);
        
        addPhysicsBody(player);
        
        final FakePlayer fakePlayer = new FakePlayer(
                TextureRegionStore.get("down"), 256, 256);

        addPhysicsBody(fakePlayer);

        final OrthogonalMapRenderer mapRenderer = new OrthogonalMapRenderer(
                map, camera);

        addDrawable(mapRenderer);
        addDrawable(player);
        addUpdateable(player);
        addDrawable(fakePlayer);

        for (int i = 0; i < 5; i++) {
            // TODO Need to dynamically get display width and height
            final FakePlayer f = new FakePlayer(
                    TextureRegionStore.get("soldierdown"),
                    (float) (800 * Math.random()),
                    (float) (600 * Math.random()));
            addDrawable(f);
            addUpdateable(f);
            addControllable(f);
            addPhysicsBody(f);
        }

        addUpdateable(camera);

        setMainCharacter(player);

        backgroundMusic = OpenALAudioLoader.INSTANCE.loadAudio("bgm",
                ResourceLoader
                        .getResource("res/bgm/POL-sacred-temple-short.wav"));
        
        OpenALAudioPlayer.INSTANCE.play(backgroundMusic, true);
        OpenALAudioPlayer.INSTANCE.setVolume(backgroundMusic, 0.2f);
    }
    
    @Override
    public void processInput() {
        InputManager.processInput(mainCharacter);
        super.processInput();
    }

    @Override
    public void render() {
        final Matrix4f view = batch.getViewMatrix();

        // Reset the view matrix
        view.setIdentity();

        // Pan the camera
        view.translate(new Vector2f((float) -getCamera().getX(),
                (float) -getCamera().getY()));

        batch.updateUniforms();
        
        super.render();
    }
    
    @Override
    public void update(final int delta) {
        // Update physics engine
        world.update(delta);
        super.update(delta);
    }
    
    private Camera getCamera() {
        return camera;
    }
    
    protected final void setMainCharacter(final Actor actor) {
        mainCharacter = actor;
    }

    protected final void addPhysicsBody(final Body body) {
        world.addBody(body);
    }

}
