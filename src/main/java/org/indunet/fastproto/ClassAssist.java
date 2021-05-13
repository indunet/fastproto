package org.indunet.fastproto;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.decoder.TypeDecoder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class ClassAssist {
    ClassAssist parent;
    Class<?> type;
    Optional<Field> field;
    Optional<Annotation> dataType;
    Class<? extends TypeDecoder> decoder;
    Class<? extends TypeDecoder> encoder;
    Optional<Class<? extends Function>> decodeFormula;
    Optional<Class<? extends Function>> encodeFormula;
    EndianPolicy endianPolicy;

    List<ClassAssist> children;

    public void addChild(ClassAssist assist) {
        this.children.add(assist);
    }

    public static ClassAssist create(Class<?> clazz) {
        ClassAssistBuilder builder = ClassAssist.builder();

        builder.type(clazz);

        return builder.build();
    }
}
