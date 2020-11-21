package com.darkgran.farstar.battle;

public interface WorldSettings {
    //Technical
    float FPS = 60.0f;
    float STEP_TIME = 1f / FPS;
    int VELOCITY_ITERATIONS = 15;
    int POSITION_ITERATIONS = 12;
    float WORLD_WIDTH = 9.6f;
    float WORLD_HEIGHT = 4.8f;
    float CAMERA_CLOSEUP_X = 0.2f;
    float CAMERA_CLOSEUP_Y = 0.1f;
}
