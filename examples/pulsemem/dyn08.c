#include <stdio.h>

void g(int x, int *y) {
  /*label 1*/
  *y = *y + x;
}

void f(int *x, int y) {
  int z; /*label 2*/
  if (y > 0) {
    f(&z, y - 1); 
    *x = y * z + 1; /*label 3*/
  } 
  else *x = 2;
  if (*x > 2) g(y, x); /*label 4*/
}

int main() {
  int e, a;
  scanf("%i", &e); /*label 5*/
  f(&a, e); /*label 6*/
  printf("%d", a);
  return 0;
}
