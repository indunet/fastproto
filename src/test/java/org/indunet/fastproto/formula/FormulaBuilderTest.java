package org.indunet.fastproto.formula;

import lombok.val;
import lombok.var;
import org.indunet.fastproto.Formula;
import org.indunet.fastproto.FormulaBuilder;
import org.indunet.fastproto.compiler.JavaStringCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FormulaBuilderTest {
    @Test
    public void testBuild() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        val compiler = new JavaStringCompiler();
        StringBuffer lambda = new StringBuffer("x -> x * 0.1");

        if (lambda.toString().endsWith(";") || lambda.toString().endsWith("}")) {

        } else {
            lambda.append(";");
        }

        val results = compiler.compile("Formula01.java", String.format(Formula.TEMPLATE(), "Formula01", lambda));
        val clazz = compiler.loadClass("org.indunet.fastproto.formula.Formula01", results);

        val builder = (FormulaBuilder) clazz.newInstance();
        assertNotNull(builder.build());
        System.out.println(builder.build().apply(10));
    }
}
