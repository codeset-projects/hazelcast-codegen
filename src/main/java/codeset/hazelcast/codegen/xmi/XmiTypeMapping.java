package codeset.hazelcast.codegen.xmi;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmiTypeMapping {

    private static Map<String, Class<?>> mappings = new HashMap<>();

    static {

        mappings.put("Text", String.class);
        mappings.put("Set", Set.class);
        mappings.put("Map", Map.class);
        mappings.put("List", List.class);
        mappings.put("Integer", Integer.class);
        mappings.put("Decimal", Double.class);
        mappings.put("Date", Date.class);
        mappings.put("Boolean", Boolean.class);

    }

    public static Class<?> get(String xmiType) {

        return mappings.get(xmiType);

    }

    public static void addMapping(String xmiTypeName, Class<?> mappingClass) {

        mappings.put(xmiTypeName, mappingClass);

    }

}
