package org.indunet.fastproto.tuple;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deng Ran
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public class Pair<A, B> {
    A c1;
    B c2;

    public <C> Triple<A, B, C> append(C c3) {
        return new Triple(c1, c2, c3);
    }
}
