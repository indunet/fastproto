package org.indunet.fastproto.tuple;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deng Ran
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public class Triple<A, B, C> {
    A c1;
    B c2;
    C c3;
}
