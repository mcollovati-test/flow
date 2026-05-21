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

class SetPropertyActionTest {

    @Test
    void literalString_emitsAssignmentWithJsonValue() {
        assertEquals("$0[\"value\"] = \"x\"",
                emit(new SetPropertyAction<>(new TagComponent("input"), "value",
                        "x")));
    }

    @Test
    void literalNull_emitsAssignmentToLiteralNull() {
        // Cast disambiguates the literal-T constructor from the
        // Input<? extends T> overload.
        assertEquals("$0[\"label\"] = null",
                emit(new SetPropertyAction<String>(new TagComponent("button"),
                        "label", (String) null)));
    }

    @Test
    void inputSource_emitsAssignmentWithInputExpression() {
        TagComponent host = new TagComponent("button");
        TagComponent target = new TagComponent("input");
        TagComponent source = new TagComponent("input");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        StringBuilder out = new StringBuilder();
        new SetPropertyAction<>(target, "value",
                new PropertyInput<>(source, "value", String.class))
                .appendStatement(builder, out);

        assertEquals("$0[\"value\"] = $1[\"value\"]", out.toString());
    }

    @Test
    void propertyNameWithQuotes_isEscapedAndCannotBreakOutOfBrackets() {
        TagComponent host = new TagComponent("button");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        StringBuilder out = new StringBuilder();
        new SetPropertyAction<>(host, "a\"b", true).appendStatement(builder,
                out);

        assertEquals("this[\"a\\\"b\"] = true", out.toString());
    }

    private static String emit(SetPropertyAction<?> action) {
        TagComponent host = new TagComponent("button");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));
        StringBuilder out = new StringBuilder();
        action.appendStatement(builder, out);
        return out.toString();
    }
}
