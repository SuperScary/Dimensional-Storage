#version 150
uniform sampler2D Noise0;
uniform sampler2D Noise1;
uniform float GameTime;
uniform vec3  CameraPos;

in vec4 vColor;
in vec3 vWorldPos;

out vec4 fragColor;

void main() {
    // screen-space-ish parallax: depend on camera offset
    vec3 rel = vWorldPos - CameraPos;
    float t = GameTime * 0.02;

    // Two scrolling layers with different scales to fake depth
    vec2 uv0 = rel.xz * 0.45 + vec2(t, -t*0.7);
    vec2 uv1 = rel.xy * 0.90 + vec2(-t*0.6, t*0.3);

    float n0 = texture(Noise0, uv0).r;
    float n1 = texture(Noise1, uv1).g;

    float depth = clamp(n0*0.6 + n1*0.5, 0.0, 1.0);

    // “absorb light”: ignore lightmap completely; output very dark with subtle movement
    float alpha = 0.95; // slightly translucent helps the shimmer read
    vec3 color = mix(vec3(0.0), vec3(0.02), depth); // near-black with faint depth

    // Optional edge darkening to feel like a void
    float vignette = 1.0 - smoothstep(0.0, 0.1, length(rel)*0.0);
    fragColor = vec4(color * vignette, alpha) * vColor.a;
}