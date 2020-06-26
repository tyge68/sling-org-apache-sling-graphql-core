/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.graphql.core.engine;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;

import static org.junit.Assert.assertThat;

import javax.script.ScriptException;

import static org.hamcrest.Matchers.equalTo;

import org.apache.sling.graphql.core.mocks.AddressDataFetcher;
import org.apache.sling.graphql.core.mocks.TestUtil;
import org.junit.Test;

public class CustomScalarsTest extends ResourceQueryTestBase {
    protected String getTestSchemaName() {
        return "scalars-schema";
    }

    protected void setupDataFetchers() {
        TestUtil.registerSlingDataFetcher(context.bundleContext(), "scalars/address", new AddressDataFetcher());
    }

    @Test
    public void urlScalar() throws Exception {
        final String url = "http://www.perdu.com";
        final String query = String.format("{ address (url: \"%s\") { url hostname } }", url);
        final String json = queryJSON(query);
        assertThat(json, hasJsonPath("$.data.address.hostname", equalTo("WWW.PERDU.COM")));
        assertThat(json, hasJsonPath("$.data.address.url", equalTo("URLCoercing says:" + url)));
    }

    @Test(expected = ScriptException.class)
    public void urlSyntaxError() throws Exception {
        final String url = "This is not an URL!";
        final String query = String.format("{ address (url: \"%s\") { url hostname } }", url);
        queryJSON(query);
    }

}