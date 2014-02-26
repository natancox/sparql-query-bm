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

package net.sf.sparql.benchmarking.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jena.atlas.web.auth.HttpAuthenticator;

import net.sf.sparql.benchmarking.monitoring.ProgressListener;
import net.sf.sparql.benchmarking.operations.OperationMix;
import net.sf.sparql.benchmarking.runners.mix.OperationMixRunner;

/**
 * Implementation of generic options
 * 
 * @author rvesse
 * 
 */
public class OptionsImpl implements Options {

    private boolean haltOnTimeout = false;
    private boolean haltOnError = false;
    private boolean haltAny = false;
    private HaltBehaviour haltBehaviour = DEFAULT_HALT_BEHAVIOUR;
    private List<ProgressListener> listeners = new ArrayList<ProgressListener>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private OperationMix operationMix;
    private String queryEndpoint;
    private String updateEndpoint;
    private String graphStoreEndpoint;
    private Map<String, String> customEndpoints = new HashMap<String, String>();
    private int timeout = DEFAULT_TIMEOUT;
    private String selectResultsFormat = DEFAULT_FORMAT_SELECT;
    private String askResultsFormat = DEFAULT_FORMAT_SELECT;
    private String graphResultsFormat = DEFAULT_FORMAT_GRAPH;
    private int delay = DEFAULT_MAX_DELAY;
    private int parallelThreads = DEFAULT_PARALLEL_THREADS;
    private boolean allowCompression = false;
    private HttpAuthenticator authenticator;
    private AtomicLong globalOrder = new AtomicLong(0);
    private boolean randomize = true;
    int sanity = DEFAULT_SANITY_CHECKS;
    private OperationMix setupMix;
    private OperationMix teardownMix;
    private OperationMixRunner mixRunner;

    @Override
    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public void setHaltOnTimeout(boolean halt) {
        haltOnTimeout = halt;
    }

    @Override
    public boolean getHaltOnTimeout() {
        return haltOnTimeout;
    }

    @Override
    public void setHaltOnError(boolean halt) {
        haltOnError = halt;
    }

    @Override
    public boolean getHaltOnError() {
        return haltOnError;
    }

    @Override
    public void setHaltAny(boolean halt) {
        haltAny = halt;
        if (halt) {
            haltOnError = true;
            haltOnTimeout = true;
        }
    }

    @Override
    public boolean getHaltAny() {
        return haltAny;
    }

    @Override
    public void setHaltBehaviour(HaltBehaviour behaviour) {
        haltBehaviour = behaviour;
    }

    @Override
    public HaltBehaviour getHaltBehaviour() {
        return haltBehaviour;
    }

    @Override
    public List<ProgressListener> getListeners() {
        return this.listeners;
    }

    @Override
    public void addListener(ProgressListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void removeListener(ProgressListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public OperationMix getOperationMix() {
        return operationMix;
    }

    @Override
    public void setOperationMix(OperationMix queries) {
        operationMix = queries;
    }

    @Override
    public void setQueryEndpoint(String endpoint) {
        this.queryEndpoint = endpoint;
    }

    @Override
    public String getQueryEndpoint() {
        return queryEndpoint;
    }

    @Override
    public void setUpdateEndpoint(String endpoint) {
        this.updateEndpoint = endpoint;
    }

    @Override
    public String getUpdateEndpoint() {
        return updateEndpoint;
    }

    @Override
    public void setGraphStoreEndpoint(String endpoint) {
        this.graphStoreEndpoint = endpoint;
    }

    @Override
    public String getGraphStoreEndpoint() {
        return graphStoreEndpoint;
    }

    @Override
    public void setCustomEndpoint(String name, String endpoint) {
        this.customEndpoints.put(name, endpoint);
    }

    @Override
    public String getCustomEndpoint(String name) {
        return customEndpoints.get(name);
    }

    @Override
    public Map<String, String> getCustomEndpoints() {
        return Collections.unmodifiableMap(this.customEndpoints);
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setResultsAskFormat(String contentType) {
        askResultsFormat = contentType;
    }

    @Override
    public String getResultsAskFormat() {
        return askResultsFormat;
    }

    @Override
    public void setResultsSelectFormat(String contentType) {
        selectResultsFormat = contentType;
    }

    @Override
    public String getResultsSelectFormat() {
        return selectResultsFormat;
    }

    @Override
    public void setResultsGraphFormat(String contentType) {
        graphResultsFormat = contentType;
    }

    @Override
    public String getResultsGraphFormat() {
        return graphResultsFormat;
    }

    @Override
    public void setAllowCompression(boolean allowed) {
        allowCompression = allowed;
    }

    @Override
    public boolean getAllowCompression() {
        return allowCompression;
    }

    @Override
    public void setAuthenticator(HttpAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public HttpAuthenticator getAuthenticator() {
        return this.authenticator;
    }

    @Override
    public void setMaxDelay(int milliseconds) {
        if (delay < 0)
            delay = 0;
        delay = milliseconds;
    }

    @Override
    public int getMaxDelay() {
        return delay;
    }

    @Override
    public void setParallelThreads(int threads) {
        if (threads < 1)
            threads = 1;
        parallelThreads = threads;
    }

    @Override
    public int getParallelThreads() {
        return parallelThreads;
    }

    @Override
    public long getGlobalOrder() {
        return globalOrder.incrementAndGet();
    }

    @Override
    public void resetGlobalOrder() {
        globalOrder.set(0);
    }

    @Override
    public void setRandomizeOrder(boolean randomize) {
        this.randomize = randomize;
    }

    @Override
    public boolean getRandomizeOrder() {
        return randomize;
    }

    @Override
    public void setSanityCheckLevel(int level) {
        sanity = level;
    }

    @Override
    public int getSanityCheckLevel() {
        return sanity;
    }

    @Override
    public void setSetupMix(OperationMix mix) {
        this.setupMix = mix;
    }

    @Override
    public OperationMix getSetupMix() {
        return this.setupMix;
    }

    @Override
    public void setTeardownMix(OperationMix mix) {
        this.teardownMix = mix;
    }

    @Override
    public OperationMix getTeardownMix() {
        return this.teardownMix;
    }

    @Override
    public void setMixRunner(OperationMixRunner runner) {
        this.mixRunner = runner;
    }

    @Override
    public OperationMixRunner getMixRunner() {
        return this.mixRunner;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Options> T copy() {
        OptionsImpl copy = new OptionsImpl();
        this.copyStandardOptions(copy);
        return (T) copy;
    }

    /**
     * Copies standard options across from this instance
     * <p>
     * Primarily intended for use by derived implementations which extend the
     * basic options to make it easier for them to create copies of themselves.
     * </p>
     * 
     * @param copy
     *            Copy to copy to
     */
    protected final void copyStandardOptions(OptionsImpl copy) {
        copy.setAllowCompression(this.getAllowCompression());
        copy.setAuthenticator(this.getAuthenticator());
        for (String key : this.customEndpoints.keySet()) {
            copy.setCustomEndpoint(key, this.getCustomEndpoint(key));
        }
        copy.setGraphStoreEndpoint(this.getGraphStoreEndpoint());
        copy.setHaltAny(this.getHaltAny());
        copy.setHaltBehaviour(this.getHaltBehaviour());
        copy.setHaltOnError(this.getHaltOnError());
        copy.setHaltOnTimeout(this.getHaltOnTimeout());
        copy.setMaxDelay(this.getMaxDelay());
        copy.setOperationMix(this.getOperationMix());
        copy.setParallelThreads(this.getParallelThreads());
        copy.setQueryEndpoint(this.getQueryEndpoint());
        copy.setRandomizeOrder(this.getRandomizeOrder());
        copy.setResultsAskFormat(this.getResultsAskFormat());
        copy.setResultsGraphFormat(this.getResultsGraphFormat());
        copy.setResultsSelectFormat(this.getResultsSelectFormat());
        copy.setSanityCheckLevel(this.getSanityCheckLevel());
        copy.setSetupMix(this.getSetupMix());
        copy.setTeardownMix(this.getTeardownMix());
        copy.setTimeout(this.getTimeout());
        copy.globalOrder.set(this.globalOrder.get());
    }
}