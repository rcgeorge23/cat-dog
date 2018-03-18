package com.novinet.catdog.neuralnet;

import java.util.ArrayList;
import java.util.List;

public class NetworkTopology {
    private List<Layer> layers;

    public NetworkTopology() {
        this.layers = new ArrayList<>();
    }

    public NetworkTopology addLayer(Layer layer) {
        this.layers.add(layer);
        return this;
    }

    public List<Layer> getLayers() {
        return layers;
    }
}
