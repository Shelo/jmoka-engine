package com.moka.components;

import com.moka.core.ComponentAttribute;
import com.moka.math.Vector2;
import com.moka.physics.Collider;
import com.moka.physics.Collision;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.List;

public class SatCollider extends Collider
{
	private Vector2[] vertices;
	private int[] npvi; // Non parallel vertices indexes
	private Vector2[] tVertices; // Transformed vertices
	private Vector2[] axes;

	public SatCollider()
	{
		
	}

	@Override
	public void onCreate()
	{
		if (vertices == null)
		{
			if (entity().hasSprite())
			{
				setVertices(entity().getSprite().getMesh().getVerticesAsVector2());
			}
		}
	}

	public void setVertices(Vector2[] vertices)
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

		Vector2[] edges = new Vector2[vertices.length];
		List<Float> slopes = new ArrayList<>();
		ArrayList<Integer> npvi = new ArrayList<>();

		Vector2 buffer = new Vector2();

		for (int i = 0; i < vertices.length; i++)
		{
			edges[i] = new Vector2(vertices[i]);
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

		axes = new Vector2[npvi.size()];

		for (int i = 0; i < axes.length; i++)
		{
			axes[i] = new Vector2();
		}
	}

	public void updateVertices()
	{
		tVertices = entity().transformVertices(vertices);
	}

	public void updateAxes()
	{
		updateVertices();

		int k = 0;
		for (int i : npvi)
		{
			Vector2 v1 = new Vector2(tVertices[i]);
			Vector2 v2 = tVertices[(i == tVertices.length - 1) ? 0 : i + 1];
			Vector2 edge = v1.sub(v2);

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

	public Vector2[] getAxes()
	{
		return axes;
	}

	public Vector2[] getTVertices()
	{
		return tVertices;
	}

	@ComponentAttribute("vertices")
	public void setVertices(String path)
	{
		
	}
}
