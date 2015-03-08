package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

import java.util.ArrayList;
import java.util.List;

public class SATCollider extends Collider {
	private Vector2[] vertices;
	private int[] npvi; // Non parallel vertices indexes
	private Vector2[] tVertices; // Transformed vertices
	private Vector2[] axes;

	public SATCollider() {
		
	}

	@Override
	public void onCreate() {
		if(vertices == null)
			setVertices(getEntity().getSprite().getMesh().getVerticesAsVector2());
	}

	public void setVertices(Vector2[] vertices) {
		if (this.vertices != null)
			throw new JMokaException("Vertices already set.");

		if (vertices == null)
			throw new JMokaException("Vertices cannot be null.");

		this.vertices = vertices;

		initAxes();
		updateAxes();
	}

	public void initAxes() {
		if (axes != null)
			throw new JMokaException("Axes already initiated.");

		Vector2[] edges = new Vector2[vertices.length];
		List<Float> slopes = new ArrayList<Float>();
		ArrayList<Integer> npvi = new ArrayList<Integer>();

		for (int i = 0; i < vertices.length; i++)
			edges[i] = vertices[i].sub(vertices[(i == vertices.length - 1) ? 0 : i + 1]);

		for (int i = 0; i < edges.length; i++) {
			float slope = Math.abs(edges[i].y / edges[i].x);

			if (!slopes.contains(slope))
				npvi.add(i);

			slopes.add(slope);
		}

		this.npvi = new int[npvi.size()];

		for (int i = 0; i < npvi.size(); i++)
			this.npvi[i] = npvi.get(i);

		axes = new Vector2[npvi.size()];
	}

	public void updateVertices() {
		tVertices = getEntity().transformVertices(vertices);
	}

	public void updateAxes() {
		updateVertices();

		int k = 0;
		for (int i : npvi) {
			Vector2 v1 = tVertices[i];
			Vector2 v2 = tVertices[(i == tVertices.length - 1) ? 0 : i + 1];
			Vector2 edge = v1.sub(v2);

			// Perpendicular
			float y = edge.y;
			edge.y = 0 - edge.x;
			edge.x = y;

			axes[k] = edge.normalized();
			k++;
		}
	}

	@Override
	public Collision collidesWith(Collider other) {
		if(other instanceof SATCollider) {
			return Collider.sat(this, (SATCollider) other);
		}

		return null;
	}

	@Override
	public void response(Collision collision) {
		getTransform().move(collision.getMovement());
	}

	public Collision collidesWith(CircleCollider circle) {
		return null;
	}

	public Collision collidesWith(AABBCollider rect) {
		return null;
	}

	public Collision collidesWith(SATCollider sat) {
		/*
		if (getEntity().getTransform().hasRotated())
			updateData();

		if (sat.getEntity().getTransform().hasRotated())
			sat.updateData();

		ArrayList<Vector2> axes = new ArrayList<>();

		Collections.addAll(axes, this.axes);

		for (Vector2 axis : sat.axes)
			if (!axes.contains(axis))
				axes.add(axis);

		float overlap = Float.POSITIVE_INFINITY;
		Vector2 smallest = null;

		for (Vector2 axis : axes) {
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

	public Vector2[] getAxes() {
		return axes;
	}

	public Vector2[] getTVertices() {
		return tVertices;
	}

	@XmlAttribute("vertices")
	public void setVertices(String path) {
		
	}
}
