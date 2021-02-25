package org.indunet.fastproto.util;

import org.indunet.fastproto.domain.Motor;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.indunet.fastproto.Endian;

import java.lang.reflect.Field;
import java.util.List;

import static org.indunet.fastproto.util.ReflectUtils.*;
import static org.junit.Assert.*;

public class ReflectUtilsTest { 
    Motor motor = new Motor();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetEndianObject() throws Exception {
        assertEquals(getEndian(motor.getClass()), Endian.Little);
    }

    @Test
    public void testGetEndianField() throws Exception {
        assertEquals(getEndian(motor.getClass().getDeclaredField("voltage")), Endian.Big);
    }

    @Test
    public void testGetDatagramNameObject() throws Exception {
        assertEquals(getDatagramName(motor.getClass()), "motor");
    }

    @Test
    public void testGetDatagramNameField() throws Exception {
        assertEquals(getDatagramName(motor.getClass().getDeclaredField("speed")), "sensor");
    }

    @Test
    public void testGetDecodeFormula() throws Exception {
        //TODO: Test goes here...
    }

    @Test
    public void testGetDecodeIngore() throws Exception {
    //TODO: Test goes here...
    }

    @Test
    public void testGetEncodeIgnore() throws Exception {
    //TODO: Test goes here...
    }

    @Test
    public void testGetDataType() throws Exception {
        List<Field> fieldList = getDataTypeField(motor.getClass());

        assertTrue(fieldList.size() > 0);
    }

    @Test
    public void testGetObjectType() throws Exception {
    //TODO: Test goes here...
    }
} 
