package org.indunet.fastproto.domain;

import lombok.ToString;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.annotation.type.IntegerType;

@ToString
@Endian(EndianPolicy.LITTLE)
// @Datagram("Motor")
public class Motor {
    @Endian(EndianPolicy.BIG)
    @IntegerType(value = 0)
    public int voltage;
    @FloatType(value = 4)
    public float speed;

    public void beforeDecode() {
        System.out.println(this.getClass() + " before decode.");
    }

    public void afterDecode() {
        System.out.println(this.getClass() + " after decode.");
    }

    public void beforeEncode() {
        System.out.println(this.getClass() + " before encode.");
    }

    public void afterEncode() {
        System.out.println(this.getClass() + " after encode.");
    }
}
