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
package org.eclipselabs.garbagecat.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/osrodka">Wojciech Osrodka</a>
 */
public class RunTimeWindow {

    private long pauseTime;
    private long number;
    private long interval;
    private List<String> logEntries;

    public RunTimeWindow() {
        this.pauseTime = 0;
        this.number = 0;
        this.interval = 0;
        this.logEntries = new ArrayList<String>();
    }

    public RunTimeWindow(long number, long interval) {
        this.pauseTime = 0;
        this.number = number;
        this.interval = interval;
        this.logEntries = new ArrayList<String>();
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getStartTimestamp() {
        return interval * number;
    }

    public long getEndTimestamp() {
        return interval * number + interval;
    }

    public List<String> getLogEntries() {
        return logEntries;
    }

    public void addLogEntry(String logEntry) {
        this.logEntries.add(logEntry);
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public void addPauseTime(long pauseTime) {
        this.pauseTime += pauseTime;
    }

    @Override
    public String toString() {
        return "MMU #" + number + " pause: " + pauseTime/1000 + "ms";
    }
}
