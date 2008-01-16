#include <stdio.h>
int a, b, c;

void g(int x, int y, int *z) {
  /*label 1*/
  if (x > 0) {
    g(x - 1, y, z);  /*label 7*/
    *z = *z + 1;
  }
  else *z = y;
}

void f(int x, int y, int *z) {
  int u; /*label 2*/
  if (x > 0) {
    f(x - 1, y, &u);  /*label 3*/
    g(u, y, z);  /*label 4*/
  } 
  else *z = 0;
}

int main() {
  scanf("%i", &a);
  scanf("%i", &b); /*label 5*/
  f(a, b, &c);  /*label 6*/
  printf("%d", c);
  return 0;
}
