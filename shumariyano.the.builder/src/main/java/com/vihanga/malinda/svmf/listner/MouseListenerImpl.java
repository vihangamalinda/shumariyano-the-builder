package com.vihanga.malinda.svmf.listner;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListenerImpl implements MouseListener {

    private double scrollX;
    private double scrollY;
    private double currentXPosition;
    private double currentYPosition;
    private double previousXPosition;
    private double previousYPosition;
    private boolean mouseButtonPressed[];
    private boolean isDragging;

    public MouseListenerImpl(double scrollX,
                             double scrollY,
                             double currentXPosition,
                             double currentYPosition,
                             double previousXPosition,
                             double previousYPosition,
                             boolean[] mouseButtonPressed,
                             boolean isDragging) {
//        this.scrollX = 0.0;
//        this.scrollY = 0.0;
//        this.xPosition = 0.0;
//        this.yPosition = 0.0;
//        this.previousXPosition = 0.0;
//        this.previousYPosition = 0.0;
//        this.mouseButtonPressed = new boolean[3];
//        this.isDragging = false;
        this.scrollX = scrollX;
        this.scrollY = scrollY;
        this.currentXPosition = currentXPosition;
        this.currentYPosition = currentYPosition;
        this.previousXPosition = previousXPosition;
        this.previousYPosition = previousYPosition;
        this.mouseButtonPressed = mouseButtonPressed;
        this.isDragging = isDragging;

    }
    @Override
    public void mousePositionCallBack(long window,
                                      double xPosition,
                                      double yPosition) {
        updatePosition(xPosition,
                       yPosition);
        updateDraggingBehaviour();
    }

    private void updateDraggingBehaviour() {
        boolean isAnyMouseButtonPressed = this.mouseButtonPressed[0] || this.mouseButtonPressed[1] || this.mouseButtonPressed[2];

        if (isAnyMouseButtonPressed) {
            this.startDragging();
        } else {
            this.stopDragging();
        }

    }

    @Override
    public void mouseButtonCallBack(long window,
                                    int button,
                                    int action,
                                    int mods) {
        if (action == GLFW_PRESS) {
            this.mouseButtonPressed[button] = true;
            this.startDragging();

        } else if (action == GLFW_RELEASE) {
            this.mouseButtonPressed[button] = false;
            this.stopDragging();
        }
    }

    @Override
    public void mouseScrollCallBack(long window,
                                    double xOffset,
                                    double yOffset) {
        this.scrollX = xOffset;
        this.scrollY = yOffset;
    }

//    @Override
    public void endFrame() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.previousXPosition = currentXPosition;
        this.previousYPosition = currentYPosition;
    }

    private void updatePosition(double xPosition,
                                double yPosition) {
        this.updatePreviousPosition(this.currentXPosition,
                                    this.currentYPosition);
        this.updateCurrentPosition(xPosition,
                                   yPosition);


    }


    private void stopDragging() {
        this.setDragging(false);
    }

    private void startDragging() {
        this.setDragging(true);
    }

    private void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    private void updateCurrentPosition(double xPosition,
                                      double yPosition) {
        this.setCurrentXPosition(xPosition);
        this.setCurrentYPosition(yPosition);
    }

    private void updatePreviousPosition(double xPosition,
                                        double yPosition) {
        this.setPreviousXPosition(xPosition);
        this.setPreviousYPosition(yPosition);
    }

    private void setPreviousXPosition(double previousXPosition) {
        this.previousXPosition = previousXPosition;
    }

    private void setPreviousYPosition(double previousYPosition) {
        this.previousYPosition = previousYPosition;
    }

    private void setCurrentXPosition(double currentXPosition) {
        this.currentXPosition = currentXPosition;
    }

    private void setCurrentYPosition(double currentYPosition) {
        this.currentYPosition = currentYPosition;
    }

    public double getCurrentPositionX() {
        return this.currentXPosition;
    }

    public double getCurrentPositionY() {
        return this.currentYPosition;
    }

    public double getPreviousPositionX() {
        return this.previousXPosition;
    }

    public double getPreviousPositionY() {
        return this.previousYPosition;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public double getDeltaX() {
        return this.previousXPosition - this.currentXPosition;
    }

    public double getDeltaY() {
        return this.previousYPosition - this.currentYPosition;
    }

    public boolean isMouseButtonPressed(int button) {
        boolean isInvalidButtonIndex = button < 0 || button >= this.mouseButtonPressed.length;
        if (isInvalidButtonIndex) {
            throw new IllegalArgumentException("Invalid mouse button index: " + button);
        }
        return this.mouseButtonPressed[button];
    }
}
