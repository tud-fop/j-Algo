#include <stdio.h>
int a, b, c;

void p1(int *c, int b) {
  /*label 2*/
  *c = a - b;
  b = a + b;
  a = *c + b; /*label 3*/
}

int p2(int *a, int *b) {
  int c; /*label 4*/
  c = *a;
  *a = *b;
  *b = c + 1; /*label 5*/
  return c;
}

void p3() {
  int a; /*label 6*/
  a = 1;
  a = p2(&c, &a);  /*label 7*/
}

void p4(int *a, int b, int *c) {
  /*label 8*/
  p1(c, b); 
  *a = *a + b + *c; /*label 9*/
}

int main() {
  a = 1;
  b = 2;
  c = 3; /*label 1*/
  p1(&b, a);  /*label 10*/
  a = p2(&c, &b);  /*label 11*/ 
  p3();  /*label 12*/
  p4(&c, b, &a);  /*label 13*/
  printf("%d", a);
  printf("%d", b);
  printf("%d", c);
  return 0;
}
