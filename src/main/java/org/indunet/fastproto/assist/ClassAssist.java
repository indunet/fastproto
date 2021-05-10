package org.indunet.fastproto.assist;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Data
@Builder
public class ClassAssist {
    Class<?> clazz;
    EndianPolicy endianPolicy;

    List<Method> beforeDecodes;
    List<Method> afterDecodes;
    List<Method> beforeEncodes;
    List<Method> afterEncodes;

    List<FieldAssist> fieldAssists;

    public static ClassAssist create(Class<?> clazz) {
        return  ClassAssist.builder()
                    .clazz(clazz)
                    .endianPolicy(Optional.ofNullable(clazz.getAnnotation(Endian.class))
                        .map(Endian::value)
                        .orElse(EndianPolicy.Little))
                    .fieldAssists(Arrays.stream(clazz.getDeclaredFields())
                            .map(f -> FieldAssist.create(clazz, f))
                            .peek(a -> a.setParent(clazz))
                            .collect(Collectors.toList()))
                    .build();
    }

    public void decode() {

    }
}
