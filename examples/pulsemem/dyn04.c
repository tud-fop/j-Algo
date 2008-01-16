#include <stdio.h>

void g(int x, int y, int *z);

void f(int x, int *y) {
  int u; /*label 1*/
  if (x > 0) {
    f(x - 1, &u);  /*label 2*/
    g(x - 1, u, y); 
  } 
  else *y = 1;
  /*label 3*/
}

void g(int x, int y, int *z) {
  int u; /*label 4*/
  if (x > 0) {
    f(x - 1, &u);  /*label 5*/
    *z = u + y;
  }  
  else *z = 1;
  /*label 6*/
}

int main() {
  int e, a; /*label 7*/
  scanf("%i", &e);
  f(e, &a);  /*label 8*/
  printf("%d", a);
  return 0;
}
