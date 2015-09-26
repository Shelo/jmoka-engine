package com.moka.scene;

import com.moka.components.Camera;
import com.moka.components.Sprite;
import com.moka.scene.entity.Entity;
import com.moka.graphics.Texture;

/**
 * The scene is the element of your game that will actually show something.
 */
public abstract class Scene
{
    private Context context;
    private boolean created;

    /**
     * Called the first time the scene is called to play it self.
     */
    public abstract void onCreate();

    /**
     * Called when the game decides that it will show another scene, but not totally exiting the game nor
     * destroying this particular scene.
     */
    public abstract void onLeave();

    /**
     * Called when the game shows this scene the first time or the scene is shown and it wasn't destroyed.
     */
    public abstract void onResume();

    /**
     * Called when the game either stops or it decides that this scene should be destroyed.
     */
    public abstract void onExit();

    /**
     * Creates a new {@link Entity} with a camera on it.
     *
     * @param name      name for the getEntity.
     * @param main      sets this camera as the main one.
     * @return          the entity.
     */
    public Entity newCamera(String name, boolean main)
    {
        Entity entity = context.newEntity(name, 0);
        Camera camera = new Camera(0, context.getDisplay().getWidth(), 0, context.getDisplay().getHeight());

        entity.addComponent(camera);

        if (main)
            camera.setAsMain();

        return entity;
    }

    /**
     * Constructs a new {@link Entity} with a sprite component and add it to the hierarchy.
     *
     * @return the new {@link Entity} with a sprite.
     */
    public final Entity newEntity(String name, int layer, Texture texture)
    {
        Entity entity = context.newEntity(name, layer);
        entity.addComponent(new Sprite(texture));
        return entity;
    }

    /**
     * Constructs a new {@link Entity} with a sprite component and add it to the hierarchy.
     *
     * @return the new {@link Entity} with a sprite.
     */
    public final Entity newEntity(String name, int layer)
    {
        return context.newEntity(name, layer);
    }

    public void setCreated(boolean created)
    {
        this.created = created;
    }

    public boolean isCreated()
    {
        return created;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }
}
