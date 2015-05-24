package com.hatfat.agltest;

import android.content.Context;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglNode;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.AglTexturedGeometry;
import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.mesh.TestRenderableFactory;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.util.Vec3;

import java.util.Random;

public class TestTextureScene extends AglScene {

    public TestTextureScene(Context context) {
        super(context);

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 30.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 500.0f);

        setCamera(camera);
    }

    @Override protected void setupSceneBackgroundWork() {
        super.setupSceneBackgroundWork();

    }

    @Override protected void setupSceneGLWork(AglRenderer renderer) {
        super.setupSceneGLWork(renderer);

        Random rand = new Random();

        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                spinVec.normalize();

                float rotateSpeed = rand.nextFloat() * 180.0f;

                AglTexturedGeometry texturedGeometry = TestRenderableFactory.createTextureCube(renderer.getTextureManager(), rand.nextBoolean());
                AglNode texturedNode = new AglNode(new Vec3(x * 4.0f, y * 4.0f, 0.0f),
                        texturedGeometry);
                texturedNode.addModifier(new SpinModifier(rotateSpeed, spinVec));
                addNode(texturedNode);
            }
        }
    }
}
