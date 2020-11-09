package org.indunet.fastproto.assist;

import org.indunet.fastproto.object.Motor;
import org.indunet.fastproto.object.Tesla;
import org.junit.Test;

public class ObjectAssistTest {
    @Test
    public void testCreate() {
        Motor motor = new Motor();
        Tesla tesla = new Tesla();

        ObjectAssist objectAssist = ObjectAssist.create(tesla);
        System.out.println(objectAssist);
    }
}
