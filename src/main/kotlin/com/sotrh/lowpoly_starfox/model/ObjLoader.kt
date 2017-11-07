package com.sotrh.lowpoly_starfox.model

import com.sotrh.lowpoly_starfox.file.FileLoader
import org.joml.Vector2f
import org.joml.Vector3f

/**
 * Created by benjamin on 11/6/17
 */
class ObjLoader {

    private data class FaceVertex(val v: Int, val vt: Int, val vn: Int)

    fun loadObjWithTextureAndNormals(filename: String, modelLoader: ModelLoader): Model {
        val vertexList = mutableListOf<Vector3f>()
        val normalList = mutableListOf<Vector3f>()
        val texCoordList = mutableListOf<Vector2f>()
        val faceVertexList = mutableListOf<FaceVertex>()

        FileLoader.getResourceReader(filename).use {
            var line = it.readLine()

            // get the vertex data
            while (line != null) {
                val components = line.split(" ")
                when (components[0]) {
                    "v" -> {
                        vertexList.add(Vector3f(
                                components[1].toFloat(),
                                components[2].toFloat(),
                                components[3].toFloat()
                        ))
                    }
                    "vn" -> {
                        normalList.add(Vector3f(
                                components[1].toFloat(),
                                components[2].toFloat(),
                                components[3].toFloat()
                        ))
                    }
                    "vt" -> {
                        texCoordList.add(Vector2f(
                                components[1].toFloat(),
                                components[2].toFloat()
                        ))

                    }
                    "f" -> {
                        arrayOf(
                                components[1].split("/"),
                                components[2].split("/"),
                                components[3].split("/")
                        ).forEach {
                            faceVertexList.add(FaceVertex(
                                    it[0].toInt(), it[1].toInt(), it[2].toInt()
                            ))
                        }
                    }
                }
                line = it.readLine()
            }
        }

        // create the vertex data
        val floatsPerVertex = 3 + 3 + 2
        val vertexData = FloatArray(faceVertexList.size * floatsPerVertex)
        faceVertexList.forEachIndexed { index, faceVertex ->
            var position = index * floatsPerVertex
            println("position = $position")
            println("faceVertex = $faceVertex")
            vertexData[position++] = vertexList[faceVertex.v - 1].x
            vertexData[position++] = vertexList[faceVertex.v - 1].y
            vertexData[position++] = vertexList[faceVertex.v - 1].z
            vertexData[position++] = normalList[faceVertex.vn - 1].x
            vertexData[position++] = normalList[faceVertex.vn - 1].y
            vertexData[position++] = normalList[faceVertex.vn - 1].z
            vertexData[position++] = texCoordList[faceVertex.vt - 1].x
            vertexData[position] = 1 - texCoordList[faceVertex.vt - 1].y
        }

        return modelLoader.loadNormalTexturedModel(vertexData)
    }
}