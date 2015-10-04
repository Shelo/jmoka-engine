package com.moka.scene;

import com.moka.core.SubEngine;
import com.moka.prefabs.OpingPrefabReader;
import com.moka.prefabs.PrefabReader;
import com.moka.utils.JMokaException;

import java.util.ArrayList;

/**
 * The context is the main game state handler. It serves as a scene
 * manager, and controls every change in states between them.
 */
public class Context extends SubEngine
{
    private PrefabReader prefabReader = new OpingPrefabReader();

    /**
     * Store all scenes.
     */
    private final ArrayList<Scene> scenes = new ArrayList<>();
    private int currentScene;

    public void create()
    {
        getCurrentScene().load(this);
    }

    public void update()
    {
        getCurrentScene().onUpdate();
        getCurrentScene().update();
    }

    public void postUpdate()
    {
        getCurrentScene().postUpdate();
    }

    /**
     * Clean destroyed objects.
     */
    public void clean()
    {
        getCurrentScene().clean();
    }

    /**
     * Dispose all scenes in order to close the application.
     */
    public void dispose()
    {
        for (Scene scene : scenes)
            scene.dispose();
    }

    public void addScene(Scene scene)
    {
        scenes.add(scene);
    }

    public void setMainScene(Class<? extends Scene> sceneClass)
    {
        for (int i = 0; i < scenes.size(); i++)
        {
            Scene scene = scenes.get(i);
            if (sceneClass.isInstance(scene))
            {
                log("Loading main scene: " + scene.getClass().getSimpleName());

                // switch to the new scene.
                currentScene = i;
                return;
            }
        }
    }

    public void loadScene(Class<? extends Scene> sceneClass, boolean exitPrevious)
    {
        if (!application.isCreated())
            throw new JMokaException("Can't load scenes until the engine starts.");

        for (int i = 0; i < scenes.size(); i++)
        {
            Scene scene = scenes.get(i);
            if (sceneClass.isInstance(scene))
            {
                log("Loading scene: " + scene.getClass().getSimpleName());

                // unload the previous scene.
                Scene previousScene = getCurrentScene();
                exitScene(previousScene, exitPrevious);

                // switch to the new scene.
                currentScene = i;
                getCurrentScene().load(this);

                return;
            }
        }

        // the scene could not be found. Error!.
        throw new JMokaException("Scene does not exists in the context.");
    }

    public Scene getScene(Class<? extends Scene> sceneClass)
    {
        for (Scene scene : scenes)
            if (sceneClass.isInstance(scene))
                return scene;

        // the scene could not be found. Error!.
        throw new JMokaException("Scene does not exists in the context.");
    }

    private void exitScene(Scene scene, boolean exit)
    {
        if (exit)
        {
            scene.onExit();
            scene.dispose();
            scene.destroy();
            scene.setCreated(false);
        }
        else
        {
            scene.onLeave();
        }
    }

    /**
     * Returns the current scene.
     * @return  the current scene.
     */
    public Scene getCurrentScene()
    {
        return scenes.get(currentScene);
    }

    public int getEntitiesCount()
    {
        return getCurrentScene().getEntitiesCount();
    }

    public PrefabReader getPrefabReader()
    {
        return prefabReader;
    }
}
