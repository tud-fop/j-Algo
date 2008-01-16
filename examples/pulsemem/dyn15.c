#include <stdio.h>
int e;

void g(int *x, int y) {
  /*label 1*/
  *x = *x + y;
}

void f(int x, int *y) {
  int u; /*label 2*/
  if (x > 0) {
    f(x - 1, &u);  
    *y = x * u + 2; /*label 3*/
  } 
  else *y = 5;
  if (*y > 5) {
    g(y, x); /*label 4*/
  }
}

int main() {
  int a;
  scanf("%i", &e); /*label 5*/
  f(e, &a); /*label 6*/
  printf("%d", a);
  return 0;
}
