#include "Arkanoid.h"

// COLLISION
void CollisionDetect(Ball* ball, Paddle* paddle, Block blocks[blocksNumber], float previous_X, float previous_Y) {

    // with window
    if ((ball->x + ball->size) > WINDOW_WIDTH) {
        if (start)
            Mix_PlayChannel(-1,sound_bounce, 0);
        ball->xSpeed *= -1;
        ball->x = WINDOW_WIDTH - ball->size;
    }
    else if (ball->x < 0) {
        Mix_PlayChannel(-1, sound_bounce, 0);
        ball->xSpeed *= -1;
        ball->x = 0;
    }
    if ((ball->y) > WINDOW_HEIGHT) {
        // game over
        Mix_PlayChannel(-1, sound_gameOver, 0);
        game_over = true;
        Initial(ball, paddle, blocks);
        return;
    }
    else if (ball->y < 0) {
        Mix_PlayChannel(-1, sound_bounce, 0);
        ball->ySpeed *= -1;
        ball->y = 0;
    }

    //platform with window
    if ((paddle->x + paddle->w) > WINDOW_WIDTH) {
        paddle->x = WINDOW_WIDTH - paddle->w;
    }
    else if (paddle->x < 0) {
        paddle->x = 0;
    }

   if (!start && !paused) {
        BallStartPosition(ball, paddle);
    }

    //ball with platform
    if (((ball->x + ball->size) > paddle->x) && (ball->x < (paddle->x + paddle->w)) && ((ball->y + ball->size) > paddle->y) && (ball->y < (paddle->y + paddle->h))) { // if collides with platform
        Mix_PlayChannel(-1, sound_bounce, 0);
        if ((ball->x + ball->size - ball->size / 2) < paddle->x) { // collides with the left of the platfrom
            ball->xSpeed = -(abs(ball->xSpeed));
            ball->x = paddle->x - ball->size;
        }
        else if ((ball->x + (ball->size) / 5) > (paddle->x + paddle->w)) { // collides with the right of the platform
            ball->xSpeed = -(abs(ball->xSpeed));
            ball->x = paddle->x + paddle->w + 1;
        }
        else {
            Direction(ball, paddle);
            ball->y = paddle->y - ball->size;
        }
    }

    //with blocks_______________________________________________________________________
    bool allDestroyed = true;
    for (int index = 0; index < blocksNumber; index++) {
        if (!(blocks[index].isDestroyed) || blocks[index].isSolid) { // if block exist and it's not a solid block
            if (((ball->x + ball->size) > (blocks[index].x)) && (ball->x < (blocks[index].x + blocks[index].w + PAUSE - 1)) && ((ball->y + ball->size) > blocks[index].y) && ((ball->y - PAUSE + 1) < (blocks[index].y + blocks[index].h))) { // if ball collides with block
                // to avoid problem, when the ball collides with two blocks
                if ((((ball->x + ball->size / 2) < (blocks[index].x + blocks[index].w + PAUSE - 1)) && ((ball->x + ball->size / 2) > blocks[index].x)) || (((ball->y + ball->size / 2) < (blocks[index].y + blocks[index].y)) && ((ball->y + ball->size / 2) > (blocks[index].y - PAUSE + 1)))) {
                    Mix_PlayChannel(-1, sound_bounce, 0);
                    if (((previous_X + ball->size) < blocks[index].x) && (previous_Y > (blocks[index].y - PAUSE + 1)) && ((previous_Y + ball->size) < (blocks[index].y + blocks[index].h))) { // collides with the left of the block
                        ball->xSpeed *= -1;
                    }
                    else if ((previous_X > (blocks[index].x + blocks[index].w + PAUSE - 1)) && ((previous_Y > (blocks[index].y - PAUSE + 1)) || ((previous_Y + ball->size) < (blocks[index].y + blocks[index].h)))) { // collides with the right of the block
                        ball->xSpeed *= -1;
                    }
                    else if ((previous_Y > (blocks[index].y + blocks[index].h)) && (((previous_X + ball->size) > blocks[index].x) || (previous_X < (blocks[index].x + blocks[index].w + PAUSE - 1)))) { // collides with the bottom of the block
                        ball->ySpeed *= -1;
                    }
                    else if (((previous_Y) < (blocks[index].y - PAUSE + 1)) && (((previous_X + ball->size) > blocks[index].x) || (previous_X < (blocks[index].x + blocks[index].w + PAUSE - 1)))) { // collides with the top of the block
                        ball->ySpeed *= -1;
                    }
                    else {
                        ball->ySpeed *= -1;
                        ball->xSpeed *= -1;
                        RandomizeBouncing(ball, 50);
                    }
                    ball->x = previous_X;
                    ball->y = previous_Y;
                    blocks[index].isDestroyed = true;
                }
            }
            if (!blocks[index].isSolid) {
                allDestroyed = false;
            }
        }
    }
    //win
    if (allDestroyed) {
        win = true;
        Mix_PlayChannel(-1, sound_win, 0);
        Initial(ball, paddle, blocks);
        return;
    }
}