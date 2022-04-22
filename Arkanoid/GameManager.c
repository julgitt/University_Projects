#include "Arkanoid.h"

// INITIALISATION
bool Initialize(Ball* ball, Paddle* paddle, Block blocks[blocksNumber]) {

    //libraries
    if (SDL_Init(SDL_INIT_EVERYTHING) != 0) {
        printf("SDL_Init Error: %s\n", SDL_GetError());
        return false;
    }
    if (Mix_OpenAudio(44100, MIX_DEFAULT_FORMAT, 2, 1024) != 0) {
        printf("Error initializing SDL_mixer: %s\n", Mix_GetError());
        return false;
    }
    if (TTF_Init() != 0) {
        printf("Failed to initialize SDL_ttf\n");
        return false;
    }

    //creating window
    window = SDL_CreateWindow("Arkanoid", SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED, WINDOW_WIDTH, WINDOW_HEIGHT, SDL_WINDOW_SHOWN);
    if (!window) {
        return false;
    }

    //creating renderer
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC);
    if (!renderer) {
        return false;
    }

    //cursor
    SDL_ShowCursor(SDL_DISABLE);
    SDL_SetRelativeMouseMode(SDL_TRUE);

    //textures
    background_texture = LoadTexture("textures/background.png");
    game_over_texture = LoadTexture("textures/game_over.png");
    paddle_texture = LoadTexture("textures/paddleRed.png");
    ball_texture = LoadTexture("textures/ballGrey.png");
    block_grey_texture = LoadTexture("textures/blockGrey.png");
    block_red_texture = LoadTexture("textures/blockRed.png");
    block_yellow_texture = LoadTexture("textures/blockYellow.png");
    block_green_texture = LoadTexture("textures/blockGreen.png");
    block_blue_texture = LoadTexture("textures/blockBlue.png");
    block_purple_texture = LoadTexture("textures/blockPurple.png");

    //font
    font = TTF_OpenFont("Font/arcade_font.ttf", 100);
    if (!font) {
        printf("problem loading font\n");
        return false;
    }

    //text
    message_quit = LoadText("Escape to quit", dark);
    message_music = LoadText("Alt to stop/play music", dark);
    message_start = LoadText("Space to start", light);
    message_pause = LoadText("PAUSE", light);
    message_space_to_pause = LoadText("Space to pause", dark);
    message_again = LoadText("Space to play again", white);
    message_win = LoadText("YOU WIN", white);

    //sounds
    if (!LoadSounds()) {
        return false;
    }

    //objects
    Initial(ball, paddle, blocks);

    return true;
}

SDL_Texture* LoadTexture(char* file) {
    SDL_Surface* image = IMG_Load(file);
    SDL_Texture* texture;

    if (!image) {
        printf("Failed to load image.png: %s\n", IMG_GetError());
        return NULL;
    }

    texture = SDL_CreateTextureFromSurface(renderer, image);
    SDL_FreeSurface(image);
    image = NULL;

    if (!texture) {
        printf("Error creating texture: %s\n", SDL_GetError());
        return NULL;
    }

    return  texture;
}

SDL_Texture* LoadText(char* t, SDL_Color color) {
    SDL_Texture* texture;
    SDL_Surface* text = TTF_RenderText_Solid(font, t, color);
    if (!text) {
        printf("text not loaded\n");
        return NULL;
    }
    texture = SDL_CreateTextureFromSurface(renderer, text);
    SDL_FreeSurface(text);
    return texture;
}

bool LoadSounds() {
    //music
   music = Mix_LoadMUS("sounds/music.mp3");
     if (!music) {
         printf ("Error loading music: %s\n", Mix_GetError());
         return false;
     }

     // Load sounds
    sound_gameOver = Mix_LoadWAV("sounds/game_over.wav");
    if (!sound_gameOver) {
        printf("Error loading game over sound: %s\n", Mix_GetError());
        return false;
    }
    sound_win = Mix_LoadWAV("sounds/win.wav");
    if (!sound_win) {
        printf("Error loading win sound: %s\n", Mix_GetError());
        return false;
    }
    sound_bounce = Mix_LoadWAV("sounds/bounce.wav");
    if (!sound_bounce) {
        printf("Error loading bounce sound: %s\n", Mix_GetError());
        return false;
    }
    
    
    if (Mix_PlayMusic(music, -1) == -1) {
        printf("Mix_PlayMusic: %s\n", Mix_GetError());
    }
    Mix_VolumeMusic(20);
    return true;
}

// UPDATE
void Update(Ball* ball, Paddle* paddle, Block blocks[blocksNumber], float elapsed) {
    // refreshing window
    SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
    SDL_RenderClear(renderer);

    if (!game_over && !win) {
        SDL_RenderCopy(renderer, background_texture, NULL, NULL);
        RenderText();
        
        // drawing elements
        float previousLocation_X = ball->x;
        float previousLocation_Y = ball->y;

        UpdateBall(ball, paddle, elapsed);
        CollisionDetect(ball, paddle, blocks, previousLocation_X, previousLocation_Y);
        RenderBlocks(blocks);
        RenderPaddle(paddle);
        Control(paddle);
        RenderBall(ball);
    }
    else {
        if (game_over) {
            SDL_RenderCopy(renderer, game_over_texture, NULL, NULL);
        }
        else {
            SDL_Rect rect = {
                .x = 150,
                .y = 150,
                .w = 400,
                .h = 200,
            };
            SDL_RenderCopy(renderer, message_win, NULL, &rect);
        
        }
        SDL_Rect rect = {
           .x = 180,
           .y = 380,
           .w = 310,
           .h = 70,
        };
        SDL_RenderCopy(renderer, message_again, NULL, &rect);
    }
    SDL_RenderPresent(renderer);
}

// SHUTDOWN
void ShutDown(void) {
    if (renderer)
        SDL_DestroyRenderer(renderer);

    if (window)
        SDL_DestroyWindow(window);
    if (font) {
        TTF_CloseFont(font);
    }
    font = NULL;
    window = NULL;
    renderer = NULL;

    Mix_FreeChunk(sound_bounce);
    Mix_FreeChunk(sound_gameOver);
    Mix_FreeChunk(sound_win);
    sound_bounce = NULL;
    sound_gameOver = NULL;
    sound_win = NULL;

    IMG_Quit();
    TTF_Quit();
    Mix_Quit();
    SDL_Quit();
}

void Initial(Ball* ball, Paddle* paddle, Block blocks[blocksNumber]) {
    NewBlocks(blocks);
    SolidBlocks(blocks);

    while (!is_reachable(blocks)) {
        NewBlocks(blocks);
        SolidBlocks(blocks);
    }
    

    *paddle = NewPaddle(PADDLE_LENGTH, PADDLE_WIDTH);
    *ball = NewBall(BALL_SIZE, paddle);
    start = false;
}

void EventHandler(SDL_Event* event, bool* quit) {
    while (SDL_PollEvent(event)) {
        if ((*event).type == SDL_KEYDOWN && (*event).key.keysym.sym == SDLK_ESCAPE)
        {
            *quit = true;
        }
        else if ((*event).type == SDL_KEYDOWN && (*event).key.keysym.sym == SDLK_SPACE) {
            if (start && !game_over) {
                paused = !paused;
            }
            else if (game_over || win) {
                start = false;
                paused = false;
                game_over = false;
                win = false;
            }
            else if (!game_over && !win) {
                start = true;
            }
        }
        else if ((*event).type == SDL_KEYDOWN && (*event).key.keysym.sym == SDLK_LALT) {
            if (!Mix_PausedMusic()) {
                Mix_PauseMusic();
            }
            else {
                Mix_ResumeMusic();
            }
        }
        else if ((*event).type == SDL_MOUSEMOTION)
        {
            SDL_GetMouseState(&mouse_x, NULL);
        }
    }
}
