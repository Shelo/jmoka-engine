package com.moka.core;

/**
 * The scene is the element of your game that will actually show something.
 */
public abstract class Scene
{
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
}
