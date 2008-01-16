#include <stdio.h>
int a, b;

void g(int *x, int y) {
  /*label 1*/
  if (y > 1) {
    g(x, y - 1);
    *x = *x * *x; /*label 2*/
  }
  else *x = *x + 1;
}

void f(int x, int *y, int *z) {
  /*label 3*/
  if (x > 0) {
    f(x - 2, y, z); /*label 4*/
    g(z, x); /*label 5*/
  }
  else *z = *y; 
}

int main() {
  int c;
  scanf("%i", &a);
  scanf("%i", &b); /*label 6*/
  f(a, &b, &c); /*label 7*/ 
  printf("%d", c);
  return 0;
}
