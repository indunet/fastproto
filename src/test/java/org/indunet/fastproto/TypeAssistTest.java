package org.indunet.fastproto;

import lombok.SneakyThrows;
import org.indunet.fastproto.annotation.type.AutoType;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.iot.tesla.Tesla;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @see TypeAssist
 * @since 1.0.0
 */
public class TypeAssistTest {
    @Test
    public void testOf() {
        TypeAssist assist = TypeAssist.of(Tesla.class);
        List<DecodeContext> contexts = assist.toDecodeContexts(new byte[100]);

        assertNotNull(contexts);
    }


    @AutoType(10)
    Boolean b;

    @SneakyThrows
    @Test
    public void testAnnotation() {
        AutoType auto = this.getClass().getDeclaredField("b").getAnnotation(AutoType.class);
        BooleanType type = (BooleanType) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {BooleanType.class}, (object, method, params) -> {
            return Arrays.stream(auto.getClass().getMethods())
                    .filter(m -> m.getName().equals(method.getName()))
                    .findAny()
                    .get()
                    .invoke(auto);
        });

        System.out.println(type.value());
        System.out.println(type.bitOffset());
        System.out.println(type.MAX_BIT_OFFSET);
    }
}
