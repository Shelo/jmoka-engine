Simple Docs
===========

## Collision Components

A collider can be solid or just trigger, the first will block every other solid collider to overlap it, and call a
trigger, if set, so the client can act, but the second, will just call the trigger, giving to it a Collision object.

If the trigger is set, the component can send something to it, and also, the trigger can return something, as feedback,
in order to make things simple, the return is always a simple Object, the component will have to find out if the
object can be used in some way.

### AABB



### SAT



### Circle


