package com.moka.math;

public class Matrix4 {
	private float[][] m;

	public Matrix4() {
		m = new float[4][4];
	}

	public Matrix4 initIdentity() {
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = 0;
		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;
		m[1][3] = 0;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;
		m[2][3] = 0;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;

		return this;
	}

	public Matrix4 toTranslation(float x, float y, float z) {
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = x;

		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;
		m[1][3] = y;

		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;
		m[2][3] = z;

		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;

		return this;
	}

	public Matrix4 initRotation(float x, float y, float z) {
		Matrix4 rx = new Matrix4();
		Matrix4 ry = new Matrix4();
		Matrix4 rz = new Matrix4();

		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);

		rz.m[0][0] = (float) Math.cos(z);
		rz.m[0][1] = -(float) Math.sin(z);
		rz.m[0][2] = 0;
		rz.m[0][3] = 0;
		rz.m[1][0] = (float) Math.sin(z);
		rz.m[1][1] = (float) Math.cos(z);
		rz.m[1][2] = 0;
		rz.m[1][3] = 0;
		rz.m[2][0] = 0;
		rz.m[2][1] = 0;
		rz.m[2][2] = 1;
		rz.m[2][3] = 0;
		rz.m[3][0] = 0;
		rz.m[3][1] = 0;
		rz.m[3][2] = 0;
		rz.m[3][3] = 1;

		rx.m[0][0] = 1;
		rx.m[0][1] = 0;
		rx.m[0][2] = 0;
		rx.m[0][3] = 0;
		rx.m[1][0] = 0;
		rx.m[1][1] = (float) Math.cos(x);
		rx.m[1][2] = -(float) Math.sin(x);
		rx.m[1][3] = 0;
		rx.m[2][0] = 0;
		rx.m[2][1] = (float) Math.sin(x);
		rx.m[2][2] = (float) Math.cos(x);
		rx.m[2][3] = 0;
		rx.m[3][0] = 0;
		rx.m[3][1] = 0;
		rx.m[3][2] = 0;
		rx.m[3][3] = 1;

		ry.m[0][0] = (float) Math.cos(y);
		ry.m[0][1] = 0;
		ry.m[0][2] = -(float) Math.sin(y);
		ry.m[0][3] = 0;
		ry.m[1][0] = 0;
		ry.m[1][1] = 1;
		ry.m[1][2] = 0;
		ry.m[1][3] = 0;
		ry.m[2][0] = (float) Math.sin(y);
		ry.m[2][1] = 0;
		ry.m[2][2] = (float) Math.cos(y);
		ry.m[2][3] = 0;
		ry.m[3][0] = 0;
		ry.m[3][1] = 0;
		ry.m[3][2] = 0;
		ry.m[3][3] = 1;

		m = rz.mul(ry.mul(rx)).getM();

		return this;
	}

	public Matrix4 toScale(float x, float y, float z) {
		m[0][0] = x;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = 0;
		m[1][0] = 0;
		m[1][1] = y;
		m[1][2] = 0;
		m[1][3] = 0;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = z;
		m[2][3] = 0;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;

		return this;
	}

	public Matrix4 initPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float) Math.tan(fov / 2);
		float zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = 0;

		m[1][0] = 0;
		m[1][1] = 1.0f / tanHalfFOV;
		m[1][2] = 0;
		m[1][3] = 0;

		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = (- zNear - zFar) / zRange;
		m[2][3] = 2 * zFar * zNear / zRange;

		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 1;
		m[3][3] = 0;

		return this;
	}

	private Matrix4 initOrthographic(float left, float right, float bottom, float top, float near, float far) {
		float width = right - left;
		float height = top - bottom;
		float depth = far - near;

		m[0][0] = 2 / width;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = -(right + left) / width;

		m[1][0] = 0;
		m[1][1] = 2 / height;
		m[1][2] = 0;
		m[1][3] = -(top + bottom) / height;

		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = -2 / depth;
		m[2][3] = -(far + near) / depth;

		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;

		return this;
	}

	public Matrix4 initRotation(Vector3f forward, Vector3f up, Vector3f right) {
		m[0][0] = right.x;
		m[0][1] = right.y;
		m[0][2] = right.z;
		m[0][3] = 0;
		m[1][0] = up.x;
		m[1][1] = up.y;
		m[1][2] = up.z;
		m[1][3] = 0;
		m[2][0] = forward.x;
		m[2][1] = forward.y;
		m[2][2] = forward.z;
		m[2][3] = 0;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
		return this;
	}

	public Vector3f transform(Vector3f r) {
		return new Vector3f(
				m[0][0] * r.x + m[0][1] * r.y + m[0][2] * r.z + m[0][3],
				m[1][0] * r.x + m[1][1] * r.y + m[1][2] * r.z + m[1][3],
				m[2][0] * r.x + m[2][1] * r.y + m[2][2] * r.z + m[2][3]
		);
	}

	public Matrix4 mul(Matrix4 r) {
		Matrix4 res = new Matrix4();

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res.set(i, j,
						m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j)
								+ m[i][3] * r.get(3, j));

		return res;
	}

	public Matrix4 mul(Matrix4 r, Matrix4 res) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res.set(i, j, m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j)
						+ m[i][3] * r.get(3, j));
		return res;
	}

	public Vector2f mul(Vector2f vector) {
		return new Vector2f(m[0][0] * vector.x + m[0][1] * vector.y + m[0][3],
				m[1][0] * vector.x + m[1][1] * vector.y + m[1][3]);
	}

	public float[][] getM() {
		float[][] res = new float[4][4];

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				res[i][j] = m[i][j];

		return res;
	}

	public float get(int x, int y) {
		return m[x][y];
	}

	public void setM(float[][] m) {
		this.m = m;
	}

	public void set(int x, int y, float value) {
		m[x][y] = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 4; y++)
				builder.append(get(x, y)).append(" ");
			builder.append("\n");
		}

		return builder.toString();
	}

	public static Matrix4 scale(float x, float y, float z) {
		return new Matrix4().toScale(x, y, z);
	}
	
	public static Matrix4 orthographic(float left, float right, float bottom, float top, float zNear, float zFar) {
		return new Matrix4().initOrthographic(left, right, bottom, top, zNear, zFar);
	}
	
	public static Matrix4 translate(float x, float y, float z) {
		return new Matrix4().toTranslation(x, y, z);
	}
}