package com.coding.args;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Args {
    private final String argsText;
    private final SchemaDesc schemaDesc;
    private Map<String, Object> flagMap = new HashMap<>();

    public Args(String argsText, SchemaDesc schemaDesc) {
        this.argsText = argsText;
        this.schemaDesc = schemaDesc;
        parse();
    }

    private void parse() {
        //-l true l:boolean
        String[] cmdArray = argsText.split("-");
        for (int i = 1; i < cmdArray.length ; i++) {
            String oneCmd = cmdArray[i];
            String[] oneCmdArray = oneCmd.split(" ");
            String flag = oneCmdArray[0];
            Object flagValue = oneCmdArray.length > 1 ? oneCmdArray[1] : schemaDesc.getDefaultValue(flag);
            flagMap.put(flag, flagValue);
        }
    }

    public Object getValue(String flag) {
        Object flagValue = flagMap.get(flag);
        if ("boolean".equals(schemaDesc.getType(flag))) {
            return Boolean.valueOf(String.valueOf(flagValue));
        }
        if ("integer".equals(schemaDesc.getType(flag))) {
            return Integer.valueOf(String.valueOf(flagValue));
        }
        if ("string".equals(schemaDesc.getType(flag))) {
            return String.valueOf(flagValue);
        }
        if ("string[]".equals(schemaDesc.getType(flag))) {
            String s = String.valueOf(flagValue);
            return s.split(",");
        }
        if ("integer[]".equals(schemaDesc.getType(flag))) {
            String s = String.valueOf(flagValue);
            String[] array = s.split(",");
            int[] ints = Arrays.stream(array).mapToInt(Integer::parseInt).toArray();
            return ArrayUtils.toObject(ints);
        }

        return flagValue;
    }


}
