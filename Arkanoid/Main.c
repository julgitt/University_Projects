#include "Arkanoid.h"


const int WINDOW_WIDTH = 640;
const int WINDOW_HEIGHT = 480;

SDL_Window* window = NULL;
SDL_Renderer* renderer = NULL;

SDL_Color light= { 100, 140, 200 };
SDL_Color dark = { 50, 70, 100 };
SDL_Color white = { 255, 255, 255 };

//text textures
TTF_Font* font = NULL;
SDL_Texture* message_quit = NULL;
SDL_Texture* message_music = NULL;
SDL_Texture* message_start = NULL;
SDL_Texture* message_pause = NULL;
SDL_Texture* message_space_to_pause = NULL;
SDL_Texture* message_again = NULL;
SDL_Texture* message_win = NULL;

//textures
SDL_Texture* background_texture = NULL;
SDL_Texture* game_over_texture = NULL;

SDL_Texture* ball_texture = NULL;
SDL_Texture* paddle_texture = NULL;
SDL_Texture* block_grey_texture = NULL;
SDL_Texture* block_red_texture = NULL;
SDL_Texture* block_green_texture = NULL;
SDL_Texture* block_yellow_texture = NULL;
SDL_Texture* block_blue_texture = NULL;
SDL_Texture* block_purple_texture = NULL;

//music
Mix_Music* music = MUS_NONE;
Mix_Chunk* sound_gameOver = MUS_NONE;
Mix_Chunk* sound_win = MUS_NONE;
Mix_Chunk* sound_bounce = MUS_NONE;

//ball
const int BALL_SIZE = 12;
//platform
const int PADDLE_WIDTH = 10;
const int PADDLE_LENGTH = 90;
//block
const int BLOCK_WIDTH = 68;
const int BLOCK_HEIGHT = 30;
const int PAUSE = 2;
const int xMargin = 6;
//game
bool win = false;
bool start = false;
bool paused = false;
bool game_over = false;
int mouse_x = 0;
int numberOfSolid = 10;



//___________________________________MAIN______________________________

int main(int argc, char* argv[]) {

    Paddle paddle;
    Ball ball;
    Block blocks[blocksNumber];

    srand((unsigned int)time(NULL));
    atexit(&ShutDown);
    if (!Initialize(&ball,&paddle,blocks)) {
        exit(1);
    }

    bool quit = false;
    
    SDL_Event event;

    // get the number of milliseconds since SDL library initialization
    Uint32 last_tick = SDL_GetTicks();

    // game loop
    while (!quit) {
        Uint32 current_tick = SDL_GetTicks();
        Uint32 difference = current_tick - last_tick;
        float elapsed = difference / 1000.0f;
        Update(&ball, &paddle, blocks, elapsed);
        last_tick = current_tick;
        EventHandler(&event, &quit);
    }
    ShutDown();
    
    return 0;
}
