package com.drakosha.augmentedr_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ArFragment fragment;
    private Button cube;
    private Button sphere;
    private Button cylinder;

    private enum ObjectType {
        CUBE,
        SPHERE,
        CYLINDER
    }

    ObjectType objectType = ObjectType.CUBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        cube = findViewById(R.id.cube);
        cube.setOnClickListener(view -> objectType = ObjectType.CUBE);

        sphere = findViewById(R.id.sphere);
        sphere.setOnClickListener(view -> objectType = ObjectType.SPHERE);

        cylinder = findViewById(R.id.cylinder);
        cylinder.setOnClickListener(view -> objectType = ObjectType.CYLINDER);

        fragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            switch (objectType) {
                case CUBE:
                    createCube(hitResult.createAnchor());
                    break;
                case SPHERE:
                    createSphere(hitResult.createAnchor());
                    break;
                case CYLINDER:
                    createCylinder(hitResult.createAnchor());
                    break;
                default:
                    break;
            }
        });
    }

    private void createCylinder(Anchor anchor) {

        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.rgb(255, 99, 71)))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable = ShapeFactory.makeCylinder(0.1f, 0.2f, new Vector3(0, 0, 0), material);

                    placeModel(modelRenderable, anchor);
                });

    }

    private void createSphere(Anchor anchor) {

        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.rgb(175, 238, 238)))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable = ShapeFactory.makeSphere(0.1f, new Vector3(0, 0, 0), material);

                    placeModel(modelRenderable, anchor);
                });

    }

    private void createCube(Anchor anchor) {

        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.rgb(250, 128, 114)))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable = ShapeFactory.makeCube(new Vector3(0.1f, 0.1f, 0.1f), new Vector3(0, 0, 0), material);

                    placeModel(modelRenderable, anchor);
                });

    }

    private void placeModel(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode node = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(fragment.getTransformationSystem());
        transformableNode.setParent(node);
        transformableNode.setRenderable(modelRenderable);
        fragment.getArSceneView().getScene().addChild(node);
        transformableNode.select();
    }
}
