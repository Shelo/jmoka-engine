#version 330

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_texCoord;

uniform mat4 u_projectedView;
uniform mat4 u_model;

out vec2 texCoord;

void main() {
	gl_Position = u_projectedView * (u_model * vec4(a_position, 1.0));

	// out.
	texCoord = a_texCoord;
}