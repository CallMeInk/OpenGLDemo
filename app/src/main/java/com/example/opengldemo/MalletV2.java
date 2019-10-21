package com.example.opengldemo;

import java.util.List;

public class MalletV2 {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public MalletV2(float radius, float height, int numPointsAroundMallet){
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f, 0f, 0f),
                radius,
                height,
                numPointsAroundMallet
        );

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;

    }

    public void bindData(ColorShaderProgram colorShaderProgram){
        vertexArray.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0
        );
    }

    public void draw(){
        for (ObjectBuilder.DrawCommand drawCommand : drawList){
            drawCommand.draw();
        }
    }

}
