#include <stdio.h>

void g(int x, int *y, int z) {
  /*label 3*/
  if (x < 2) {
    g(x + 1, y, z);  
    *y = *y * 2; /*label 4*/
  }
  else *y = z;
}

int main() {
  int a, b, c;
  scanf("%i", &a);
  scanf("%i", &b); /*label 1*/
  g(a, &c, b);  /*label 2*/
  printf("%d", c);
  return 0;
}
