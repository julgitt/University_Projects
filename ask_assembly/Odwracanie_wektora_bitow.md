
        .text
        .globl  bitrev
        .type bitrev, @function


  W moim rozwiązaniu używam następującej techniki: Odwracam bajty, a następnie zamieniam miejscami "czwórki", pary i sąsiadujące bity.
 

	bitrev:

		movq 	%rdi, %rdx
		movq	%rdx, %rcx

		shrq 	$32, %rdx			/*odwrocenie kolejnosci bajtow*/
		rol	$8, %dx	
		rol	$16, %edx
		rol	$8, %dx

		rol	$8, %cx
		rol	$16, %ecx
		rol	$8, %cx
		shlq 	$32, %rcx
		orq 	%rdx, %rcx

		movq	$0xf0f0f0f0f0f0f0f0, %rdx 	/*odwrocenie kolejnosci "czwórek" bitów*/
		andq 	%rcx, %rdx			/*aby wyłuskuję pierwsze czwórki bitów*/
		shrq 	$4, %rdx			/*przesuwam je na miejsce drugich czwórek*/	

		movq 	$0x0f0f0f0f0f0f0f0f, %rax
		andq 	%rax, %rcx			/*aby wyłuskuję drugie czwórki bitów*/
		shlq 	$4, %rcx			/*przesuwam je na miejsce pierwszych czwórek*/	
		orq 	%rdx, %rcx			
							/*odwrocenie kolejnosci par bitów analogicznie*/
		movq 	$0xcccccccccccccccc, %rdx	/*maska 11001100...*/
		leaq 	(, %rcx, 4), %rax
		andq 	%rdx, %rcx
		shrq 	$2, %rcx

		andq	%rdx, %rax
		orq	%rax, %rcx
							/*odwrocenie kolejnosci sąsiadujących bitów analogicznie*/
		movq 	$0xaaaaaaaaaaaaaaaa, %rdx	/*maska 10101010...*/
		leaq 	(,%rcx,2), %rax
		andq 	%rdx, %rcx
		shrq 	$1, %rcx

		andq	%rdx, %rax
		orq	%rcx, %rax

		ret
