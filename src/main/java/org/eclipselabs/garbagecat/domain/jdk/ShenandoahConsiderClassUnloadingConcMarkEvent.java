/**********************************************************************************************************************
 * garbagecat                                                                                                         *
 *                                                                                                                    *
 * Copyright (c) 2008-2023 Mike Millson                                                                               *
 *                                                                                                                    * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse *
 * Public License v1.0 which accompanies this distribution, and is available at                                       *
 * http://www.eclipse.org/legal/epl-v10.html.                                                                         *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.eclipselabs.garbagecat.domain.jdk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipselabs.garbagecat.domain.ThrowAwayEvent;
import org.eclipselabs.garbagecat.util.jdk.JdkMath;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;
import org.eclipselabs.garbagecat.util.jdk.unified.UnifiedRegEx;

/**
 * <p>
 * CONSIDER_CLASS_UNLOADING_CONC_MARK
 * </p>
 * 
 * <p>
 * Logging output at the beginning of gc log when using the Shenandoah collector.
 * </p>
 * 
 * <h2>Example Logging</h2>
 * 
 * <p>
 * 1) With <code>-Xlog:gc*:file=&lt;file&gt;</code>:
 * </p>
 * 
 * <pre>
 * [[0.001s][info][gc] Consider -XX:+ClassUnloadingWithConcurrentMark if large pause times are observed on class-unloading sensitive workloads
 * </pre>
 * 
 * <p>
 * 2) With <code>-Xlog:gc*:file=&lt;file&gt;:time,uptimemillis</code>.
 * </p>
 * 
 * <pre>
 * [2019-02-05T14:47:31.090-0200][2ms] Consider -XX:+ClassUnloadingWithConcurrentMark if large pause times are observed on class-unloading sensitive workloads
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class ShenandoahConsiderClassUnloadingConcMarkEvent extends ShenandoahCollector implements ThrowAwayEvent {

    /**
     * Regular expressions defining the logging.
     */
    private static final String _REGEX = "^" + UnifiedRegEx.DECORATOR
            + " Consider -XX:\\+ClassUnloadingWithConcurrentMark if large pause times are "
            + "observed on class-unloading sensitive workloads[ ]*$";

    private static Pattern PATTERN = Pattern.compile(_REGEX);

    /**
     * Determine if the logLine matches the logging pattern(s) for this event.
     * 
     * @param logLine
     *            The log line to test.
     * @return true if the log line matches the event pattern, false otherwise.
     */
    public static final boolean match(String logLine) {
        return PATTERN.matcher(logLine).matches();
    }

    /**
     * The log entry for the event. Can be used for debugging purposes.
     */
    private String logEntry;

    /**
     * The time when the GC event started in milliseconds after JVM startup.
     */
    private long timestamp;

    /**
     * Create event from log entry.
     * 
     * @param logEntry
     *            The log entry for the event.
     */
    public ShenandoahConsiderClassUnloadingConcMarkEvent(String logEntry) {
        this.logEntry = logEntry;
        Matcher matcher = PATTERN.matcher(logEntry);
        if (matcher.find()) {
            if (matcher.group(2).matches(UnifiedRegEx.UPTIMEMILLIS)) {
                timestamp = Long.parseLong(matcher.group(13));
            } else if (matcher.group(2).matches(UnifiedRegEx.UPTIME)) {
                timestamp = JdkMath.convertSecsToMillis(matcher.group(12)).longValue();
            } else {
                if (matcher.group(15) != null) {
                    if (matcher.group(15).matches(UnifiedRegEx.UPTIMEMILLIS)) {
                        timestamp = Long.parseLong(matcher.group(17));
                    } else {
                        timestamp = JdkMath.convertSecsToMillis(matcher.group(16)).longValue();
                    }
                } else {
                    // Datestamp only.
                    timestamp = JdkUtil.convertDatestampToMillis(matcher.group(2));
                }
            }
        }
    }

    public String getLogEntry() {
        return logEntry;
    }

    public String getName() {
        return JdkUtil.LogEventType.SHENANDOAH_CONSIDER_CLASS_UNLOADING_CONC_MARK.toString();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
