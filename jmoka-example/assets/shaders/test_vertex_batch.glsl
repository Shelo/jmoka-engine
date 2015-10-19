#version 330 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_texCoord;

uniform mat3 u_projectedView;
uniform mat3 u_model;

out float out_position;
out vec2 texCoord;

void main() {
    vec3 position = u_projectedView * (u_model * vec3(a_position, 1.0));
    gl_Position = vec4(position.xy, 0, position.z);

    texCoord = a_texCoord;
}