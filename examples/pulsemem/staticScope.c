#include <stdio.h>

int x, i;

void A();

void B()
{ int x;

  x = 1;
  printf("%d\n", x);
   /*label1*/
  A();
   /*label2*/
  printf("%d\n", x);
}

void A()
{  /*label3*/
  x = 2*x;
  printf("%d\n", x);
  if (i < 4) 
  { i = i+1;
     /*label4*/
    B();
  }
   /*label5*/
  printf("%d\n", x);
}

int main()
{ i = 1;
  x = 2;
   /*label6*/
  A();
   /*label7*/
  return 0;
}