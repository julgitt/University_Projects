#include "Arkanoid.h"

// BALL
int initialDirection(void) {
    return rand() % 2 == 1 ? 1 : -1;
}

// direction depends on position of the ball relative to the platform 
void Direction(Ball* ball, Paddle* paddle) {
    float xySpeed = ball->xySpeed;

    // position of the ball: -1->right edge, 1-> left edge, 0-> center of the platform
    float delta_x = ((ball->x + ball->size / 2) - (paddle->x + paddle->w / 2)) / (paddle->w / 2);
    float speedControl = 0.8f;

    int sign = delta_x < 0 ? -1 : 1;
    // scaling a range to another
    ball->xSpeed = sign * (((fabs)(delta_x)*speedControl * xySpeed) * ((double)xySpeed - 100.0f) / xySpeed + 100);
    ball->ySpeed = sqrt((double)xySpeed * xySpeed - (double)ball->xSpeed * ball->xSpeed) * (ball->ySpeed > 0 ? -1 : 1);
}

void RandomizeBouncing(Ball* ball, int range) {
    float xySpeed = ball->xySpeed;
    ball->xSpeed += (rand() % 2 == 1 ? 1 : -1) * rand() % range;
    ball->ySpeed = sqrt((double)xySpeed * xySpeed - (double)ball->xSpeed * ball->xSpeed);
}

void UpdateBall(Ball* ball, Paddle* paddle, float elapsed) {
    if (start && !paused) {
        ball->x += ball->xSpeed * elapsed;
        ball->y += ball->ySpeed * elapsed;
    }
    if (!start && !paused) {
        BallStartPosition(ball, paddle);
    }
}

void BallStartPosition(Ball* ball, Paddle* paddle) {
    ball->x = paddle->x + paddle->w / 2 - ball->size / 2;
}



Ball NewBall(int size, const Paddle* paddle) {
    const float Speed = 270.0f;
    Ball ball = {
        .x = paddle->x + (paddle->w) / 2,
        .y = paddle->y - (paddle->h) - size,
        .size = size,
        .xSpeed = Speed * initialDirection(),
        .ySpeed = -Speed,
        .xySpeed = sqrt(2 * (double)Speed * Speed),
    };
    return ball;
}

void RenderBall(const Ball* ball) {
    int size = ball->size;
    int halfSize = size / 2;
    SDL_Rect rect = {
        .x = ball->x,
        .y = ball->y,
        .w = size,
        .h = size,
    };
    SDL_RenderCopy(renderer, ball_texture, NULL, &rect);
}

//PLATFORM
Paddle NewPaddle(int length, int width) {
    Paddle paddle = {
        .x = WINDOW_WIDTH / 2 - length / 2,
        .y = WINDOW_HEIGHT - length,
        .w = length,
        .h = width,
    };
    return paddle;
}

void RenderPaddle(const Paddle* paddle) {
    SDL_Rect rect = {
        .x = paddle->x,
        .y = paddle->y,
        .w = paddle->w,
        .h = paddle->h,
    };

    SDL_RenderCopy(renderer, paddle_texture, NULL, &rect);
}

void Control(Paddle* paddle) {
    if (!paused) {
        paddle->x = mouse_x;
    }
}


//BLOCK
void SolidBlocks(Block blocks[blocksNumber]) {
    if (numberOfSolid != 0) {
        int number = (rand() % numberOfSolid / 2) + numberOfSolid / 2;
        while (number > 0) {
            if (!blocks[(rand() % (blocksNumber))].isSolid) {
                blocks[(rand() % (blocksNumber))].isSolid = true;
                number--;
            }
        }
    }
}

Block NewBlock(int height, int width, int y_num, int x_num) {
    Block block = {
        .x = xMargin + PAUSE * x_num + BLOCK_WIDTH * x_num,
        .y = PAUSE * (y_num + 1) + BLOCK_HEIGHT * y_num,
        .w = width,
        .h = height,
        .isDestroyed = false,
        .isSolid = false,
    };
    return block;
}


void RenderBlock(Block* block, int y_num) {
    SDL_Rect rect = {
        .x = block->x,
        .y = block->y,
        .w = block->w,
        .h = block->h,
    };
    SDL_Texture* texture = NULL;
    if (block->isSolid) {
        texture = block_grey_texture;
    }
    else {
        switch (y_num)
        {
        case 1:
            texture = block_red_texture;
            break;
        case 2:
            texture = block_yellow_texture;
            break;
        case 3:
            texture = block_green_texture;
            break;
        case 4:
            texture = block_blue_texture;
            break;
        case 5:
            texture = block_purple_texture;
            break;
        }
    }
    SDL_RenderCopy(renderer, texture, NULL, &rect);
}

void NewBlocks(Block blocks[blocksNumber]) {
    int h = 0;
    int w = 0;
    for (int index = 0; index < blocksNumber; index++) {
        if (w == inWidth) {
            w = 0;
            h++;
        }
        blocks[index] = NewBlock(BLOCK_HEIGHT, BLOCK_WIDTH, h, w);
        w++;
    }
}

void RenderBlocks(Block blocks[blocksNumber]) {
    int h = 0;
    int w = 0;
    for (int index = 0; index < blocksNumber; index++) {
        if (w == inWidth) {
            w = 0;
            h++;
        }
        if ((!(blocks[index].isDestroyed)) || (blocks[index].isSolid)) {
            RenderBlock(&blocks[index], h + 1);
        }
        w++;
    }
}

bool is_reachable(Block blocks[blocksNumber]) {
    bool reachable[blocksNumber];
    // initialisation of array
    for (int i = 0; i < blocksNumber; i++) {
        if (blocks[i].isSolid || i < (blocksNumber - inWidth)) {
            reachable[i] = false;
        }
        else {
            reachable[i] = true;
        }
    }

    for (int i = (blocksNumber - inWidth - 1); i >= 0; i--) {
        if (((i + inWidth < blocksNumber && reachable[i + inWidth]) || ((i + 1) % inWidth != 0 && reachable[i + 1])) && !blocks[i].isSolid) {
            reachable[i] = true;
        }
    }

    for (int i = 0; i < blocksNumber; i++) {
        if (!reachable[i] && !blocks[i].isSolid) {
            if ((i - inWidth >= 0 && reachable[i - inWidth]) || ((i - 1) > 0 && (i - 1) % inWidth != (inWidth - 1) && reachable[i - 1])) {
                reachable[i] = true;
            }
            else {
                return false;
            }
        }
    }
    return true;
}