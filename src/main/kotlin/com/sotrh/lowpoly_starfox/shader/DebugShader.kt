package com.sotrh.lowpoly_starfox.shader

class DebugShader: Shader(VS_CODE, FS_CODE) {
    companion object {
        val VS_CODE = """
            #version 150 core
            in vec3 position;
            out vec3 color;
            void main(void) {
                gl_Position = vec4(position, 1.0);
                color = vec3(position.x + 0.5, 1.0, position.y + 0.5);
            }
            """

        val FS_CODE = """
            #version 150 core
            in vec3 color;
            out vec4 out_Color;
            void main(void) {
                out_Color = vec4(color, 1.0);
            }
            """
    }

    override fun bindAttributes() {
        bindAttribute(0, "position")
    }
}