/*-
 * ---license-start
 * keycloak-config-cli
 * ---
 * Copyright (C) 2017 - 2021 adorsys GmbH & Co. KG @ https://adorsys.com
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---license-end
 */

package de.adorsys.keycloak.config.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.NullNode;
import de.adorsys.keycloak.config.exception.ImportProcessingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonUtilTest {

    @Test
    void fromJson_shouldThrowOnNull() {
        assertThrows(ImportProcessingException.class, () -> JsonUtil.fromJson("{3"));
    }

    @Test
    void fromJson_shouldWrapJsonProcessingException() {
        var exception =
            assertThrows(ImportProcessingException.class, () -> JsonUtil.fromJson("{4"));

        assertThat(exception.getCause(), instanceOf(JsonProcessingException.class));
    }

    @Test
    void toJson_shouldWrapIOException() {
        var object = new FailingStringSerialization();

        var exception =
            assertThrows(ImportProcessingException.class, () -> JsonUtil.toJson(object));

        assertThat(exception.getCause(), instanceOf(IOException.class));
    }

    @Test
    void getJsonOrNullNode_shouldNotProduceNullPointer() {
        assertThat(JsonUtil.getJsonOrNullNode(null), instanceOf(NullNode.class));
    }

    @Test
    void getJsonOrNullNode_shouldWrapJsonProcessingException() {
        var exception =
            assertThrows(ImportProcessingException.class, () -> JsonUtil.getJsonOrNullNode("{5"));

        assertThat(exception.getCause(), instanceOf(JsonProcessingException.class));
    }

    @Test
    void readValue_shouldNotProduceNullPointer() {
        assertThat(JsonUtil.readValue(null, Integer.class), nullValue());
    }

    @Test
    void readValue_shouldMapLongCorrectly() {
        assertThat(JsonUtil.readValue("123", Long.class), equalTo(123L));
    }

    @Test
    void readValue_shouldWrapJsonProcessingException() {
        var exception =
                assertThrows(ImportProcessingException.class, () -> JsonUtil.readValue("{6", Object.class));

        assertThat(exception.getCause(), instanceOf(JsonProcessingException.class));
    }

    private class FailingStringSerialization {

        @Override
        public String toString() {
            throw new RuntimeException();
        }

    }
}
