#version 330

uniform sampler2D u_texture;
uniform vec4 u_color;

in vec2 texCoord;

void main() {
	vec4 baseColor = texture2D(u_texture, texCoord);

	if(baseColor.a == 0)
		discard;
	else
		gl_FragColor = baseColor * u_color;
}
