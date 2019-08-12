package com.drakosha.augmentedr_4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.EnumSet;
import java.util.Set;

public class CustomArFragment extends ArFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FrameLayout frameLayout = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);

        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        return frameLayout;
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = new Config(session);
        config.setAugmentedFaceMode(Config.AugmentedFaceMode.MESH3D);
        this.getArSceneView().setupSession(session);


        return config;
    }

    @Override
    protected Set<Session.Feature> getSessionFeatures() {
        return EnumSet.of(Session.Feature.FRONT_CAMERA);
    }
}
