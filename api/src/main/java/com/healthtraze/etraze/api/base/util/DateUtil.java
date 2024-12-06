package com.healthtraze.etraze.api.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import com.healthtraze.etraze.api.base.constant.StringIteration;

public class DateUtil {

	private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {

		getDate(30);
	}

	public static Date getDate(int noOfdays) {
		Date currentdate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentdate);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - noOfdays);
		return cal.getTime();
	}

	public static Date getDateWithoutTimestamp(Date date) {
		Date returnDate = null;

		try {
			String fromDate = getGivenDateAsString(date);
			returnDate = getDateFromString(fromDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}

	public static Date getDateWithoutTimestamp(String fromDate) {
		Date returnDate = null;

		try {

			returnDate = getDateFromString(fromDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}

	public static Date getFinancialYearLastDate(Date givenDate) {
		int startyear = getYearFromDate(givenDate);
		if (getMonthFromDate(givenDate) > 3) {
			startyear = startyear + 1;
		}

		Date financialYearStartDate = null;
		try {
			financialYearStartDate = FORMAT.parse(StringIteration.SPACE + startyear + "-" + "03-" + "31");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return financialYearStartDate;

	}

	public static int getMonthInterval(Date startDate, Date endDate) {

		Calendar start = Calendar.getInstance();
		start.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		LocalDate startlocal = LocalDate.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1,
				start.get(Calendar.DAY_OF_MONTH));
		LocalDate endLocal = LocalDate.of(end.get(Calendar.YEAR), end.get(Calendar.MONTH) + 1,
				end.get(Calendar.DAY_OF_MONTH));

		Period age = Period.between(startlocal, endLocal);
		return age.getMonths() + 1 + (age.getYears() * 12);

	}

	public static Date getFinancialYearStartDate(Date givenDate) {
		Date financialYearStartDate = null;
		int startyear = getYearFromDate(givenDate);
		if (getMonthFromDate(givenDate) <= 3) {
			startyear = startyear - 1;
		}

		try {
			financialYearStartDate = FORMAT.parse(StringIteration.SPACE + startyear + "-" + "04-" + "01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return financialYearStartDate;

	}

	public static Date getLastFinancialYearStartDate(Date givenDate) {
		int startYear = getYearFromDate(getFinancialYearStartDate(givenDate));
		startYear = startYear - 1;
		Date financialYearStartDate = null;
		try {
			financialYearStartDate = FORMAT.parse(StringIteration.SPACE + startYear + "-" + "04-" + "01");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return financialYearStartDate;
	}

	private static int getMonthFromDate(Date date) {
		int result = -1;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = cal.get(Calendar.MONTH) + 1;
		}
		return result;
	}

	public static int getYearFromDate(Date date) {
		int result = -1;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			result = cal.get(Calendar.YEAR);
		}
		return result;
	}

	public static int getNoofDaysinMonth(String date) throws ParseException {
		Calendar cal = null;
		Date givenDate = FORMAT.parse(date);
		cal = Calendar.getInstance();
		cal.setTime(givenDate);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static String getCurrentDateAsString() {
		int date = 0;
		int month = 0;
		int year = 0;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			date = cal.get(Calendar.DATE);
			month = cal.get(Calendar.MONTH);
			year = cal.get(Calendar.YEAR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringIteration.SPACE + year + "-" + month + "-" + date;

	}

	public static String getGivenDateAsString(Date givenDate) {
		String date = null;
		try {
			date = FORMAT.format(givenDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Date getDateFromString(String dateCriteria) {
		Date currentDate = null;
		try {
			currentDate = FORMAT.parse(dateCriteria);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}

	public static LocalDate getLocalDate() {
		return LocalDate.now();

	}

}
