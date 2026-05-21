/*
 * Copyright 2000-2026 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.trigger.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertyInputTest {

    @Test
    void nonHostTarget_emitsParameterizedBracketAccess_andCapturesTheElement() {
        TagComponent host = new TagComponent("button");
        TagComponent input = new TagComponent("input");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        StringBuilder out = new StringBuilder();
        new PropertyInput<>(input, "value", String.class)
                .appendExpression(builder, out);

        assertEquals("$0[\"value\"]", out.toString());
        assertEquals(1, builder.captures().length);
    }

    @Test
    void hostTarget_emitsThisAccess_andDoesNotAllocateCapture() {
        TagComponent host = new TagComponent("input");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "input"));

        StringBuilder out = new StringBuilder();
        new PropertyInput<>(host, "value", String.class)
                .appendExpression(builder, out);

        assertEquals("this[\"value\"]", out.toString());
        assertEquals(0, builder.captures().length);
    }

    @Test
    void propertyNameWithQuotes_isEscapedAndCannotBreakOutOfBrackets() {
        TagComponent host = new TagComponent("button");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        StringBuilder out = new StringBuilder();
        new PropertyInput<>(host, "a\"b", String.class)
                .appendExpression(builder, out);

        assertEquals("this[\"a\\\"b\"]", out.toString());
    }
}
