package com.healthtraze.etraze.api.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.BaseModel;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.security.util.SecurityUtil;

public class CommonUtil {

	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat format = new SimpleDateFormat(PATTERN);
	
	private final static Random random = new Random();
	 

	public static final String DATEPATTERN = "yyyy-MM-dd";

	public static String formateToString(Date date) {
		return formateToString(date);
	}

	static Logger logger = LogManager.getLogger(CommonUtil.class);

	public static String formateToString(Date date, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	public static Date addHoursToJavaUtilDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, 5);
		calendar.add(Calendar.MINUTE, 30);
		return calendar.getTime();
	}

	public static Date parse(String date) {
		return parse(date, PATTERN);
	}

	public static Date parse(String date, String pattern) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String datePlus(String date, int days) {
		return datePlus(parse(date), days);
	}

	public static String datePlus(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		Date currentDatePlusOne = c.getTime();
		return format.format(currentDatePlusOne);
	}

	public static void setCreatedOn(BaseModel baseModel) {
		baseModel.setCreatedBy(SecurityUtil.getUserName());
		baseModel.setCreatedOn(new Date());
	}

	public static void setModifiedOn(BaseModel baseModel) {
		baseModel.setModifiedBy(SecurityUtil.getUserName());
		baseModel.setModifiedOn(new Date());
	}

	public static Date getPasswordExpiryDate() {

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 90);
		return calendar.getTime();

	}

	public static int dateDifferance(Date passwordExpiryDate) {

		Date currentDate = new Date();
		logger.info(currentDate);
		long date1 = currentDate.getTime();
		long date2 = passwordExpiryDate.getTime();

		long difference = date2 - date1;
		int daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
		logger.info(StringIteration.DATEDIFFERANCE, daysBetween);
		return daysBetween;

	}

	public static long getDateDiff(final LocalDateTime date1, final LocalDateTime date2, final TimeUnit timeUnit) {
		long date1MilliSeconds = Timestamp.valueOf(date1).getTime();
		long date2MilliSeconds = Timestamp.valueOf(date2).getTime();
		long diffInMillies = date2MilliSeconds - date1MilliSeconds;
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public static String getTodayDateAsString() {
		LocalDate date = LocalDate.now();
		logger.info(date);
		logger.info(date.getYear());
		return date.getYear() + "" + date.getMonthValue() + "" + date.getDayOfMonth();
	}

	public static void main(String[] args) throws ParseException {
		logger.info(getPasswordExpiryDate());
		logger.info(getTodayDateAsString());
		logger.info(getMonth(new Date()));
		logger.info(lastdate());
		logger.info("localdate---->"+(getLocalDateTime()));
	}

	public static String getMonth(Date date) {
		SimpleDateFormat simpleformat = new SimpleDateFormat("MMM");
		return simpleformat.format(date);

	}

	public static String getStandardDate(Date date) {
		SimpleDateFormat simpleformat = new SimpleDateFormat("MMM dd, yyyy");
		return simpleformat.format(date);

	}

	public static String gethours() {
		LocalDateTime date1 = LocalDateTime.now();

		return date1.getDayOfWeek() + " " + date1.getHour();

	}

	public static String firstLetterCapital(String text) {
		if (text != null && !text.isEmpty() && !text.trim().isEmpty()) {
			text = text.toLowerCase();
			return text.substring(0, 1).toUpperCase() + text.substring(1);
		}
		return text;
	}

	public static String getDateTime(String time) {
		try {

			DateFormat inputFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.ENGLISH);
			Date date = inputFormat.parse(time);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

			return formatter.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String timeFormate(Date time) {
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aa");
		return formatTime.format(time);
	}

	public static String getID() {
		return System.currentTimeMillis() + StringIteration.SPACE.trim();
	}

	public static Long getID1() throws ParseException {
		String time1 = "16:00:00";
		String time2 = "19:00:00";

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = format.parse(time1);
		Date date2 = format.parse(time2);
		return date2.getTime() - date1.getTime();
	}

	public static int lastdate() {
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DATE) - 1;
	}

	public static String getDay(int day) {

		if (day == 0) {
			return "Sunday";
		}
		if (day == 1) {
			return "Monday";
		}
		if (day == 2) {
			return "Tuesday";
		}
		if (day == 3) {
			return "Wednesday";
		}
		if (day == 4) {
			return "Thursday";
		}
		if (day == 5) {
			return "Friday";
		}
		if (day == 6) {
			return "Saturday";
		}

		return null;

	}

	public static int getDate(String day) {

		if (day.equals("sun")) {
			return 0;
		}
		if (day.equals("mon")) {
			return 1;
		}
		if (day.equals("tue")) {
			return 2;
		}
		if (day.equals("wed")) {
			return 3;
		}
		if (day.equals("thu")) {
			return 4;
		}
		if (day.equals("fri")) {
			return 5;
		}
		if (day.equals("sat")) {
			return 6;
		}
	
		return 0;

	}
	
	
	
	@SuppressWarnings("deprecation")
	public static Date getCurrentUtcTime() throws ParseException, java.text.ParseException {   
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");  
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  
        SimpleDateFormat ldf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");  
        Date d1 = null;
        d1 = ldf.parse( sdf.format(new Date()));  

    int hr=    d1.getHours()+5;
    int min=d1.getMinutes()+30;
    d1.setHours(hr);
    d1.setMinutes(min);

		return d1;  

 }
	
	public static LocalDateTime getLocalDateTime() {
		return getLocalDateTime(SecurityUtil.getUserName());
	}

	
	public static LocalDateTime getLocalDateTime(String email) {
		
		return LocalDateTime.now(); 
	}
	


	
	public static Double demicalFormate(Double value) {
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		return Double.valueOf(numberFormat.format(value));
		
	}
	
	public static LocalDateTime financialYearStartDate(LocalDateTime date) {
		if(date.getMonthValue() > 4) {
			return date.withMonth(4).withDayOfMonth(1);
		} 
		else if(date.getMonthValue() == 4) {
			return date.withMonth(4).withDayOfMonth(1);
		}
		
		else {
			return date.minusYears(1).withMonth(4).withDayOfMonth(1);
		}
	}
	

	public static LocalDateTime financialYearEndDate(LocalDateTime date) {
		if(date.getMonthValue() < 4) {
		
			return date.withMonth(3).withDayOfMonth(31);
		
		} 
		else if(date.getMonthValue() ==4) {
			return date.plusYears(1).withMonth(3).withDayOfMonth(31);
		}
		else {
			return date.plusYears(1).withMonth(3).withDayOfMonth(1);
		}
	}
	
	
	 public static LocalDateTime previousFinancialYeartartDate(LocalDateTime date) {
	        LocalDateTime previousYear = date.minusYears(1);
	        if (previousYear.getMonthValue() >= 4) {
	            return previousYear.withMonth(4).withDayOfMonth(1);
	        } 
	            return previousYear.minusYears(1).withMonth(4).withDayOfMonth(1);
	        }
	 
	    

	    public static LocalDateTime previousFinancialYearEndDate(LocalDateTime date) {
	        LocalDateTime previousYear = date.minusYears(1);
	        if (previousYear.getMonthValue() < 4) {
	            return previousYear.withMonth(3).withDayOfMonth(31);
	        }
	        else if(date.getMonthValue() ==4) {
				return previousYear.plusYears(1).withMonth(3).withDayOfMonth(31);
			}
	        else {
	            return previousYear.plusYears(1).withMonth(3).withDayOfMonth(1).minusDays(1);
	        }
	    }
	    
	    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
			File convFile = new File(file.getOriginalFilename());
			try (FileOutputStream fos = new FileOutputStream(convFile)) {
				fos.write(file.getBytes());
				return convFile;
			}
		}
	    
	    public static boolean containsDateFormat(String input) {
			String datePattern = "\\b\\d{2}-\\d{2}-\\d{4}\\b";
			Pattern pattern = Pattern.compile(datePattern);
			Matcher matcher = pattern.matcher(input);
			return matcher.find();
		}
	    
	    public static Date getCurrentDate() {
	    	return new Date();
	    }
	    
	    
	
	    public static String generateUniqueId() {
			long currentTimeMillis = System.currentTimeMillis();
			int randomValue = random.nextInt(9999);
			return currentTimeMillis + "" + randomValue;
		}
	    
	    public static Long generateUniqueIdLong() {
			long currentTimeMillis = System.currentTimeMillis();
			int randomValue = random.nextInt(9999);
			return Long.parseLong(currentTimeMillis +""+ randomValue);
		}
	
	
	
}
