/*
Copyright 2011-2014 Cray Inc. All Rights Reserved

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

 * Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in the
  documentation and/or other materials provided with the distribution.

 * Neither the name Cray Inc. nor the names of its contributors may be
  used to endorse or promote products derived from this software
  without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

package net.sf.sparql.benchmarking.stats.impl;

import net.sf.sparql.benchmarking.stats.OperationRun;

/**
 * Represents a run of a single query
 * 
 * @author rvesse
 * 
 */
public class QueryRun extends AbstractOperationRun implements OperationRun {

    /**
     * Creates a Query Run which represents that the running of a query resulted
     * in an error
     * 
     * @param error
     *            Error Message
     * @param category
     *            Error Category
     * @param runtime
     *            Runtime, this is the amount of time elapsed until the
     *            error/timeout was reached
     */
    public QueryRun(String error, int category, long runtime) {
        super(error, category, runtime);
    }

    /**
     * Creates a Query run which represents the results of running a query
     * 
     * @param runtime
     *            Runtime
     * @param resultCount
     *            Result Count
     */
    private QueryRun(long runtime, long resultCount) {
        super(runtime, resultCount);
    }

    /**
     * Creates a Query run which represents the results of running a query
     * 
     * @param runtime
     *            Runtime
     * @param responseTime
     *            Response Time
     * @param resultCount
     *            Result Count
     */
    public QueryRun(long runtime, long responseTime, long resultCount) {
        super(runtime, responseTime, resultCount);
    }
}
