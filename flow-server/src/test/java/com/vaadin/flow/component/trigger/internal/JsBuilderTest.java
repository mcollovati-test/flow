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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class JsBuilderTest {

    @Test
    void reference_hostElement_returnsThis_noCaptures() {
        TagComponent host = new TagComponent("button");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        assertEquals("this", builder.reference(host.getElement()));
        assertEquals(0, builder.captures().length);
    }

    @Test
    void reference_nonHostElements_getSequentialPlaceholders() {
        TagComponent host = new TagComponent("button");
        TagComponent a = new TagComponent("input");
        TagComponent b = new TagComponent("input");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        assertEquals("$0", builder.reference(a.getElement()));
        assertEquals("$1", builder.reference(b.getElement()));
    }

    @Test
    void reference_sameElementTwice_reusesPlaceholder_dedupedInCaptures() {
        TagComponent host = new TagComponent("button");
        TagComponent a = new TagComponent("input");
        TagComponent b = new TagComponent("input");
        JsBuilder builder = new JsBuilder(new DomEventTrigger(host, "click"));

        builder.reference(a.getElement());
        builder.reference(b.getElement());
        assertEquals("$0", builder.reference(a.getElement()),
                "Second reference to the same element must return the"
                        + " same placeholder");

        assertArrayEquals(new Object[] { a.getElement(), b.getElement() },
                builder.captures(),
                "Captures should list each element once, in first-reference"
                        + " order");
    }

    @Test
    void json_encodesPrimitivesAndStrings() {
        assertEquals("\"hello\"", JsBuilder.json("hello"));
        assertEquals("42", JsBuilder.json(42));
        assertEquals("true", JsBuilder.json(true));
        assertEquals("null", JsBuilder.json(null));
    }

    @Test
    void json_escapesStringSpecialChars() {
        // A string with a quote must be safely encoded so it cannot break
        // out of the surrounding JS expression.
        assertEquals("\"a\\\"b\"", JsBuilder.json("a\"b"));
        assertEquals("\"a\\nb\"", JsBuilder.json("a\nb"));
    }

    @Test
    void trigger_returnsTheWrappedTrigger() {
        TagComponent host = new TagComponent("button");
        Trigger trigger = new DomEventTrigger(host, "click");

        assertSame(trigger, new JsBuilder(trigger).trigger());
    }
}
