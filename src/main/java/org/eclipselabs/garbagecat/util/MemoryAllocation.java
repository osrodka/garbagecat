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

import org.eclipselabs.garbagecat.util.Memory.Unit;

/**
 * @author <a href="https://github.com/osrodka">Wojciech Osrodka</a>
 */
public class MemoryAllocation {

    public static enum AllocationType {
        MIN, MAX, AVG, HIGH
    }
    
    private Memory allocatedMemory;
    private String initLogEntry;
    private String endLogEntry;
    private long initLogEntryTimestamp;
    private long endLogEntryTimestamp;

    private AllocationType allocationType;

    public MemoryAllocation(Memory allocatedMemory, AllocationType type) {
        this.allocatedMemory = allocatedMemory;
        this.allocationType = type;
    }

    public Memory getAllocatedMemory() {
        return allocatedMemory;
    }

    public void setAllocatedMemory(Memory allocatedMemory) {
        this.allocatedMemory = allocatedMemory;
    }

    public String getInitLogEntry() {
        return initLogEntry;
    }

    public void setInitLogEntry(String initLogEntry) {
        this.initLogEntry = initLogEntry;
    }

    public String getEndLogEntry() {
        return endLogEntry;
    }

    public void setEndLogEntry(String endLogEntry) {
        this.endLogEntry = endLogEntry;
    }

    public long getInitLogEntryTimestamp() {
        return initLogEntryTimestamp;
    }

    public void setInitLogEntryTimestamp(long initLogEntryTimestamp) {
        this.initLogEntryTimestamp = initLogEntryTimestamp;
    }

    public long getEndLogEntryTimestamp() {
        return endLogEntryTimestamp;
    }

    public void setEndLogEntryTimestamp(long endLogEntryTimestamp) {
        this.endLogEntryTimestamp = endLogEntryTimestamp;
    }

    public AllocationType getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(AllocationType allocationType) {
        this.allocationType = allocationType;
    }

    @Override
    public String toString() {
        if (endLogEntryTimestamp - initLogEntryTimestamp > 1000) {
            return allocationType.toString() + " Allocation Rate: " + allocatedMemory.toString() + "/sec";
        }
        else {
            return allocationType.toString() + "* Allocation Rate: " + allocatedMemory.toString() + "/sec";
        }
    }

    public String toString(Unit unit) {
        if (endLogEntryTimestamp - initLogEntryTimestamp > 1000) {
            return allocationType.toString() + " Allocation Rate: " + allocatedMemory.convertTo(unit).toString() + "/sec";
        }
        else {
            return allocationType.toString() + "* Allocation Rate: " + allocatedMemory.convertTo(unit).toString() + "/sec";
        }
    }
}
