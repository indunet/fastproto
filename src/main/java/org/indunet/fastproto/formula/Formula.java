package org.indunet.fastproto.formula;

public interface Formula<I, O> {
    O transform(I input);
}
