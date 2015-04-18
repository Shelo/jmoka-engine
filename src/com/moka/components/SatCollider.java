package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import com.moka.physics.Collider;
import com.moka.physics.Collision;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.List;

public class SatCollider extends Collider
{
	private Vector2f[] vertices;
	private int[] npvi; // Non parallel vertices indexes
	private Vector2f[] tVertices; // Transformed vertices
	private Vector2f[] axes;

	public SatCollider()
	{
		
	}

	@Override
	public void onCreate()
	{
		if (vertices == null)
		{
			if (getEntity().hasSprite())
			{
				setVertices(getEntity().getSprite().getMesh().getVerticesAsVector2());
			}
		}
	}

	public void setVertices(Vector2f[] vertices)
	{
		if (this.vertices != null)
		{
			throw new JMokaException("Vertices are already set.");
		}

		if (vertices == null)
		{
			throw new JMokaException("Vertices cannot be null.");
		}

		this.vertices = vertices;

		initAxes();
		updateAxes();
	}

	public void initAxes()
	{
		if (axes != null)
		{
			throw new JMokaException("Axes already initiated.");
		}

		Vector2f[] edges = new Vector2f[vertices.length];
		List<Float> slopes = new ArrayList<>();
		ArrayList<Integer> npvi = new ArrayList<>();

		Vector2f buffer = new Vector2f();

		for (int i = 0; i < vertices.length; i++)
		{
			edges[i] = new Vector2f(vertices[i]);
			edges[i].sub(vertices[(i + 1) % vertices.length]);
		}

		for (int i = 0; i < edges.length; i++)
		{
			float slope = Math.abs(edges[i].y / edges[i].x);

			if (!slopes.contains(slope))
			{
				npvi.add(i);
			}

			slopes.add(slope);
		}

		this.npvi = new int[npvi.size()];

		for (int i = 0; i < npvi.size(); i++)
		{
			this.npvi[i] = npvi.get(i);
		}

		axes = new Vector2f[npvi.size()];

		for (int i = 0; i < axes.length; i++)
		{
			axes[i] = new Vector2f();
		}
	}

	public void updateVertices()
	{
		tVertices = getEntity().transformVertices(vertices);
	}

	public void updateAxes()
	{
		updateVertices();

		int k = 0;
		for (int i : npvi)
		{
			Vector2f v1 = new Vector2f(tVertices[i]);
			Vector2f v2 = tVertices[(i == tVertices.length - 1) ? 0 : i + 1];
			Vector2f edge = v1.sub(v2);

			// Perpendicular
			float y = edge.y;
			edge.y = 0 - edge.x;
			edge.x = y;

			axes[k].set(edge.cpy().nor());
			k++;
		}
	}

	@Override
	public Collision collidesWith(Collider other)
	{
		if (other instanceof SatCollider)
		{
			return Collider.sat(this, (SatCollider) other);
		}

		return null;
	}

	@Override
	public void response(Collision collision)
	{
		getTransform().move(collision.getMovement());
	}

	public Collision collidesWith(CircleCollider circle)
	{
		return null;
	}

	public Collision collidesWith(AABBCollider rect)
	{
		return null;
	}

	public Collision collidesWith(SatCollider sat)
	{
		/*
		if (getEntity().getTransform().hasRotated())
			updateData();

		if (sat.getEntity().getTransform().hasRotated())
			sat.updateData();

		ArrayList<Vector2f> axes = new ArrayList<>();

		Collections.addAll(axes, this.axes);

		for (Vector2f axis : sat.axes)
			if (!axes.contains(axis))
				axes.add(axis);

		float overlap = Float.POSITIVE_INFINITY;
		Vector2f smallest = null;

		for (Vector2f axis : axes) {
			Projection p1 = new Projection(tVertices, axis);
			Projection p2 = new Projection(sat.tVertices, axis);

			if (!p1.overlaps(p2)) {
				return null;
			} else {
				float o = p1.getOverlap(p2);

				if (Math.abs(o) < Math.abs(overlap)) {
					overlap = o;
					smallest = axis;
				}
			}
		}

		return (smallest != null) ? new Collision(sat.getEntity(), smallest, overlap) : null;
		*/
		return null;
	}

	public Vector2f[] getAxes()
	{
		return axes;
	}

	public Vector2f[] getTVertices()
	{
		return tVertices;
	}

	@XmlAttribute("vertices")
	public void setVertices(String path)
	{
		
	}
}