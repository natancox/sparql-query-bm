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

import net.sf.sparql.benchmarking.operations.query.DatasetSizeOperation;
import net.sf.sparql.benchmarking.options.Options;
import net.sf.sparql.benchmarking.runners.Runner;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.util.NodeFactoryExtra;

/**
 * A callable for getting the value of a specific variable in the first row of a
 * result set as a long
 * <p>
 * Usually used in conjunction with custom operations like
 * {@link DatasetSizeOperation} which calculate some aggregate on the data using
 * a {@code SELECT} query and want to return that aggregate value as the number
 * of results rather than the number of results rows as the basic
 * {@link RemoteQueryCallable} would return.
 * </p>
 * 
 * @author rvesse
 * 
 * @param <T>
 *            Options type
 * @param <TCallable>
 *            Callable type
 */
public class LongValueCallable<T extends Options, TCallable extends AbstractQueryCallable<T>> extends
        AbstractScalarValueCallable<T, TCallable> {

    /**
     * Creates a new callable
     * 
     * @param var
     *            Variable whose value is to be retrieved
     * @param runner
     *            Runner
     * @param options
     *            Options
     * @param callable
     *            Callable to decorate
     */
    public LongValueCallable(Runner<T> runner, T options, TCallable callable, String var) {
        super(runner, options, callable, var);
    }

    @Override
    protected long nodeToLong(Node n) {
        if (n == null)
            return 0;
        return NodeFactoryExtra.nodeToLong(n);
    }

}
