package org.indunet.fastproto;

import org.indunet.fastproto.util.ObjectInfo;
import org.junit.Test;

public class ObjectInfoTest {
    @Test
    public void testCreate() {
        Motor motor = new Motor();
        Vehicle vehicle = new Vehicle();

        ObjectInfo objectInfo = ObjectInfo.create(vehicle);
        System.out.println(objectInfo);
    }
}
