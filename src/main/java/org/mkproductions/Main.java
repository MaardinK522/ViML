package org.mkproductions;

import org.mkproductions.jnn.network.graphics.NetworkRenderingJFrame;
import org.mkproductions.jnn.network.helper.TinkeringNetHelper;

public class Main {
    public static void main(String[] args) {
        TinkeringNetHelper.getInstance();
        new NetworkRenderingJFrame().startRendering();
    }
}