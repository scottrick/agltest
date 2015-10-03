package com.hatfat.agltest;

import android.content.Context;

import com.hatfat.agl.AglScene;
import com.hatfat.agl.app.AglRenderer;
import com.hatfat.agl.component.PhysicsComponent;
import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.entity.AglEntity;

public class EntityScene extends AglScene {

    public EntityScene(Context context) {
        super(context, true);

        getCamera().getEye().z = 5.0f;
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
