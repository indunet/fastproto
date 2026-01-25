/*
 * Copyright 2019-2022 indunet.org
 *
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
 */

package org.indunet.fastproto.formula;

import lombok.val;
import org.indunet.fastproto.exception.ResolvingException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test of Formula Builder.
 *
 * @author Deng Ran
 * @since 3.7.0
 */
public class FormulaBuilderTest {
    private static String originalLambdaEnabled;

    @BeforeAll
    public static void setUp() {
        // Save original value
        originalLambdaEnabled = System.getProperty(FormulaBuilder.LAMBDA_ENABLED_PROPERTY);
    }

    @AfterAll
    public static void tearDown() {
        // Restore original value
        if (originalLambdaEnabled == null) {
            System.clearProperty(FormulaBuilder.LAMBDA_ENABLED_PROPERTY);
        } else {
            System.setProperty(FormulaBuilder.LAMBDA_ENABLED_PROPERTY, originalLambdaEnabled);
        }
    }

    @Test
    public void testBuildWithDynamicCompilationEnabled() {
        // Enable dynamic compilation for this test
        System.setProperty(FormulaBuilder.LAMBDA_ENABLED_PROPERTY, "true");
        
        val lambda = "x -> x * 0.1";
        val builder = FormulaBuilder.create(Integer.class, lambda);

        assertNotNull(builder.build());
    }

    @Test
    public void testBuildThrowsWhenDynamicCompilationDisabled() {
        // Ensure dynamic compilation is disabled (default)
        System.clearProperty(FormulaBuilder.LAMBDA_ENABLED_PROPERTY);
        
        val lambda = "x -> x * 0.1";
        
        ResolvingException exception = assertThrows(ResolvingException.class, 
                () -> FormulaBuilder.create(Integer.class, lambda));
        
        assertTrue(exception.getMessage().contains("disabled"));
    }

    @Test
    public void testLambdaEnabledPropertyConstant() {
        assertEquals("fastproto.lambda.enabled", FormulaBuilder.LAMBDA_ENABLED_PROPERTY);
    }
}
