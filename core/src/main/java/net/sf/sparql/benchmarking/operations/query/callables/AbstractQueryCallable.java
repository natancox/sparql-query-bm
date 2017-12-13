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

package net.sf.sparql.benchmarking.operations.query.callables;

import org.apache.jena.atlas.web.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;

import net.sf.sparql.benchmarking.operations.AbstractOperationCallable;
import net.sf.sparql.benchmarking.options.Options;
import net.sf.sparql.benchmarking.runners.Runner;
import net.sf.sparql.benchmarking.stats.OperationRun;
import net.sf.sparql.benchmarking.stats.impl.QueryRun;
import net.sf.sparql.benchmarking.util.ConvertUtils;
import net.sf.sparql.benchmarking.util.ErrorCategories;
import net.sf.sparql.benchmarking.util.FormatUtils;

/**
 * Abstract callable for queries
 * 
 * @author rvesse
 * 
 * @param <T>
 *            Options type
 */
public abstract class AbstractQueryCallable<T extends Options> extends AbstractOperationCallable<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractQueryCallable.class);

    /**
     * Creates a new callable
     * 
     * @param runner
     *            Runner
     * @param options
     *            Options
     */
    public AbstractQueryCallable(Runner<T> runner, T options) {
        super(runner, options);
    }

    /**
     * Gets the query to be run
     * 
     * @return Query
     */
    protected abstract Query getQuery();

    /**
     * Counts the results for queries that return a boolean
     * <p>
     * The default implementation always returns {@code 1}
     * </p>
     * 
     * @param options
     *            Options
     * @param result
     *            Result
     * @return Number of results
     */
    protected long countResults(T options, boolean result) {
        return 1;
    }

    /**
     * Counts results for queries that return a model.
     * <p>
     * The default implementation returns the size of the model
     * </p>
     * 
     * @param options
     *            Options
     * @param m
     *            Model
     * @return Number of results
     */
    protected long countResults(T options, Model m) {
        return m.size();
    }

    /**
     * Counts results for queries that return a result set
     * <p>
     * The default implementation either returns {@link OperationRun#UNKNOWN} if
     * the options indicate that counting is disabled or iterates over the
     * results to count them.
     * </p>
     * 
     * @param options
     *            Options
     * @param rset
     *            Result Set
     * @return Number of results
     */
    protected long countResults(T options, ResultSet rset) {
        // Result Counting may be skipped depending on user options
        if (options.getNoCount()) {
            return OperationRun.UNKNOWN;
        }

        // Count Results
        long numResults = 0;
        long localLimit = options.getLocalLimit();
        while (rset.hasNext() && !isCancelled() && (localLimit <= 0 || numResults < localLimit)) {
            numResults++;
            rset.next();
        }
        return numResults;
    }

    /**
     * Provides derived implementations the option to customize the query
     * execution before actually executing the query e.g. to add custom
     * parameters
     * <p>
     * The default implementation does nothing.
     * </p>
     * 
     * @param qe
     *            Query Execution
     */
    protected void customizeRequest(QueryExecution qe) {
        // Does nothing by default
    }

    /**
     * Runs the Query counting the number of Results
     */
    @Override
    public QueryRun call() {
        T options = this.getOptions();

        Query query = this.getQuery();

        // Impose Limit if applicable
        if (options.getLimit() > 0) {
            if (!query.isAskType()) {
                if (query.getLimit() == Query.NOLIMIT || query.getLimit() > options.getLimit()) {
                    query.setLimit(options.getLimit());
                }
            }
        }

        logger.debug("Running query:\n{}", query);

        // Create query execution
        QueryExecution exec = this.createQueryExecution(query);
        this.customizeRequest(exec);

        long numResults = 0;
        long responseTime = OperationRun.NOT_YET_RUN;
        long startTime = System.nanoTime();
        try {

            // Make the query
            if (query.isAskType()) {
                boolean result = exec.execAsk();
                numResults = countResults(options, result);
            } else if (query.isConstructType()) {
                Model m = exec.execConstruct();
                numResults = countResults(options, m);
            } else if (query.isDescribeType()) {
                Model m = exec.execDescribe();
                numResults = countResults(options, m);
            } else if (query.isSelectType()) {
                ResultSet rset = exec.execSelect();
                responseTime = System.nanoTime() - startTime;

                // Abort if we have been cancelled by the time the engine
                // responds
                if (isCancelled()) {
                    return null;
                }
                this.getRunner().reportPartialProgress(options,
                        "started responding in " + ConvertUtils.toSeconds(responseTime) + "s...");
                numResults = countResults(options, rset);
            } else {
                logger.warn("Query is not of a recognised type and so was not run");
                if (options.getHaltAny())
                    this.getRunner().halt(options, "Unrecognized Query Type");
            }

            // Abort if we have been cancelled by the time the engine
            // responds
            if (isCancelled()) {
                return null;
            }

            // Return results
            long endTime = System.nanoTime();
            return new QueryRun(endTime - startTime, responseTime, numResults);

        } catch (HttpException e) {
            // Make sure to categorize HTTP errors appropriately
            logger.error("{}", FormatUtils.formatException(e));
            return new QueryRun(e.getMessage(), ErrorCategories.categorizeHttpError(e), System.nanoTime() - startTime);
        } catch (QueryExceptionHTTP e) {
            logger.error("{}", FormatUtils.formatException(e));
            return new QueryRun(e.getMessage(), ErrorCategories.categorizeHttpError(e), System.nanoTime() - startTime);
        } finally {
            // Clean up query execution
            if (exec != null)
                exec.close();
        }
    }

    /**
     * Creates the query execution to use for the query
     * 
     * @param query
     *            Query
     * @return Query execution
     */
    protected abstract QueryExecution createQueryExecution(Query query);

}