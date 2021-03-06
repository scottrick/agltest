package com.hatfat.agltest;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hatfat.agl.app.AglActivity;
import com.hatfat.agl.component.ComponentType;
import com.hatfat.agl.component.LightComponent;
import com.hatfat.agl.component.transform.Transform;
import com.hatfat.agl.util.AglRandom;

import javax.inject.Inject;

public class TestActivity extends AglActivity implements View.OnTouchListener {

    @Inject
    AglRandom rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final TestScene aglScene = new TestScene(getApplicationContext());
        final BumpMapScene aglScene = new BumpMapScene(getApplicationContext());
//        final TextureScene aglScene = new TextureScene(getApplicationContext());
        aglSurfaceView.setScene(aglScene);

        aglSurfaceView.setOnTouchListener(this);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.base_layout_content_view);
        View ourView = getLayoutInflater().inflate(R.layout.test_activity_layout, container, false);
        container.addView(ourView);

        Button testButton = (Button) ourView.findViewById(R.id.test_activity_layout_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LightComponent lightComponent = getScene().getGlobalLight().getComponentByType(ComponentType.LIGHT);
                lightComponent.lightColor = rand.nextColor();
            }
        });

        Button meshButton = (Button) ourView.findViewById(R.id.test_activity_layout_toggle_button);
        meshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aglScene.nextMesh();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getScene().isPaused()) {
                    getScene().unpause();
                }
                else {
                    getScene().pause();
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                float halfWidth = aglSurfaceView.getWidth() / 2.0f;
                float halfHeight = aglSurfaceView.getHeight() / 2.0f;
                float scale = 1.6f;

                float xValue = (event.getX() - halfWidth) / halfWidth * scale;
                float yValue = (event.getY() - halfHeight) / halfHeight * scale;

                Transform transformComponent = getScene().getGlobalLight().getComponentByType(ComponentType.TRANSFORM);
                transformComponent.posQuat.pos.x = xValue;
                transformComponent.posQuat.pos.y = -yValue;
            }
                break;
        }

        return true;
    }
}
