package org.indunet.fastproto.formula;

@FunctionalInterface
public interface Formula<I, O> {
    O transform(I input);
}
