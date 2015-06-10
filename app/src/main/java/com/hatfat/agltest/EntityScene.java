package com.hatfat.agltest;

import android.content.Context;

import com.hatfat.agl.AglCamera;
import com.hatfat.agl.AglPerspectiveCamera;
import com.hatfat.agl.AglScene;
import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.component.PhysicsComponent;
import com.hatfat.agl.component.Transform;
import com.hatfat.agl.entity.AglEntity;
import com.hatfat.agl.util.Vec3;

public class EntityScene extends AglScene {

    public EntityScene(Context context) {
        super(context);

        AglCamera camera = new AglPerspectiveCamera(
                new Vec3(0.0f, 0.0f, 5.0f),
                new Vec3(0.0f, 0.0f, 0.0f),
                new Vec3(0.0f, 1.0f, 0.0f),
                60.0f, 1.0f, 0.1f, 100.0f);

        setCamera(camera);
    }

    @Override protected void setupSceneBackgroundWork() {
        super.setupSceneBackgroundWork();

        AglEntity entity1 = new AglEntity("HATFAT ENTITY");
        entity1.addComponent(new PhysicsComponent());
        entity1.addComponent(new Transform());

        AglEntity entity2 = new AglEntity(null);
        entity2.addComponent(new Transform());

        AglEntity entity3 = new AglEntity();

        System.out.println(entity2.toString());
        System.out.println(entity3.toString());
        System.out.println(entity1.toString());
    }

    @Override
    protected void setupSceneGLWork(AglRenderer renderer) {
        super.setupSceneGLWork(renderer);
    }
}
