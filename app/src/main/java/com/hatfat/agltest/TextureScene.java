package com.hatfat.agltest;

import android.content.Context;

import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.base.AglScene;
import com.hatfat.agl.base.systems.TransformModifierSystem;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.ModifierComponent;
import com.hatfat.agl.component.RenderableComponent;
import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.mesh.TestRenderableFactory;
import com.hatfat.agl.modifiers.SpinModifier;
import com.hatfat.agl.render.AglTexturedGeometry;
import com.hatfat.agl.util.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TextureScene extends AglScene {

    private List<AglEntity> textureEntities   = new ArrayList<>();
    private int             activeEntityIndex = 0;

    public TextureScene(Context context) {
        super(context, true);

        addSystem(new TransformModifierSystem());

        getCamera().getEye().z = 4.0f;
    }

    @Override protected void setupSceneBackgroundWork() {
        super.setupSceneBackgroundWork();
    }

    @Override protected void setupSceneGLWork(AglRenderer renderer) {
        super.setupSceneGLWork(renderer);

        Random rand = new Random();

        Vec3 spinVec = new Vec3(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        spinVec.normalize();

        List<String> materials = new LinkedList<>();
        materials.add("01");
        materials.add("05");
        materials.add("113");
        materials.add("133");

        for (String material : materials) {
            AglTexturedGeometry renderable = TestRenderableFactory.createTextureCube(
                    renderer.getTextureManager().getTexture("pattern_" + material + "_diffuse"),
                    true);

            AglEntity entity = new AglEntity("Texture Entity " + material);

            RenderableComponent renderableComponent = new RenderableComponent(renderable);
            renderableComponent.setShouldRender(false);
            Transform transformComponent = new Transform();
            ModifierComponent modifierComponent = new ModifierComponent();
            modifierComponent.addModifier(new SpinModifier(24.0f, spinVec));

            entity.addComponent(renderableComponent);
            entity.addComponent(transformComponent);
            entity.addComponent(modifierComponent);

            textureEntities.add(entity);
            addEntity(entity);
        }

        RenderableComponent renderableComponent = textureEntities.get(0).getComponentByType(
                ComponentType.RENDERABLE);
        renderableComponent.setShouldRender(true);
    }

    public void nextMesh() {
        RenderableComponent oldComponent = textureEntities.get(activeEntityIndex).getComponentByType(ComponentType.RENDERABLE);
        oldComponent.setShouldRender(false);

        activeEntityIndex = (activeEntityIndex + 1) % textureEntities.size();

        RenderableComponent newComponent = textureEntities.get(activeEntityIndex).getComponentByType(ComponentType.RENDERABLE);
        newComponent.setShouldRender(true);
    }
}
