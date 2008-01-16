#include <stdio.h>
int x;

void wirksam (int *a, int b)
{
  /*label 1*/
  *a = *a + b;
  /*label 2*/
}

int main ()
{
  x = 3;
  /*label 3*/
  wirksam(&x, 4); /*$1*/
  /*label 4*/
  return 0;
}