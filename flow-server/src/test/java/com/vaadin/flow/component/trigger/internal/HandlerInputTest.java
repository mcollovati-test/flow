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
import static org.junit.jupiter.api.Assertions.assertThrows;

class HandlerInputTest {

    @Test
    void usedInOwningTrigger_emitsExpressionVerbatim() {
        TagComponent host = new TagComponent("button");
        Trigger owner = new DomEventTrigger(host, "click");
        HandlerInput<Integer> input = new HandlerInput<>("event[\"screenX\"]",
                owner);

        StringBuilder out = new StringBuilder();
        input.appendExpression(new JsBuilder(owner), out);

        assertEquals("event[\"screenX\"]", out.toString());
    }

    @Test
    void usedInDifferentTrigger_isRejectedWithIllegalArgumentException() {
        TagComponent host = new TagComponent("button");
        Trigger owner = new DomEventTrigger(host, "click");
        Trigger other = new DomEventTrigger(host, "keydown");
        HandlerInput<Integer> input = new HandlerInput<>("event[\"screenX\"]",
                owner);

        assertThrows(IllegalArgumentException.class, () -> input
                .appendExpression(new JsBuilder(other), new StringBuilder()));
    }
}
