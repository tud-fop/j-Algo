#include <stdio.h>

void f(int x, int y, int *z) {
  /*label 1*/
  if (x > 0) {
    f(x - 1, y, z); /*label 2*/
    *z = x + y;
  }
  else *z = y;
  /*label 3*/
}

int main() {
  int a, b, c;
  scanf("%i", &a);
  scanf("%i", &b); /*label 4*/
  f(a, b, &c); /*label 5*/
  printf("%d", c);
  return 0;
}
