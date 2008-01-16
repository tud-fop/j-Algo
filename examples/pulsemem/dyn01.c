#include <stdio.h>
int e, a;

void g(int x, int y, int *z);

void f(int x, int *y) {
  int y1; /*label 1*/
  if (x > 0) {
    f(x - 1, &y1);  /*label 2*/
    g(x - 1, y1, y);  /*label 3*/
  }
  else *y = 1;
}

void g(int x, int y, int *z) {
  int y1; /*label 4*/
  if (x > 0) {
    f(x - 1, &y1);  /*label 5*/
    *z = y1 + y;
  }
  else *z = 1;
}

int main() {
  scanf("%i", &e); /*label 6*/
  f(e, &a);  /*label 7*/
  printf("%d", a);
  return 0;
}
