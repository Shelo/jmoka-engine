package com.moka.physics;

import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Entity;
import com.moka.triggers.Trigger;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

public abstract class PhysicsBody extends Component
{
    private Trigger<Collision> collisionTrigger;
    private Body body;
    private BodyDef bodyDefinition;
    private FixtureDef fixture;
    private Shape shape;
    private Shapes shapeType;
    private Vector2 size;

    private float friction = 0.3f;
    private float density = 1;
    private float restitution = 0;
    private int maskBits = '\uffff';
    private int categoryBits = 1;

    public enum Shapes
    {
        BOX,
        CIRCLE,
        POLYGON,
    }

    @Override
    public void onCreate()
    {
        // select the shape.
        shapeAs();

        // create the body definition.
        bodyDefinition = new BodyDef();
        bodyDefinition.position = new Vec2(toPos(getTransform().getPosition().x),
                toPos(getTransform().getPosition().y));
        bodyDefinition.angle = getTransform().getLookAngle();
        defineBody(bodyDefinition);

        // create the fixture.
        fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = density;
        fixture.friction = friction;
        fixture.restitution = restitution;
        fixture.userData = this;
        fixture.filter.maskBits = maskBits;
        fixture.filter.categoryBits = categoryBits;
        defineFixture(fixture);

        body = Moka.getApplication().getPhysics().add(this);
    }

    protected abstract void defineFixture(FixtureDef fixture);

    protected abstract void defineBody(BodyDef bodyDefinition);

    public void sync()
    {

    }

    public void fixedUpdate()
    {
        getTransform().setPosition(toPixel(body.getPosition().x), toPixel(body.getPosition().y));
        getTransform().setRotation(body.getAngle());
    }

    private void shapeAs()
    {
        if (size == null)
        {
            raise("The body needs dimensions.");
        }

        if (shapeType == null)
        {
            raise("Shape Type missing.");
        }

        switch (shapeType)
        {
            case BOX:
            {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(toPos(size.x) / 2, toPos(size.y) / 2);
                this.shape = shape;
            } break;
            case CIRCLE:
            {
                CircleShape shape = new CircleShape();
                shape.m_radius = toPos(size.x);
                this.shape = shape;
            } break;
            case POLYGON:
                raise("Polygon not supported yet.");
                break;
        }
    }

    public void onCollide(Entity other, Contact contact)
    {
        if (collisionTrigger != null)
        {
            callTrigger(collisionTrigger, new Collision(other, contact));
        }
    }

    protected static float toPixel(float v)
    {
        return v * 50;
    }

    protected static float toPos(float v)
    {
        return v / 50;
    }

    public Body getBody()
    {
        return body;
    }

    public BodyDef getBodyDefinition()
    {
        return bodyDefinition;
    }

    public FixtureDef getFixture()
    {
        return fixture;
    }

    public void setLinearVelocity(float x, float y)
    {
        body.setLinearVelocity(new Vec2(x, y));
    }

    /**
     * Sets the current angular velocity of this body.
     *
     * @param radians   the radius in radians.
     */
    public void setAngularVelocity(float radians)
    {
        body.setAngularVelocity(radians);
    }

    /**
     * Sets the shape of this body.
     * BOX: a simple rectangle.
     * CIRCLE: a simple circle.
     * POLYGON: any polygon.
     *
     * @param shape the type of the shape.
     */
    @ComponentAttribute(value = "Shape", required = true)
    public void setShape(Shapes shape)
    {
        this.shapeType = shape;
    }

    /**
     * Used in case this body is a box.
     *
     * @param width     width of the box.
     * @param height    height of the box.
     */
    @ComponentAttribute("Size")
    public void setSize(float width, float height)
    {
        this.size = new Vector2(width, height);
    }

    /**
     * Used in case this body is a circle.
     *
     * @param radius    radius of the circle.
     */
    @ComponentAttribute("Radius")
    public void setRadius(float radius)
    {
        size = new Vector2(radius, 0);
    }

    @ComponentAttribute("Density")
    public void setDensity(float density)
    {
        this.density = density;
    }

    @ComponentAttribute("Restitution")
    public void setRestitution(float restitution)
    {
        this.restitution = restitution;
    }

    @ComponentAttribute("Friction")
    public void setFriction(float friction)
    {
        this.friction = friction;
    }

    @ComponentAttribute("OnCollide")
    public void setOnCollide(Trigger<Collision> trigger)
    {
        this.collisionTrigger = trigger;
    }

    @ComponentAttribute("MaskBits")
    public void setMaskBits(int maskBits)
    {
        this.maskBits = maskBits;
    }

    @ComponentAttribute("CategoryBits")
    public void setCategoryBits(int categoryBits)
    {
        this.categoryBits = categoryBits;
    }

    @Override
    public void onDestroy()
    {
        Moka.getPhysics().destroy(this);
    }
}
