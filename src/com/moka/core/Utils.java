package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Utils {
	public static FloatBuffer genBuffer(Vertex[] vertices) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);

		for(Vertex vertex : vertices) {
			buffer.put(vertex.getX());
			buffer.put(vertex.getY());
			buffer.put(vertex.getZ());
			buffer.put(vertex.getS());
			buffer.put(vertex.getT());
		}

		return (FloatBuffer) buffer.flip();
	}

	public static IntBuffer genBuffer(int[] indices) {
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);

		for(int index : indices)
			buffer.put(index);

		return (IntBuffer) buffer.flip();
	}
	
	public static String readFile(String filePath) {
		BufferedReader reader;
		StringBuilder builder;
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			builder = new StringBuilder();

			String line;
			while((line = reader.readLine()) != null)
				builder.append(line).append("\n");

			reader.close();
			return builder.toString();
		} catch(FileNotFoundException e) {
			throw new JMokaException("File " + filePath + " not found.");
		} catch(IOException e) {
			throw new JMokaException("IOException when reading file " + filePath + ".");
		}
	}

	public static FloatBuffer genBuffer(Matrix4 matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				buffer.put(matrix.get(j, i));

		return (FloatBuffer) buffer.flip();
	}

	public static String getExtensionFrom(String filePath) {
		String[] s = filePath.split("\\.(?=[^\\.]+$)");
		return s[s.length - 1];
	}
}
