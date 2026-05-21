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

/**
 * TriggerTest already covers screenX/screenY end-to-end. This test pins the
 * remaining factory methods (clientX/Y plus the four modifier keys) by
 * inspecting the JS expression each produces — proves the rest of
 * ClickTrigger's surface is wired correctly.
 */
class ClickTriggerTest {

    @Test
    void clientCoordinatesAndModifierKeys_renderMatchingEventProperties() {
        TagComponent host = new TagComponent("button");
        ClickTrigger click = new ClickTrigger(host);
        JsBuilder builder = new JsBuilder(click);

        assertEquals("event[\"clientX\"]", emit(click.clientX(), builder));
        assertEquals("event[\"clientY\"]", emit(click.clientY(), builder));
        assertEquals("event[\"shiftKey\"]", emit(click.shiftKey(), builder));
        assertEquals("event[\"ctrlKey\"]", emit(click.ctrlKey(), builder));
        assertEquals("event[\"altKey\"]", emit(click.altKey(), builder));
        assertEquals("event[\"metaKey\"]", emit(click.metaKey(), builder));
    }

    private static String emit(Action.Input<?> input, JsBuilder builder) {
        StringBuilder out = new StringBuilder();
        input.appendExpression(builder, out);
        return out.toString();
    }
}
