package org.indunet.fastproto.assist;

import org.indunet.fastproto.domain.Motor;
import org.indunet.fastproto.domain.Tesla;
import org.junit.Test;

public class ObjectAssistTest {
    @Test
    public void testCreate() {
        Motor motor = new Motor();
        Tesla tesla = new Tesla();

        ObjectAssist objectAssist = ObjectAssist.create(tesla.getClass());
        System.out.println(objectAssist);
    }
}
