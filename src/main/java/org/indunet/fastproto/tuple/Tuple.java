package org.indunet.fastproto.tuple;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class Tuple {
    public static <A, B> Pair<A, B> get(A c1, B c2) {
        return new Pair<>(c1, c2);
    }

    public static <A, B, C> Triple<A, B, C> get(A c1, B c2, C c3) {
        return new Triple<>(c1, c2, c3);
    }
}
