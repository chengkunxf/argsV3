package com.coding.args;

import java.util.Arrays;
import java.util.List;

public class SchemaDesc {
    public static final String REGEX_SPACE = " ";
    private String schemaText;

    public SchemaDesc(String schemaText) {
        this.schemaText = schemaText;
    }


    public int getSize() {
        return schemaText.split(REGEX_SPACE).length;
    }

    public Object getDefaultValue(String schemaName) {
        String type = getType(schemaName);
        if ("boolean".equals(type)) {
            return false;
        }
        if ("integer".equals(type)) {
            return 0;
        }
        if ("string".equals(type)) {
            return "";
        }
        if ("string[]".equals(type)) {
            return new String[]{};
        }
        throw new IllegalArgumentException(String.format("This %s type is not supported", type));
    }

    public String getType(String schemaName) {
        // l:boolean p:integer
        List<String> schemaList = Arrays.asList(schemaText.split(REGEX_SPACE));
        String oneDesc = schemaList.stream().filter(e -> e.startsWith(schemaName)).findAny().get();
        String type = oneDesc.split(":")[1];
        return type;
    }
}
