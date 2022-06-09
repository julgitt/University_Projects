	clz:    
		.text
		.globl  clz
		.type   clz, @function

/*
 * W moim rozwiązaniu używam następującej techniki: przeszukiwanie binarne
 */

	clz:

		movq	%rdi, %rdx			/* wykonywanie operacji na rdx działa szybiej, więc przenoszę do niego argument (nazwijmy go x) */		

		xorq	%rcx, %rcx			/* temp = 0 */

		movq	$0xffffffff00000000, %rax	
		andq	%rdx, %rax			
		test	%rax, %rax			/* jeżeli najbardziej znaczące 32 bity x'a są zerami, to:*/
		sete	%cl 				/* temp = 32 (wpp n = 0) */
		sal 	$5, %rcx           		
		sal 	%cl, %rdx          		/*oraz shiftuję x o 32 w lewo, pozbywając się zer, wpp shiftuję x o 0 (x pozostaje bez zmian) */
		movq	%rcx, %r8			/* n = temp*/

		xorq	%rcx, %rcx			/*zeruję tempa*/

		movq	$0xffff000000000000, %rax
		andq	%rdx, %rax
		test	%rax, %rax   		 	/*podobnie jak wyżej,  najbardziej znaczące 16 bitów x'a jest zerami*/
		sete	%cl
		sal	$4, %rcx   			/*wtedy temp = 16 (wpp temp = 0) */
		leaq	(%r8, %rcx), %r8   		/*n += temp */      
		sal	%cl, %rdx			/*shiftujemy x'a o temp */ 
							/*dalej wykonuję analogiczne instrukcje do tych powyżej*/
		xorq	%rcx, %rcx

		movq	$0xff00000000000000, %rax
		andq	%rdx, %rax
		test	%rax, %rax     			
		sete	%cl
		sal	$3, %rcx         		
		leaq	(%r8, %rcx), %r8      		
		sal	%cl, %rdx      			

		xorq	%rcx, %rcx

		movq	$0xf000000000000000, %rax
		andq	%rdx, %rax  
		test	%rax, %rax     			 
		sete	%cl
		sal	$2, %rcx         		
		leaq	(%r8, %rcx), %r8    		
		sal	%cl, %rdx

		xorq	%rcx, %rcx

		movq	$0xc000000000000000, %rax
		andq	%rdx, %rax
		test	%rax, %rax      		
		sete	%cl         			
		sal	$1, %rcx
		leaq	(%r8, %rcx), %r8     		
		sal	%cl, %rdx 	      		

		xorq	%rax, %rax
		movq	$0x8000000000000000, %rcx
		cmp 	%rcx, %rdx 			/*jeżeli pierwszy bit x'a nie jest zapalony, to cmp flagę CF na 1 */
		adc 	%rax, %r8      			/*n = n + 0 + (1 jeśli CF ustawiona na 1, 0  wpp*/


		test	%rdx, %rdx			/*jeżeli x = 0, to n++*/           
		sete	%al
		leaq	(%r8, %rax), %rax  

		ret

		.size   clz, .-clz        
		sete	%al
		leaq	(%r8, %rax), %rax  

		ret
