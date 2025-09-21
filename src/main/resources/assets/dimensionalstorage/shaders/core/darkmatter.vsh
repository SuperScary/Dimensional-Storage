#version 150
in vec3 Position;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 vColor;
out vec3 vWorldPos;

void main() {
    vec4 world = ModelViewMat * vec4(Position, 1.0);
    vWorldPos = world.xyz;
    vColor = Color;
    gl_Position = ProjMat * world;
}