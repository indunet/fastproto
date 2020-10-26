package org.vnet.fastproto.formula;

public interface Formula<I, O> {
    O execute(I input);
}
