package org.vnet.fastproto;

import java.util.Map;
import java.util.Set;

public class FastProtoSession {
    Map<String, byte[]> datagramMap;
    Set<Object> objectSet;
    Object currentObject;

    public FastProtoSession(Map<String, byte[]> datagramMap, Set<Object> objectSet) {
        this.datagramMap = datagramMap;
        this.objectSet = objectSet;
    }

    public boolean containsDatagram(String datagramName) {
        return this.datagramMap.containsKey(datagramName);
    }

    public byte[] getDatagram() {
        return this.datagramMap.getOrDefault("default", null);
    }

    public byte[] getDatagram(String datagramName) {
        return this.datagramMap.getOrDefault("datagramName", null);
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(Object currentObject) {
        this.currentObject = currentObject;
    }
}
