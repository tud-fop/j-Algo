#include <stdio.h>
int b;

void g(int *x, int *y, int z) {
  /*label 7*/
  if (z > 2) {
    g(x, y, z - 2);
    *y = *y * 2; /*label 8*/
  }
  else *y = *x + 1; /*label 9*/
}

void f(int x, int y, int *z) {
  /*label 3*/
  if (x > 0) {
    f(x - 2, y, z);  /*label 4*/
    g(&y, z, x);  /*label 5*/
  }
  else *z = y; /*label 6*/
}

int main() {
  int a, c;
  scanf("%i", &a);
  scanf("%i", &c); /*label 1*/
  f(a, c, &b);  /*label 2*/
  printf("%d", b);
  return 0;
}
