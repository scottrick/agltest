package com.hatfat.agltest;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.LightComponent;
import com.hatfat.agl.component.ModifierComponent;
import com.hatfat.agl.component.RenderableComponent;
import com.hatfat.agl.component.Transform;
import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.mesh.AglBBMesh;
import com.hatfat.agl.mesh.TestRenderableFactory;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.render.AglRenderable;
import com.hatfat.agl.util.Vec3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestScene extends AglScene {

    private Random rand;

    private AglBBMesh[] bbMeshes;

    private List<AglEntity> meshEntities;
    private int             activeEntityIndex = 0;


    int numTestNodes = 7;

    public TestScene(Context context) {
        super(context);

        rand = new Random();

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 4.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 100.0f);

        setCamera(camera);
    }

    @Override protected void setupSceneBackgroundWork() {
        super.setupSceneBackgroundWork();

        meshEntities      = new ArrayList<>();

        if (bbMeshes == null) {
            bbMeshes = new AglBBMesh[numTestNodes];

            for (int i = 0; i < numTestNodes; i++) {
                String resourceName = "mesh" + (i + 1);
                int resId = getContext().getResources()
                        .getIdentifier(resourceName, "raw", getContext().getPackageName());
                InputStream in = getContext().getResources().openRawResource(resId);

                try {
                    bbMeshes[i] = AglBBMesh.readFromStreamAsBytes(in);
                } catch (IOException e) {
                    Log.e("TestScene", "Error loading BB mesh resources.");
                }
            }
        }
    }

    @Override
    protected void setupSceneGLWork(AglRenderer renderer) {
        super.setupSceneGLWork(renderer);

        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1.0f, 1.0f);

        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec.normalize();

        float rotateSpeed = 30.0f;

        for (int i = 0; i < numTestNodes; i++) {
            AglBBMesh shapeMesh = bbMeshes[i];

            if (shapeMesh != null) {
                AglRenderable wireframeRenderable = shapeMesh.toWireframeRenderable();
                AglRenderable coloredRenderable = shapeMesh.toColoredGeometryRenderable();

                AglEntity meshEntity = new AglEntity("mesh entity " + String.valueOf(i));
                meshEntity.addComponent(new RenderableComponent(wireframeRenderable));
                meshEntity.addComponent(new RenderableComponent(coloredRenderable));
                meshEntity.addComponent(new Transform());

                ModifierComponent modifierComponent = new ModifierComponent();
                modifierComponent.addModifier(new SpinModifier(rotateSpeed, spinVec));

                meshEntity.addComponent(modifierComponent);

                meshEntities.add(meshEntity);
                addEntity(meshEntity);
            }
        }

        updateMeshes();

        //TEST LIGHT ENTITY STUFF
        removeEntity(globalLightEntity);

        globalLightEntity = new AglEntity("hatfat global light");
        globalLightEntity.addComponent(new LightComponent());

        globalLightEntity.addComponent(new RenderableComponent(TestRenderableFactory.createIcosahedronWireframe()));
        Transform lightTransform = new Transform(new Vec3(0.0f, 0.0f, 1.75f));
        lightTransform.setScale(new Vec3(0.05f, 0.05f, 0.05f));
        globalLightEntity.addComponent(lightTransform);
        addEntity(globalLightEntity);
    }

    @Override
    public void destroyScene(AglRenderer renderer) {
        super.destroyScene(renderer);

        GLES20.glPolygonOffset(0.0f, 0.0f);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
    }

    public void nextMesh() {
        activeEntityIndex = (activeEntityIndex + 1) % meshEntities.size();
        updateMeshes();
    }

    private void updateMeshes() {
        for (AglEntity entity : meshEntities) {
            List<RenderableComponent> renderableComponents = entity.getComponentsByType(ComponentType.RENDERABLE);

            for (RenderableComponent renderableComponent : renderableComponents) {
                renderableComponent.setShouldRender(meshEntities.indexOf(entity) == activeEntityIndex);
            }
        }
    }
}
