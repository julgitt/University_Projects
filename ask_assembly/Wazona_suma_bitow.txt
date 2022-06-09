        .data          
        mask1 : .quad 0xaaaaaaaaaaaaaaaa       
        mask2 : .quad 0xcccccccccccccccc
        mask3 : .quad 0xf0f0f0f0f0f0f0f0
        mask4 : .quad 0xff00ff00ff00ff00
        mask5 : .quad 0xffff0000ffff0000
        mask6 : .quad 0xffffffff00000000

        .text
        .globl  wbs
        .type wbs, @function

/*
 * W moim rozwiązaniu używam następującej techniki: Obliczam sumę ważoną na parach bitów (gdzie wagi wynoszą 0 i 1), przez andowanie z maską 101010... i popcnt
 *następnie dla czwórek bitów (gdzie wagi to 3, 2, 1, 0), przez andowanie liczby z maska 11001100.., 
 *pomnożenie wyniku przez 2 oraz dodanie wcześniej obliczonej sumy dla par bitów. Reszta działań wykonywana jest analogicznie, co ostatecznie daje nam sumę ważoną dla n bitów
 */

wbs:
        movq            %rdi, %rdx
        and             mask1, %rdx
        popcnt          %rdx, %rax
        
        movq            %rdi, %rdx
        and             mask2, %rdx
        popcnt          %rdx, %rcx
        shlq            $1, %rcx
        add             %rcx, %rax
        
        movq            %rdi, %rdx
        and             mask3, %rdx
        popcnt          %rdx, %rcx
        shlq            $2, %rcx
        add             %rcx, %rax
        
        movq            %rdi, %rdx
        and             mask4, %rdx
        popcnt          %rdx, %rcx
        shlq            $3, %rcx
        add             %rcx, %rax
        
        movq            %rdi, %rdx
        and             mask5, %rdx
        popcnt          %rdx, %rcx
        shlq            $4, %rcx
        add             %rcx, %rax
        
        movq            %rdi, %rdx
        and             mask6, %rdx
        popcnt          %rdx, %rcx
        shlq            $5, %rcx
        add             %rcx, %rax
        
        ret

        .size wbs, .-wbs