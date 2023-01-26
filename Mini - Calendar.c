#include <stdio.h>
#include <stdbool.h>

bool is_leap_year(int year) {
    return (year % 4 == 0 && (year % 100 || year % 400 == 0));
}

int days_in_month[] = {[0]=0, [1]=31, [2]=28, [3]=31, [4]=30, [5]=31, [6]=30, [7]=31, [8]=31, [9]=30, [10]=31, [11]=30, [12]=31};

void add_days_to_date(int* mm, int* dd, int* yy, int days_left_to_add) {
  int days_left_in_month;
  while (days_left_to_add > 0) {
    days_left_in_month = days_in_month[*mm] - *dd;
    if (days_in_month[2] && is_leap_year(*yy) == true) {
      days_left_in_month++;
    }
  if (days_left_to_add > days_left_in_month) {
    days_left_to_add -= days_left_in_month;
    days_left_to_add -= 1;
    *dd = 1;
    if (*mm == 12) {
      *mm = 1;
      *yy = *yy + 1;
    } else *mm = *mm + 1;
  } else {
    *dd = *dd + days_left_to_add;
    days_left_to_add = 0;
    }
  }
}

int main() {
  int year, mm, dd, yy, days_left_to_add;
  printf("Enter a Year (between 1800 to 10000) = ");
  scanf("%d", &year);
  is_leap_year(year);
  if (is_leap_year(year) == true) {
    printf("It is a leap year\n");
  } else printf("It is not a leap year\n");
  printf("Please Enter a Date betweeen the years 1800 to 10000 in the format (dd mm yy) = ");
  scanf("%d %d %d", &dd, &mm, &yy);
  printf("Enter number of days to add to this date = ");
  scanf("%d", &days_left_to_add);
  add_days_to_date(&mm, &dd, &yy, days_left_to_add);
  printf("%d/%d/%d\n", dd, mm, yy);
}