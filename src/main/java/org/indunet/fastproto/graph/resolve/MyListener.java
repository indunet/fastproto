package org.indunet.fastproto.graph.resolve;

import lombok.SneakyThrows;
import org.indunet.fastproto.exception.CodecException;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

/**
 * @author Chance
 * @since 1.0.0
 */
public class MyListener implements RuleListener {
    @SneakyThrows
    public void onFailure(Rule rule, Facts facts, Exception exception) {
        throw exception;
    }

}
