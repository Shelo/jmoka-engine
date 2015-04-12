package com.moka.tests;

import com.moka.components.Sprite;
import com.moka.core.Application;
import com.moka.core.Entity;
import com.moka.core.Transform;
import com.moka.core.contexts.TestContext;
import com.moka.graphics.Texture;
import com.moka.math.Vector2f;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestTransform
{
    @Test
    public void testPosition()
    {
        Entity entity = new Entity(null, null);
        Transform transform = entity.getTransform();
        transform.setPosition(100, 100);
        assertEquals(new Vector2f(100, 100), transform.getPosition());
    }

    @Test
    public void testSize()
    {
        Entity entity = new Entity(null, null);
        Transform transform = entity.getTransform();
        transform.setSize(100, 100);
        assertEquals(new Vector2f(100, 100), transform.getSize());
    }

    @Test
    public void testSizeWithSprite()
    {
        Application application = new Application();
        application.setContext(new TestContext());
        application.getDisplay().createDisplay(200, 200, "JMoka Engine");

        Entity entity = new Entity(null, null);

        Texture dummyText = new Texture(null) {
            @Override
            public float getWidth()
            {
                return 50;
            }

            @Override
            public float getHeight()
            {
                return 50;
            }

            @Override
            public float getTexCoordX()
            {
                return 1;
            }

            @Override
            public float getTexCoordY()
            {
                return 1;
            }
        };

        Sprite sprite = new Sprite();
        sprite.setTexture(dummyText);

        entity.addComponent(sprite);

        // check if the component was added successfully.
        assertTrue(entity.hasSprite());

        Transform transform = entity.getTransform();

        assertEquals(new Vector2f(50, 50), transform.getSize());
    }

    @Test
    public void testSizeWithSpriteOverridden()
    {
        Application application = new Application();
        application.setContext(new TestContext());
        application.getDisplay().createDisplay(200, 200, "JMoka Engine");

        Entity entity = new Entity(null, null);

        Texture dummyText = new Texture(null) {
            @Override
            public float getWidth()
            {
                return 50;
            }

            @Override
            public float getHeight()
            {
                return 50;
            }

            @Override
            public float getTexCoordX()
            {
                return 1;
            }

            @Override
            public float getTexCoordY()
            {
                return 1;
            }
        };

        Sprite sprite = new Sprite();
        sprite.setTexture(dummyText);

        entity.addComponent(sprite);

        Transform transform = entity.getTransform();
        transform.setSize(100, 100);

        assertEquals(new Vector2f(100, 100), transform.getSize());
    }
}
