#include <stdio.h>
int a, b, c;

void g(int *x, int *y, int z) {
  /*label 6*/
  if (z > 0) {
    g(x, y, z - 2);
    *y = *x * *y; /*label 7*/
  }
  else *y = *y + 1;  
}

void f(int *x, int y) {
  /*label 3*/
  if (y > 1) {
    f(x, y - 1); /*label 4*/
    g(&b, x, y); /*label 5*/
  }
  else *x = 1;
}

int main() {
  scanf("%i", &a);
  scanf("%i", &b); /*label 1*/
  f(&c, a); /*label 2*/ 
  printf("%d", c);
  return 0;
}
