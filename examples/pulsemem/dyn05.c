#include <stdio.h>

void g(int *x, int y) {
  /*label 1*/
  *x = *x + y; /*label 2*/
}

void f(int x, int *y) {
  int z; /*label 3*/
  if (x > 0) {
    f(x - 1, &z);  /*label 4*/
    *y = x * z;
  } 
  else *y = 1;
  if (*y > 2) g(y, x); 
  /*label 5*/
}

int main() {
  int e, a; 
  scanf("%i", &e); /*label 6*/
  f(e, &a);  /*label 7*/
  printf("%d", a);
  return 0;
}
