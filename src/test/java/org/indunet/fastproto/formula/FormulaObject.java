package org.indunet.fastproto.formula;

import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.util.BinaryUtils;

@Data
public class FormulaObject {
    @UInt16Type(offset = 0)
    @DecodingFormula(lambda = "x -> x * 0.1")
    @EncodingFormula(lambda = "x -> (int) (x * 10)")
    Double speed = 8.1;

    public byte[] toBytes() {
        return BinaryUtils.int16Of(81, EndianPolicy.LITTLE);
    }
}
