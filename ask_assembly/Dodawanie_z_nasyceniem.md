	  .text
		.globl  addsb
		.type   addsb, @function

/*
 * W moim rozwiązaniu używam następującej techniki: dodaje osobno parzyste i nieparzyste bajty oraz wykorzystuje dwie maski- jedna sprawdza czy doszlo do przepelnienia w bajcie, a druga ustawia bajt na int-min/int-max w zaleznosci od bitu znaku
 */

		addsb:
			movq	%rdi, %rdx
			movq	%rsi, %rax
			movq	$0x00ff00ff00ff00ff, %rcx

									/*x & 0x00ff00ff00ff00ff*/
			andq	%rcx, %rdx				

									/*y & 0xff00ff00ff00ff00*/
			andq	%rcx, %rax	

			addq	%rdx, %rax				/*(x & 0x00ff00ff00ff00ff) + (y & 0x00ff00ff00ff00ff)*/

									/*suma wykonana na bajtach o parzystym indeksie (indeksujac od prawej)*/
			andq	%rax, %rcx				/*sum1 = (x & 0x00ff00ff00ff00ff) + (y & 0x00ff00ff00ff00ff) & 0x00ff00ff00ff00ff*/

			/*______________________*/

			movq	$0xff00ff00ff00ff00, %rdx		/*analogicznie dla nieparzystych bajtow*/
			movq	%rdi, %r8
			movq	%rsi, %rax

			andq	%rdx, %r8

			andq	%rdx, %rax

			addq	%r8, %rax

									/*sum2 =  (x & 0xff00ff00ff00ff00) + (y & 0xff00ff00ff00ff00) & 0xff00ff00ff00ff00*/
			andq	%rax, %rdx

			/*______________________*/

									/*suma bez przeniesienia na kolejne bajty*/
			xorq	%rcx, %rdx				/*sum = sum1 ^ sum2*/

			xorq	%rdi, %rsi				/*x ^ y*/
			xorq	$-1, %rsi				/*~(x ^ y)*/

			xorq	%rdx, %rdi				/*x ^ sum*/
			andq	%rdi, %rsi				/* ~(x ^ y) & (x ^ sum)*/

			movq	$0x8080808080808080, %rcx		
			andq	%rcx, %rsi				/* ~(x ^ y) & (x ^ y) & 0x8080808080808080*/

			shrq	$7, %rsi				/* maska ustawia bajt na 11111111 jezeli doszlo w nim do przepelnienia (bity znaku dodawanych int8_t sa takie same, a bit znaku sumy jest inny*/
			imulq	$255, %rsi, %rax			/* mask1 = (((~(x ^ y) & (x ^ sum)) & 0x8080808080808080) >> 7) * 255/

			/*______________________*/

			movq	%rdx, %r8
			andq	%rcx, %r8				/*0x8080808080808080 & sum*/
			shrq	$7, %r8				
			imulq	$255, %r8, %r8				/*(((0x8080808080808080 & sum) >> 7) * 255)*/

									/*maska ustawia bajt na int-maxa jezeli bit znaku sumy = 1, wpp ustawia bajt na int-mina*/ 
			xorq	%r8, %rcx				/*mask2 = 0x8080808080808080 ^ (((0x8080808080808080 & sum) >> 7) * 255)*/

			/*______________________*/

			movq	%rax, %r8
			xorq	$-1, %rax				/*~mask1*/

			andq	%rdx, %rax				/*(~mask1 & sum) jezeli w bajcie nie bylo przepelnienia to przepisuje go, jezeli bylo to zeruje*/
			andq	%r8, %rcx				/*(mask1 & mask2) jezeli w bajcie bylo przepelnienie, to ustawia go na int-mina jezeli bit znaku sumy = 0 (bo to znaczy ze dodawalismy dwie dodatnie liczby i znak sie "przekrecil"), analogicznie dla int-max*/
			addq 	%rcx, %rax				/*wynik to (~mask1 & sum) + (mask1 & mask2), przepisujemy bajty gdzie nie bylo przepelnienia i ustawiamy int-min/int-max tam gdzie bylo*/

			ret

			.size   addsb, .-addsb
