#include <stdio.h>
int a;

void g(int *x, int y) {
  /*label 6*/
  if (y > 0) {
    g(x, y - 1);
    *x = *x + 2; /*label 7*/
  }
  else *x = *x + 1;
}

void f(int y, int *z) {
  /*label 3*/
  if (y > 0) {
    f(y - 1, z); /*label 4*/
    g(z, y); /*label 5*/
  }
  else *z = 1; 
}

int main() {
  int b;
  scanf("%i", &b); /*label 1*/
  f(b, &a); /*label 2*/ 
  printf("%d", a);
  return 0;
}
