#include <stdio.h>
int c;

void g(int *z, int *y, int x) {
  /*label 6*/
  if (x > 2) {
    g(z, y, x - 2);
    *y = *y * 3; /*label 7*/
  }
  else *y = *z ; 
}

void f(int z, int y, int *x) {
  /*label 3*/
  if (z > 0) {
    f(z - 2, y, x); /*label 4*/
    g(&y, x, z); /*label 5*/
  }
  else *x = y + 1; 
}

int main() {
  int a, b;
  scanf("%i", &a);
  scanf("%i", &b); /*label 1*/
  f(a, b, &c); /*label 2*/
  printf("%d", c);
  return 0;
}
