jMoka Engine
============

The jMoka Engine is a 2D Game Engine with a Component-Entity based system. Its goal is to give the game
programmer a fast way of getting things done. It uses XML to organize scenes as well as entities and
prefabricated entities (which are technically the same) and global resources.

### An example of a XML Scene file

```xml
<scene xmlns="http://www.mokadev.com/scene">
    <entity name="MainCamera" position="0, 0" rotation="0" layer="0">
        <Camera />
        <example.components.Background />
    </entity>

    <entity position="${@screen_width}, ${@screen_height / 2}" rotation="0" name="Player" layer="2">
        <Sprite texture="res/images/player.png" />
        <example.components.Shooter />
        <Controllable topSpeed="500" acceleration="600" />
        <LookTowardsMouse />
    </entity>
</scene>
```

Note here that we use a grammar: `http://www.mokadev.com/scene`. (The domain name is just for now).

### An example of a XML Resources file

```xml
<values xmlns="http://www.mokadev.com/resources">
    <string>
        <res name="image_grass1" value="res/images/grass1.png" />
        <res name="image_grass2" value="res/images/grass2.png" />
        <res name="image_moka" value="res/images/moka.png" />
    </string>

    <integer>
        <res name="screen_width" value="1024" />
        <res name="screen_height" value="760" />
        <res name="layer_sky" value="-1" />
        <res name="some_value" value="45" />
        <res name="position_x" value="200" />
    </integer>
</values>
```

Note here that we use a grammar: `http://www.mokadev.com/resources`. (The domain name is just for now).

### Java API: the main method.

A game will always use this lines to start off:

```java
public static void main(String[] args)
{
    Application application = new Application();

    application.setContext(new XmlGame("res/scene/scene.xml", "res/values.xml"));

    // set the display size. The display supports resources names as dimensions.
    application.getDisplay().createDisplay("screen_width", "screen_height", "JMoka Engine");

    application.create().start(60);
}
```

This creates the application wrapper (Application), sets the context (the game context), and then
the programmer will have space to customize the engine as she/he wants. Finally create and start at 60 FPS!

### More!

There's more about the engine, like expression (${@screen_width + 2}), Entity references, components references and
more. (There's a complete Java API if you don't really like the XML way too).

### Future

This little project of mine will grow of course, but before I have to really think in a way of optimizing memory.
A first approach is to give a build option that will optimize the XML code into Java Code. But more on that latter :).

#### TODOs

- Create a JavaContext.
- Separate the context into JavaContext and XmlContext.
- Optimize matrix calculations.
- Optimize Quaternions.