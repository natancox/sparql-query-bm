package net.sf.sparql.benchmarking.operations.query.fail;

import net.sf.sparql.benchmarking.operations.query.callables.RemoteQueryCallable;
import net.sf.sparql.benchmarking.options.Options;
import net.sf.sparql.benchmarking.runners.Runner;
import net.sf.sparql.benchmarking.stats.OperationRun;
import net.sf.sparql.benchmarking.stats.impl.QueryRun;
import net.sf.sparql.benchmarking.util.ConvertUtils;
import net.sf.sparql.benchmarking.util.ErrorCategories;
import net.sf.sparql.benchmarking.util.FormatUtils;
import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.sf.sparql.benchmarking.util.ErrorCategories.EXECUTION;

public class FailOnFalseQueryCallable<T extends Options> extends RemoteQueryCallable<T> {

  private static final Logger logger = LoggerFactory.getLogger(FailOnFalseQueryCallable.class);

  /**
   * Creates a new Query Runner
   *
   * @param q       Query to run
   * @param runner  Runner
   * @param options
   */
  public FailOnFalseQueryCallable(Query q, Runner<T> runner, T options) {
    super(q, runner, options);
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
      QueryRun failedQueryRun = null;

      // Make the query
      if (query.isAskType()) {
        boolean result = exec.execAsk();

        if (!result) {
          String message = "ASK query failed: " + query;
          failedQueryRun = new QueryRun(message, EXECUTION, System.nanoTime() - startTime);
        }

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

      if (failedQueryRun != null) return failedQueryRun;

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

}
