#include <stdio.h>
int c;

void f(int x, int y, int *z) {
  /*label 3*/
  if (x > 5) {
    f(x - 2, y, z);
    *z = *z * x; /*label 4*/
  }
  else *z = y; /*label 5*/
}

int main() {
  int a, b;
  scanf("%i", &a);
  scanf("%i", &b); /*label 1*/
  f(a, b, &c); /*label 2*/ 
  printf("%d", c);
  return 0;
}
