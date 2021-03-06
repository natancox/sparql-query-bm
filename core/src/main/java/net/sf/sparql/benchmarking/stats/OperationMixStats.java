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

package net.sf.sparql.benchmarking.stats;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.sparql.benchmarking.parallel.ParallelTimer;

/**
 * Represents statistics for an operation mix
 * 
 * @author rvesse
 * 
 */
public interface OperationMixStats {

    /**
     * Gets an iterator over the operation mix runs
     * 
     * @return Mix Runs
     */
    public abstract Iterator<OperationMixRun> getRuns();

    /**
     * Gets the number of runs for which information has been recorded
     * 
     * @return Number of runs
     */
    public abstract long getRunCount();

    /**
     * Adds information for the given run to the statistics
     * 
     * @param run
     *            Run information
     */
    public abstract void add(OperationMixRun run);

    /**
     * Clears all run statistics
     */
    public abstract void clear();

    /**
     * Trims the worst and best N results
     * 
     * @param outliers
     *            Number of outliers to trim
     */
    public abstract void trim(int outliers);

    /**
     * Gets the total number of operations run across all mix runs
     * 
     * @return Total operations run
     */
    public abstract long getTotalOperations();

    /**
     * Gets the total number of errors over all runs
     * 
     * @return Total number of errors
     */
    public abstract long getTotalErrors();

    /**
     * Gets the information for all errors grouped by category
     * 
     * @return Errors grouped by category
     */
    public abstract Map<Integer, List<OperationRun>> getCategorizedErrors();

    /**
     * Gets the total runtime over all runs
     * 
     * @return Total Runtime in nanoseconds
     */
    public abstract long getTotalRuntime();

    /**
     * Gets the actual runtime for the mix over all runs (takes into account
     * queries that run in parallel)
     * 
     * @return Actual Runtime in nanoseconds
     */
    public abstract long getActualRuntime();

    /**
     * Gets the total response time over all runs
     * 
     * @return Total Response Time in nanoseconds
     */
    public abstract long getTotalResponseTime();

    /**
     * Gets the average runtime for the mix over all runs
     * 
     * @return Arithmetic Average Runtime in nanoseconds
     */
    public abstract long getAverageRuntime();

    /**
     * Gets the average actual runtime for the mix over all runs (takes into
     * account parallelization of operations)
     * 
     * @return Arithmetic Actual Average Runtime in nanoseconds
     */
    public abstract long getActualAverageRuntime();

    /**
     * Gets the average response time for the mix
     * 
     * @return Arithmetic Average Response in nanoseconds
     */
    public abstract long getAverageResponseTime();

    /**
     * Gets the average runtime for the mix over all runs (geometric mean)
     * 
     * @return Geometric Average Runtime in nanoseconds
     */
    public abstract double getGeometricAverageRuntime();

    /**
     * Gets the minimum runtime for a mix
     * 
     * @return Minimum Runtime in nanoseconds
     */
    public abstract long getMinimumRuntime();

    /**
     * Gets the maximum runtime for a mix
     * 
     * @return Maximum Runtime in nanoseconds
     */
    public abstract long getMaximumRuntime();

    /**
     * Gets the variance in mix runtime
     * 
     * @return Runtime Variance in nanoseconds squared
     */
    public abstract double getVariance();

    /**
     * Gets the standard deviation in mix runtime
     * 
     * @return Runtime Standard Deviation in nanoseconds
     */
    public abstract double getStandardDeviation();

    /**
     * Calculates the number of operation mixes per hour that could be executed
     * based on the average runtime of the operation mix
     * 
     * @return Operation Mixes per Hour
     */
    public abstract double getOperationMixesPerHour();

    /**
     * Calculates the number of operation mixes per hour that could be executed
     * based on the {@link #getActualAverageRuntime()}
     * 
     * @return Operation Mixes per Hour
     */
    public abstract double getActualOperationMixesPerHour();

    /**
     * Gets the parallel timer used to track actual runtime
     * 
     * @return Parallel timer
     */
    public abstract ParallelTimer getTimer();

}