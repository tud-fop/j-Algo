#include <stdio.h>

void f(int a, int* b) {
	/* label1 */
	if (*b > 2 * a) {
		f(2 * a, b);   /* $1 */
	} else {
		*b = *b - a;
	}
	/* label2 */
}

void g(int* x, int y) {
	while (*x > 7) {
		/* label3 */
		f(y, x);       /* $2 */
		*x = *x + 5;
	}
	/* label4 */
}

int main() {
	int m, n;
	scanf("%i", &m);
	scanf("%i", &n);
	/* label5 */
	g(&m, n);          /* $3 */
	/* label6 */
	return 0;
}