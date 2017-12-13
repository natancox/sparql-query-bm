package net.sf.sparql.benchmarking.operations.query.fail;

import net.sf.sparql.benchmarking.operations.OperationCallable;
import net.sf.sparql.benchmarking.operations.query.AbstractRemoteQueryOperation;
import net.sf.sparql.benchmarking.options.Options;
import net.sf.sparql.benchmarking.runners.Runner;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

/**
 * Represents a query operation that will fail in case a fixed ASK query returns false
 *
 * @author natancox
 */
public class FailOnFalseQueryOperation extends AbstractRemoteQueryOperation {

  private final Query query;
  private final String queryString;

  /**
   * Creates a new Query
   *
   * @param name        Name of the query
   * @param queryString Query string
   */
  public FailOnFalseQueryOperation(String name, String queryString) {
    super(name);
    this.queryString = queryString;
    this.query = QueryFactory.create(this.queryString);
  }

  @Override
  public Query getQuery() {
    return this.query;
  }

  @Override
  public String getQueryString() {
    return this.queryString;
  }

  @Override
  public String getType() {
    return "Fail on FALSE Query";
  }

  @Override
  public <T extends Options> OperationCallable<T> createCallable(Runner<T> runner, T options) {
    return new FailOnFalseQueryCallable<>(this.getQuery(), runner, options);
  }

}
