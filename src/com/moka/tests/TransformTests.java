package com.moka.tests;

import com.moka.components.Sprite;
import com.moka.core.Application;
import com.moka.core.Entity;
import com.moka.core.Transform;
import com.moka.core.contexts.TestContext;
import com.moka.graphics.Texture;
import com.moka.math.Vector2f;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TransformTests
{
    @Test
    public void positionShouldChange()
    {
        Entity entity = new Entity(null, null);
        Transform transform = entity.getTransform();
        transform.setPosition(100, 100);
        assertThat("Transform's position should change when calling setPosition.",
                new Vector2f(100, 100), is(equalTo(transform.getPosition())));
    }

    @Test
    public void sizeShouldChange()
    {
        Entity entity = new Entity(null, null);
        Transform transform = entity.getTransform();
        transform.setSize(100, 100);
        assertThat("Transform's size should change when calling setSize.",
                new Vector2f(100, 100), is(equalTo(transform.getSize())));
    }

    @Test
    public void sizeShouldChangeGivenASpriteOrOverriding()
    {
        Application application = new Application();
        application.setContext(new TestContext());
        application.getDisplay().createDisplay(200, 200, "JMoka Engine");

        Entity entity = new Entity(null, null);

        // mess the texture in order to send handled values.
        Texture dummyText = new Texture(null) {
            @Override public float getWidth()       { return 50; }
            @Override public float getHeight()      { return 50; }
            @Override public float getTexCoordX()   { return 1; }
            @Override public float getTexCoordY()   { return 1; }
        };

        Sprite sprite = new Sprite();
        sprite.setTexture(dummyText);

        entity.addComponent(sprite);

        // check if the component was added successfully.
        assertTrue(entity.hasSprite());

        Transform transform = entity.getTransform();

        assertThat("The size should be the image's size.",
                new Vector2f(50, 50), is(equalTo(transform.getSize())));

        transform.setSize(100, 100);

        assertThat("The size should be the one that the user set to the transform.",
                new Vector2f(100, 100), is(equalTo(transform.getSize())));
    }
}
