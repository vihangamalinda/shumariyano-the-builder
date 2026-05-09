package com.vihanga.malinda.svmf.listner;

public interface MouseListener {
    void mouseScrollCallBack(long window,
                             double xOffset,
                             double yOffset);

    void mouseButtonCallBack(long window,
                             int button,
                             int action,
                             int mods);

    void mousePositionCallBack(long window,
                               double xPosition,
                               double yPosition);
}
