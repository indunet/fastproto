package org.indunet.fastproto.check;

import java.util.zip.Checksum;

/**
 * @author Deng Ran
 * @since 1.6.0
 */
public class Crc32Checker implements Checker {
    protected final static Crc32Checker checker = new Crc32Checker();

    public static Crc32Checker getInstance() {
        return checker;
    }
}
