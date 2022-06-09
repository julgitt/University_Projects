/*
 * W moim rozwiązaniu używam następującej techniki: Sumuję parzyste i nieparzyste bajty metodą dziel i zwyciężaj,
 *następnie odejmuję sumę nieparzystych od sumy parzystych (jeżeli wynik jest ujemny zapala się flaga SF)
 *instrukją lea dodaję do rejestru wynikowego 136 (ponieważ najmniejszy ujemny wynik to -120, 136 jest wielokrotnością 17, zatem nie zmienia reszty, a liczba staje się dodatnia)
 *wynik lea zapisuję w innym rejestrze. Instrukcją cmovs zapisuję wynik lea w rejestrze, w którym przechowuję wyliczoną wartość, jeżeli flaga SF jest zapalona.
 *Następnie wykonuję analogiczne działanie, tylko że dla liczby zapisanej na jednym bajcie oraz dodaję 17 zamiast 136.
 */

mod17:
        movabs          $0x0F0F0F0F0F0F0F0F, %rdx;
        movq            %rdi, %rax
        shrq            $4, %rax
        andq            %rdx, %rdi                      /*wyłuskuję parzyste bajty*/
        andq            %rdx, %rax                      /*wyłuskuję nieparzyste bajty*/
        
        movq            %rax, %rcx                      /*sumowanie bajtów*/
        shrq            $8, %rcx
        addq            %rax, %rcx
        
        movq            %rdi, %rax
        shrq            $8, %rax
        addq            %rdi, %rax
        
        movq            %rcx, %rdx
        shrq            $16, %rdx
        addq            %rcx, %rdx
        
        movq            %rax, %rcx
        shrq            $16, %rcx
        addq            %rax, %rcx
        
        movq            %rdx, %rax
        shrq            $32, %rax
        addq            %rdx, %rax
        
        movq            %rcx, %rdx
        shrq            $32, %rdx
        addq            %rcx, %rdx
        
        andq            mask1, %rax             /*usuwam "śmieci" które powstały po dodawaniu (interesuje nas jedynie ostatni bajt)*/
        andq            mask1, %rdx
        
        subq            %rax, %rdx              /*wynik = parzyste - nieparzyste*/
        leaq            136(%rdx), %rax         
        cmovs           %rax, %rdx              /*jeśli wynik < 0: wynik += 136*/
        
        movq            %rdx, %rax
        shrq            $4, %rdx
        andq            mask2, %rax
        
        subq            %rdx, %rax            /*wynik = (wynik & 0x0f) - (wynik >> 4);  */  
        leaq            17(%rax), %rdx        
        cmovs           %rdx, %rax            /*jeśli wynik < 0: wynik += 17;  */  
        
        ret