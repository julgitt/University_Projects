#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

void blackwhite(int width, int height)
{
    int size = width*height;
    int r,g,b;
    int average;
    while (size>0)
    {
        r = (unsigned char)getchar();
        g = (unsigned char)getchar();
        b = (unsigned char)getchar();
        average = (r+g+b)/3;
        putchar(average);
        putchar(average);
        putchar(average);
        size--;
    }
}
void filter(int width, int height, char parameter[])
{
    int size = width*height;
    int r = 0,g = 0,b = 0;
    int red, green, blue;
    int i = 0;
    while (i<=strlen(parameter))
    {
        if (parameter[i]=='r')
        {
            r=1;
        }
        if (parameter[i] == 'g')
        {
            g=1;
        }
        if (parameter[i] == 'b')
        {
            b=1;
        }
        i++;
    }
    while (size>0)
    {
        red=r*(unsigned char)getchar();
        green=g*(unsigned char)getchar();
        blue=b*(unsigned char)getchar();

        putchar(red);
        putchar(green);
        putchar(blue);
        size--;
    }
}
void contrast (int width, int height)
{
    int size = width*height;
    int r,g,b;
    while (size>0)
    {
        r = (unsigned char)getchar();
        g = (unsigned char)getchar();
        b = (unsigned char)getchar();
        if ( (r+g+b)>127)
        {
            putchar(255);
            putchar(255);
            putchar(255);
        }
        else
        {
            putchar(0);
            putchar(0);
            putchar(0);
        }
        size--;
    }
}
void gam(int width, int height, char parameter[])
{
    double gamma_value;
    int size = width*height;
    double r,g,b;
    while (size>0)
    {
        r = ((int)(pow((unsigned char)getchar()*0.001,gamma_value)*100))%256;
        g = ((int)(pow((unsigned char)getchar()*0.001,gamma_value)*100))%256;
        b = ((int)(pow((unsigned char)getchar()*0.001,gamma_value)*100))%256;

        putchar(r);
        putchar(g);
        putchar(b);
        size--;
    }
}

int main(int argc, char *argv[])
{
    int width;
    int height;
    int maxm;
    int c;
    if (argc >= 2)
    {
        c = getchar();
        putchar(c);
        c = getchar();
        putchar(c);
        putchar('\n');
        scanf("%d", &width);
        scanf("%d", &height);
        scanf("%d ", &maxm);
        printf("%d %d\n%d\n", width, height, maxm);
        if ((strcmp(argv[1],"-blackwhite"))== 0)
        {
            blackwhite(width,height);
        }
        else if ((strcmp(argv[1],"-filter"))== 0)
        {
            filter(width,height, argv[2]);
        }
        else if ((strcmp(argv[1],"-contrast"))== 0)
        {
            contrast(width,height);
        }
        else if ((strcmp(argv[1],"-gamma"))== 0)
        {
            gam(width,height, argv[2]);
        }
        else
        {
            printf("There is no such instruction. Use one of these:\n -gamma\n-filter\n-contrast\n-blackwhite\n");
        }
    }
    else
    {
        printf("Wrong number of arguments");
    }
    return 0;
}