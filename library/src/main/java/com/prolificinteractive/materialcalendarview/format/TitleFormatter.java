package com.prolificinteractive.materialcalendarview.format;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Used to format a {@linkplain com.prolificinteractive.materialcalendarview.CalendarDay} to a string for the month/year title
 */
public interface TitleFormatter {

  String DEFAULT_FORMAT = "MMMM yyyy";


  TitleFormatter DEFAULT = new DateFormatTitleFormatter();

  /**
   * Converts the supplied day to a suitable month/year title
   *
   * @param day the day containing relevant month and year information
   * @return a label to display for the given month/year
   */
  CharSequence format(CalendarDay day);
}
