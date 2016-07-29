package org.jeromerodrigo.jth;

import java.io.IOException;
import java.util.Properties;

import org.jeromerodrigo.lucidengine.util.ResourceLoader;
import org.lwjgl.LWJGLException;

final class Start {

    /**
     * @param args
     * @throws IOException
     * @throws LWJGLException
     * @throws NumberFormatException
     */
    public static void main(final String[] args) throws IOException,
    NumberFormatException, LWJGLException {

        final Properties prop = new Properties();
        prop.load(ResourceLoader.getResourceAsStream("engine.properties"));

        final JTHEngine engine = new JTHEngine(prop);
        engine.start(new JourneyToHeliosGame());
    }

}
