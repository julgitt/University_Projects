#ifndef _Arkanoid
#define _Arkanoid
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>
#include <SDL.h>
#include <SDL_mixer.h>
#include <SDL_image.h>
#include <SDL_ttf.h>

#define inWidth 9
#define inHeight 5
#define blocksNumber 45

//___________________________________GLOBAL______________________________
//window
extern const int WINDOW_WIDTH;
extern const int WINDOW_HEIGHT;

extern SDL_Window* window;
extern SDL_Renderer* renderer;

extern TTF_Font* font;

extern SDL_Texture* message_quit;
extern SDL_Texture* message_music;
extern SDL_Texture* message_start;
extern SDL_Texture* message_pause;
extern SDL_Texture* message_space_to_pause;
extern SDL_Texture* message_again;
extern SDL_Texture* message_win;

extern SDL_Color light;
extern SDL_Color dark;
extern SDL_Color white;

//textures used https://opengameart.org/content/puzzle-game-art
extern SDL_Texture* background_texture;
extern SDL_Texture* game_over_texture;

extern SDL_Texture* ball_texture;
extern SDL_Texture* paddle_texture;
extern SDL_Texture* block_grey_texture;
extern SDL_Texture* block_red_texture;
extern SDL_Texture* block_green_texture;
extern SDL_Texture* block_yellow_texture;
extern SDL_Texture* block_blue_texture;
extern SDL_Texture* block_purple_texture;

extern Mix_Music* music;
extern Mix_Chunk* sound_gameOver;
extern Mix_Chunk* sound_win;
extern Mix_Chunk* sound_bounce;

//ball
extern const int BALL_SIZE;
//platform
extern const int PADDLE_WIDTH;
extern const int PADDLE_LENGTH;
//block
extern const int BLOCK_WIDTH;
extern const int BLOCK_HEIGHT;
extern const int PAUSE;
extern const int xMargin;
//game
extern bool start;
extern bool paused;
extern bool game_over;
extern bool win;
extern int mouse_x;
extern int numberOfSolid;
//________________________________STRUCTURES_________________________________________

typedef struct Ball {
    float x;
    float y;
    float xSpeed;
    float ySpeed;
    float xySpeed;
    int size;
} Ball;

typedef struct Paddle {
    float x;
    float y;
    int w;
    int h;
} Paddle;

typedef struct Block {
    float x;
    float y;
    int w;
    int h;
    bool isSolid;
    bool isDestroyed;
} Block;


//___________________________________________FUNCTIONS________________________________________
// 
//Game Manager
void EventHandler(SDL_Event *event, bool* quit);

bool Initialize(Ball*, Paddle*, Block blocks[blocksNumber]);
void Update(Ball*, Paddle*, Block blocks[blocksNumber], float elapsed);
void ShutDown(void);

SDL_Texture* LoadTexture(char* file);
SDL_Texture* LoadText(char* t, SDL_Color color);
bool LoadSounds(void);
void Initial(Ball* ball, Paddle* paddle, Block blocks[blocksNumber]);

//HUD
void RenderText(void);

//Collision
void CollisionDetect(Ball* ball, Paddle* paddle, Block blocks[blocksNumber], float previousLocation_X, float previousLocation_Y);


//Objects

//ball
int initialDirection(void);
void Direction(Ball*, Paddle*);
void RandomizeBouncing(Ball*, int range);
void UpdateBall(Ball* ball, Paddle*, float elapsed);
void BallStartPosition(Ball*, Paddle*);
Ball NewBall(int size, const Paddle*);
void RenderBall(const Ball*);

//platform
Paddle NewPaddle(int length, int width);
void RenderPaddle(const Paddle*);
void Control(Paddle*);

//blocks

void SolidBlocks(Block blocks[blocksNumber]);
Block NewBlock(int height, int width, int y_num, int x_num);
void NewBlocks(Block blocks[blocksNumber]);
void RenderBlock(Block*, int y_num);
void RenderBlocks(Block blocks[blocksNumber]);

bool is_reachable(Block blocks[blocksNumber]);


#endif