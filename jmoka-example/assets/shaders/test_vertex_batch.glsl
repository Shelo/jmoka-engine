#version 330 core

layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_texCoord;
layout (location = 2) in vec4 a_color;

uniform mat3 u_projectedView;

out vec4 color;
out float out_position;
out vec2 texCoord;

void main() {
    vec3 position = u_projectedView * vec3(a_position, 1.0);
    gl_Position = vec4(position.xy, 0, position.z);

    color = a_color;
    texCoord = a_texCoord;
}