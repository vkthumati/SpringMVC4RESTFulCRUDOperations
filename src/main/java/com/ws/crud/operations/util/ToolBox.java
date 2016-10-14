package com.ws.crud.operations.util;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ws.crud.operations.exceptions.ServiceException;

/**
 * General utilities that are specific to ESP services and which cannot be found in libraries like Apache Commons, et al.
 * Created by jograd001c on 12/15/2015.
 */
public class ToolBox {
    private static final Logger LOG = LoggerFactory.getLogger(ToolBox.class);

    private static DatatypeFactory datatypeFactory = null;
    
    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch(DatatypeConfigurationException ex) {
            throw new ServiceException("Failed to initialize instance of DatatypeFactory for XML GregorianCalendar conversions.",ex);
        }
    }

    /**
     * @param calendarString Lexical representation of one the eight XML Schema date/time datatypes. 
     * @return A new XMLGregorianCalendar based on the argumented string.
     */
    public static XMLGregorianCalendar toXmlGregorianCalendar(String calendarString) {
        try {
            return datatypeFactory.newXMLGregorianCalendar(calendarString);
        } catch(IllegalArgumentException | NullPointerException allEx) {
            LOG.warn("Failed to convert string '{}' to XMLGregorianCalender",calendarString,allEx);
            return null;
        }
    }
    
    /**
     * Converts a standard calendar instance to a gregorian calendar.
     * @param calendar The gregorian calendar to convert.
     * @return The converted gregorian calendar.
     */
    public static XMLGregorianCalendar toXmlGregorianCalendar(GregorianCalendar calendar) {
        XMLGregorianCalendar xmlCalendar = null;
        if(calendar!=null) {
            calendar.setTimeZone(TimeZone.getDefault());
            xmlCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);
        }
        return xmlCalendar;
    }

    /**
     * Checks a string for truthiness.  A string is truthy if it is (ignoring case):
     * <ul>
     *     <li>y</li>
     *     <li>yes</li>
     *     <li>true</li>
     *     <li>t</li>
     *     <li>1</li>
     * </ul>
     * All other values will be considered false.
     * @param boolStr The string to check for truthiness.
     * @return Returns <code>true</code> if the string is among those considered to be truthy, otherwise returns <code>false</code>.
     */
    public static boolean isTruthy(String boolStr) {
        return "Y".equalsIgnoreCase(boolStr)
                || "yes".equalsIgnoreCase(boolStr)
                || "true".equalsIgnoreCase(boolStr)
                || "t".equalsIgnoreCase(boolStr)
                || "1".equals(boolStr)
                || "enabled".equalsIgnoreCase(boolStr);
    }

    /**
     * A stream-like way to handle Enumerations.
     * 
     * @apiNote This method will be deprecated when Java 9 comes out: <a href="http://hg.openjdk.java.net/jdk9/dev/jdk/file/4be07e0eb9b6/src/java.base/share/classes/java/util/Enumeration.java#l84">Enumeration.asIterator()</a>
     * @param enumeration
     * @param consumer
     * @param <T>
     */
    public static <T> void forEachRemaining(Enumeration<T> enumeration, Consumer<? super T> consumer) {
        while(enumeration.hasMoreElements()) consumer.accept(enumeration.nextElement());
    }

    /**
     * A way to transform an Enumeration collection into a stream.  You're welcome.
     * @param enumeration The enumeration to turn into a stream.
     * @param <T> The enumeration type.
     * @return A stream created from the enumeration.
     */
    public static <T> Stream<T> enumerationAsStream(Enumeration<T> enumeration) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return enumeration.nextElement();
                            }
                            public boolean hasNext() {
                                return enumeration.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }
}

