package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2;
import com.moka.physics.Collider;
import com.moka.physics.Collision;
import com.moka.physics.Projection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SATCollider extends Collider {
	private Vector2[] vertices;
	private int[] npvi; // Non parallel vertices indexes
	private Vector2[] tVertices; // Transformed vertices
	private Vector2[] axes;

	public SATCollider() {}

	public void setVertices(Vector2[] vertices) {
		if (this.vertices != null)
			throw new JMokaException("Vertices already set");

		this.vertices = vertices;
		initAxes();

		updateData();
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
			float slope = Math.abs(edges[i].getY() / edges[i].getX());

			if (!slopes.contains(slope))
				npvi.add(i);

			slopes.add(slope);
		}

		this.npvi = new int[npvi.size()];

		for (int i = 0; i < npvi.size(); i++)
			this.npvi[i] = npvi.get(i);

		axes = new Vector2[npvi.size()];
	}

	@XmlAttribute("vertices")
	public void setVertices(String path) {
	}

	private void updateData() {
		tVertices = getEntity().transformVertices(vertices);

		int k = 0;
		for (int i : npvi) {
			Vector2 v1 = tVertices[i];
			Vector2 v2 = tVertices[(i == tVertices.length - 1) ? 0 : i + 1];
			Vector2 edge = v1.sub(v2);

			// Perpendicular
			float y = edge.getY();
			edge.setY(0 - edge.getX());
			edge.setX(y);

			axes[k] = edge.normalized();
			k++;
		}
	}

	@Override
	public Collision collidesWith(CircleCollider circle) {
		return null;
	}

	@Override
	public Collision collidesWith(AABBCollider rect) {
		return null;
	}

	@Override
	public Collision collidesWith(SATCollider sat) {
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
	}
}
