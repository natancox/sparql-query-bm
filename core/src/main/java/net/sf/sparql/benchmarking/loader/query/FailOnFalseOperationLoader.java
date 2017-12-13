package net.sf.sparql.benchmarking.loader.query;

import net.sf.sparql.benchmarking.operations.Operation;
import net.sf.sparql.benchmarking.operations.query.fail.FailOnFalseQueryOperation;

public class FailOnFalseOperationLoader extends AbstractQueryOperationLoader {

  @Override
  protected Operation createQueryOperation(String name, String query) {
    return new FailOnFalseQueryOperation(name, query);
  }

  @Override
  public String getPreferredName() {
    return "fail-on-false";
  }

  @Override
  public String getDescription() {
    return "The query operation makes a fixed ASK SPARQL query against a remote SPARQL service via HTTP and fails on false";
  }

}
