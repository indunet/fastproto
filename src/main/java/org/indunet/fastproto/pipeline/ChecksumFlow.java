package org.indunet.fastproto.pipeline;

import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Checksum;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.annotation.UInt32Type;
import org.indunet.fastproto.annotation.UInt8Type;
import org.indunet.fastproto.checksum.ChecksumUtils;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.graph.Reference;

import java.lang.reflect.Field;
import java.util.List;

public class ChecksumFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val graph = context.getGraph();
        if (graph == null) {
            this.forward(context);
            return;
        }
        List<Reference> refs = graph.getValidReferences();
        if (context.getOutputStream() != null) {
            byte[] bytes = context.getOutputStream().toByteBuffer().toBytes();
            for (Reference r : refs) {
                Field f = r.getField();
                Checksum c = f.getAnnotation(Checksum.class);
                if (c != null) {
                    int crcOffset = getFieldOffset(f);
                    int length = crcOffset - c.offset();
                    long crc = ChecksumUtils.calculate(bytes, c.offset(), length, c.type());
                    writeChecksum(context, crcOffset, crc, c);
                }
            }
        } else if (context.getInputStream() != null) {
            byte[] bytes = context.getInputStream().toByteBuffer().toBytes();
            for (Reference r : refs) {
                Field f = r.getField();
                Checksum c = f.getAnnotation(Checksum.class);
                if (c != null) {
                    int crcOffset = getFieldOffset(f);
                    int length = crcOffset - c.offset();
                    long crc = ChecksumUtils.calculate(bytes, c.offset(), length, c.type());
                    long actual = readChecksum(bytes, crcOffset, c);
                    if (actual != crc) {
                        throw new DecodingException("Checksum validation failed");
                    }
                }
            }
        }
        this.forward(context);
    }

    private int getFieldOffset(Field field) {
        if (field.isAnnotationPresent(UInt8Type.class)) {
            return field.getAnnotation(UInt8Type.class).offset();
        } else if (field.isAnnotationPresent(UInt16Type.class)) {
            return field.getAnnotation(UInt16Type.class).offset();
        } else if (field.isAnnotationPresent(UInt32Type.class)) {
            return field.getAnnotation(UInt32Type.class).offset();
        }
        return 0;
    }

    private void writeChecksum(PipelineContext ctx, int offset, long crc, Checksum c) {
        switch (c.type()) {
            case CRC8:
                ctx.getOutputStream().writeUInt8(offset, (int) crc);
                break;
            case CRC16:
                ctx.getOutputStream().writeUInt16(offset, c.byteOrder(), (int) crc);
                break;
            case CRC32:
                ctx.getOutputStream().writeUInt32(offset, c.byteOrder(), crc);
                break;
        }
    }

    private long readChecksum(byte[] bytes, int offset, Checksum c) {
        switch (c.type()) {
            case CRC8:
                return bytes[offset] & 0xFFL;
            case CRC16:
                if (c.byteOrder() == ByteOrder.LITTLE) {
                    return (bytes[offset] & 0xFF) | ((bytes[offset + 1] & 0xFF) << 8);
                } else {
                    return ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
                }
            case CRC32:
                if (c.byteOrder() == ByteOrder.LITTLE) {
                    return (bytes[offset] & 0xFFL) |
                            ((bytes[offset + 1] & 0xFFL) << 8) |
                            ((bytes[offset + 2] & 0xFFL) << 16) |
                            ((bytes[offset + 3] & 0xFFL) << 24);
                } else {
                    return ((bytes[offset] & 0xFFL) << 24) |
                            ((bytes[offset + 1] & 0xFFL) << 16) |
                            ((bytes[offset + 2] & 0xFFL) << 8) |
                            (bytes[offset + 3] & 0xFFL);
                }
        }
        return 0;
    }

    @Override
    public long getCode() {
        return 0;
    }
}
