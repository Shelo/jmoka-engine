jMoka Engine
============

The jMoka Engine is an Open Source 2D Game Engine with a Component-Entity based system. Its goal is to give the game
programmer a fast way of getting things done. The engine features a strong java framework that works
hand in hand with file definitions for prefabs, since we know that creating an entity and adding components to it
is nothing out of the world, why should we use java code for this?

Documentation pages: [http://shelo.github.io/jmoka-docs/](http://shelo.github.io/jmoka-docs/)

## Project Structure.

A jMoka project always have this scheme in order to work fast. Note that nothing is a strict requirement (at the time).

* Setup
* Scenes
* Resources
* Components

### Setup

The setup is the main class of your project, it will simply tell the engine to create it self, set some project
settings, such as clear color, display dimensions, extensions, among others.

The setup has the important role of being the front face for your game, it will hold the current hierarchy, all the
scenes...

### Scenes

The scene is the one that will actually show something on the screen. Here you can use all parts of the engine, since
it all is initialized and ready to start. The scene has a life cycle, it has four methods (at the moment), that you
should have to take care of: onCreate, onLeave, onResume, onExit, each one has its own porpoise.

Early in the engine, the scene was set with an XML file, but that did not helped a lot since some things had to be
repeated a lot, and with Java you can do them with simple loop.

### Resources

The jMoka Engine offers a nice way to handle your assets in the most optimal way, without repeating yourself,
you have to simply extend a class and you get some helper methods for loading textures, prefabs, sounds, etc.

What is a prefab?, A prefab is simply a definition of an entity in your game, the engine offers this because
there's no need to waste java code in doing this, since is a very, very repetitive and easy task, shouldn't we
leave it to a easy, readable text file?.

The engine offers an extensible class to manage your prefabs and take care of their initialization so you don't have
to worry when actually getting to the game.

### Components

Components are a part of the behaviour of an entity. The engine is built around this concept.

Components are distributed in extensions, that way you can create a very common, let's say, platformer extensions,
and put there all sorts of components to help you in any platformer-like game, and reuse them in all projects!.
## The Prefab Scheme

In construction...


# The Architecture of the Engine

The jMoka Engine is constructed as a fully OOP software. Is useful to know that the engine itself has no singletons,
but, since is useful for code simplification, there exists the Moka class, which inside is a Singleton, but has
static method to get all the application things.

