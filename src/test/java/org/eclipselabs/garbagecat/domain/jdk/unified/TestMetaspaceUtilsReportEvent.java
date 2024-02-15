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
package org.eclipselabs.garbagecat.domain.jdk.unified;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipselabs.garbagecat.TestUtil;
import org.eclipselabs.garbagecat.domain.JvmRun;
import org.eclipselabs.garbagecat.service.GcManager;
import org.eclipselabs.garbagecat.util.Constants;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil.LogEventType;
import org.eclipselabs.garbagecat.util.jdk.unified.UnifiedUtil;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
class TestMetaspaceUtilsReportEvent {

    @Test
    void testIdentityEventType() {
        String logLine = "[2022-02-08T07:33:14.540+0000][7732788ms] Usage:";
        assertEquals(JdkUtil.LogEventType.METASPACE_UTILS_REPORT, JdkUtil.identifyEventType(logLine, null),
                JdkUtil.LogEventType.METASPACE_UTILS_REPORT + "not identified.");
    }

    @Test
    void testLineTimeUptimeMillis() {
        String logLine = "[2022-02-08T07:33:14.540+0000][7732788ms] Usage:";
        assertTrue(MetaspaceUtilsReportEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.METASPACE_UTILS_REPORT.toString() + ".");
    }

    @Test
    void testLineUptimeMillis() {
        String logLine = "[7732788ms] Usage:";
        assertTrue(MetaspaceUtilsReportEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.METASPACE_UTILS_REPORT.toString() + ".");
    }

    @Test
    void testLogLines() throws IOException {
        File testFile = TestUtil.getFile("dataset245.txt");
        GcManager gcManager = new GcManager();
        URI logFileUri = testFile.toURI();
        List<String> logLines = Files.readAllLines(Paths.get(logFileUri));
        gcManager.store(logLines, false);
        JvmRun jvmRun = gcManager.getJvmRun(null, Constants.DEFAULT_BOTTLENECK_THROUGHPUT_THRESHOLD,
                Constants.DEFAULT_HIGH_MEMORY_ALLOCATION_THRESHOLD);
        // assertEquals(0, jvmRun.getEventTypes().size(), "Event type count not correct.");
        assertFalse(jvmRun.getEventTypes().contains(LogEventType.UNKNOWN),
                JdkUtil.LogEventType.UNKNOWN.toString() + " collector identified.");
    }

    @Test
    void testParseLogLine() {
        String logLine = "[2022-02-08T07:33:14.540+0000][7732788ms] Usage:";
        assertTrue(JdkUtil.parseLogLine(logLine, null) instanceof MetaspaceUtilsReportEvent,
                JdkUtil.LogEventType.METASPACE_UTILS_REPORT.toString() + " not parsed.");
    }

    @Test
    void testReportable() {
        String logLine = "[2022-02-08T07:33:14.540+0000][7732788ms] Usage:";
        assertFalse(JdkUtil.isReportable(JdkUtil.identifyEventType(logLine, null)),
                JdkUtil.LogEventType.METASPACE_UTILS_REPORT.toString() + " incorrectly indentified as reportable.");
    }

    @Test
    void testUnified() {
        List<LogEventType> eventTypes = new ArrayList<LogEventType>();
        eventTypes.add(LogEventType.METASPACE_UTILS_REPORT);
        assertTrue(UnifiedUtil.isUnifiedLogging(eventTypes),
                JdkUtil.LogEventType.METASPACE_UTILS_REPORT.toString() + " not indentified as unified.");
    }
}
