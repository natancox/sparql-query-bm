/** 
 * Copyright 2011-2014 Cray Inc. All Rights Reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name Cray Inc. nor the names of its contributors may be
 *   used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/
package net.sf.sparql.benchmarking.operations;

import java.util.Iterator;

import net.sf.sparql.benchmarking.stats.OperationMixStats;

/**
 * Represents a mix of operations carried out as a single test run
 * 
 * @author rvesse
 * 
 */
public interface OperationMix {

    /**
     * Gets the operations in this mix
     * 
     * @return Operations
     */
    public abstract Iterator<Operation> getOperations();

    /**
     * Gets the operation with the specified ID
     * <p>
     * Generally it should be assumed that operation IDs are allocated using a
     * zero based index so 0 would obtain the first operation in the mix while
     * {@code size()-1} would obtain the last operation in the mix. All the
     * built-in implementations of this interface follow this rule.
     * </p>
     * 
     * @param id
     *            ID
     * @return Operation
     * @throws IllegalArgumentException
     *             Thrown if the ID is not valid
     */
    public abstract Operation getOperation(int id);

    /**
     * Gets the number of operations in the operation mix
     * 
     * @return Number of operations
     */
    public abstract int size();

    /**
     * Gets the statistics for the operation mix
     * 
     * @return Statistics
     */
    public abstract OperationMixStats getStats();

}