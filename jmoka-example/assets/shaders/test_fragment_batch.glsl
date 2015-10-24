#version 330 core

uniform sampler2D u_texture;

in vec2 texCoord;
in vec4 color;

out vec4 fragColor;

void main() {
    vec4 baseColor = texture(u_texture, texCoord);
    fragColor = baseColor * color;
}
