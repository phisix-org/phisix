/**
 * Copyright (C) 2012 Edge Dalmacio <edgar.dalmacio@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.xml.bind;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Adapts {@link XMLGregorianCalendar} to {@link Calendar}.
 *
 * @author Lorenzo Dee
 * @author Edge Dalmacio
 */
public class DateAdapter {

	public static Calendar parseDate(String s) {
		return DatatypeConverter.parseDate(s);
	}

	public static String printDate(Calendar date) {
		return DatatypeConverter.printDate(date);
	}

	public static Calendar parseDateTime(String s) {
		return DatatypeConverter.parseDateTime(s);
	}

	public static String printDateTime(Calendar date) {
		return DatatypeConverter.printDateTime(date);
	}
	
	public static Integer parseYear(String s) {
		return DatatypeConverter.parseDate(s).get(Calendar.YEAR);
	}

	public static String printYear(Integer year) {
		return String.valueOf(year);
	}

}
