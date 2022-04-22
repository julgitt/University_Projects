#include "Arkanoid.h"

void RenderText() {
    
    SDL_Rect rect = {
        .x = 10,
        .y = 455,
        .w = 100,
        .h = 20,
    };
    SDL_RenderCopy(renderer, message_quit, NULL, &rect);
    
    rect.x = 470;
    rect.y = 455;
    rect.w = 160;
    rect.h = 20;
    
    SDL_RenderCopy(renderer, message_music, NULL, &rect);

    rect.x = 10;
    rect.y = 430;
    rect.w = 110;
    rect.h = 20;
  
    SDL_RenderCopy(renderer, message_space_to_pause, NULL, &rect);
    
    if (!start) {
        SDL_Rect rect = {
            .x = 140,
            .y = 250,
            .w = 400,
            .h = 60,
        };
        SDL_RenderCopy(renderer, message_start, NULL, &rect);
    }
    else if (paused) {
        SDL_Rect rect = {
            .x = 210,
            .y = 250,
            .w = 240,
            .h = 70,
        };
        SDL_RenderCopy(renderer, message_pause, NULL, &rect);
    }
}


  