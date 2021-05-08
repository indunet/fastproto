package org.indunet.fastproto.domain;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.annotation.type.IntegerType;

@Endian(EndianPolicy.Little)
// @Datagram("Motor")
public class Motor {
    @Endian(EndianPolicy.Big)
    @IntegerType(byteOffset = 0)
    public int voltage;
    @FloatType(byteOffset = 4)
    public float speed;

    @BeforeDecode
    public void beforeDecode() {
        System.out.println(this.getClass() + " before decode.");
    }

    @AfterDecode
    public void afterDecode() {
        System.out.println(this.getClass() + " after decode.");
    }

    @BeforeEncode
    public void beforeEncode() {
        System.out.println(this.getClass() + " before encode.");
    }

    @AfterEncode
    public void afterEncode() {
        System.out.println(this.getClass() + " after encode.");
    }
}
