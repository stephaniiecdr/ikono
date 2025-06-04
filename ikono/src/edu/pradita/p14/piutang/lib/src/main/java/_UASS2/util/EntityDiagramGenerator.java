package _UASS2.util;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class EntityDiagramGenerator {
    
    private static final String OUTPUT_FILE = "src/main/resources/diagrams/entities.puml";
    private static final String PACKAGE_TO_SCAN = "_UASS2";
    
    public static void main(String[] args) {
        try {
            generateEntityDiagram();
            System.out.println("Entity diagram berhasil di-generate ke: " + OUTPUT_FILE);
        } catch (Exception e) {
            System.err.println("Error generating entity diagram: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void generateEntityDiagram() throws IOException {
        Reflections reflections = new Reflections(PACKAGE_TO_SCAN, Scanners.TypesAnnotated);
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
        
        StringBuilder plantUML = new StringBuilder();
        plantUML.append("@startuml Entity Relationship Diagram\n\n");
        plantUML.append("!define ENTITY class\n");
        plantUML.append("!define PK <color:red><b>PK</b></color>\n");
        plantUML.append("!define FK <color:blue><b>FK</b></color>\n");
        plantUML.append("!define UK <color:green><b>UK</b></color>\n\n");
        
        Map<String, List<String>> relationships = new HashMap<>();

        for (Class<?> entityClass : entityClasses) {
            String entityName = getEntityName(entityClass);
            plantUML.append("ENTITY ").append(entityName).append(" {\n");
            
            Field[] fields = entityClass.getDeclaredFields();
            boolean hasIdField = false;
            
            for (Field field : fields) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                
                String fieldName = field.getName();
                String fieldType = getSimpleTypeName(field.getType());
                StringBuilder fieldDefinition = new StringBuilder();

                if (field.isAnnotationPresent(Id.class)) {
                    fieldDefinition.append("    PK ");
                    hasIdField = true;
                } else if (field.isAnnotationPresent(JoinColumn.class) || 
                          field.isAnnotationPresent(ManyToOne.class) ||
                          field.isAnnotationPresent(OneToOne.class)) {
                    fieldDefinition.append("    FK ");
                } else {
                    fieldDefinition.append("    ");
                }

                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    if (column.unique()) {
                        if (!fieldDefinition.toString().contains("PK")) {
                            fieldDefinition = new StringBuilder("    UK ");
                        }
                    }
                }
                
                fieldDefinition.append(fieldName).append(": ").append(fieldType);

                if (field.isAnnotationPresent(Id.class) && !hasIdField) {
                    plantUML.append(fieldDefinition).append("\n    --\n");
                } else {
                    plantUML.append(fieldDefinition).append("\n");
                }

                collectRelationships(entityClass, field, relationships);
            }
            
            plantUML.append("}\n\n");
        }

        for (Map.Entry<String, List<String>> entry : relationships.entrySet()) {
            for (String relationship : entry.getValue()) {
                plantUML.append(relationship).append("\n");
            }
        }
        
        plantUML.append("\n@enduml");

        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            writer.write(plantUML.toString());
        }
    }
    
    private static String getEntityName(Class<?> entityClass) {
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        if (entityAnnotation != null && !entityAnnotation.name().isEmpty()) {
            return entityAnnotation.name();
        }
        return entityClass.getSimpleName();
    }
    
    private static String getSimpleTypeName(Class<?> type) {
        if (type.isPrimitive()) {
            return type.getSimpleName();
        }
        
        String typeName = type.getSimpleName();

        switch (typeName) {
            case "String": return "String";
            case "Integer": return "Integer";
            case "Long": return "Long";
            case "BigDecimal": return "BigDecimal";
            case "LocalDateTime": return "LocalDateTime";
            case "LocalDate": return "LocalDate";
            case "Boolean": return "Boolean";
            case "Double": return "Double";
            case "Float": return "Float";
            default:
                if (type.isAnnotationPresent(Entity.class)) {
                    return getEntityName(type);
                }
                return typeName;
        }
    }
    
    private static void collectRelationships(Class<?> entityClass, Field field, 
                                           Map<String, List<String>> relationships) {
        String entityName = getEntityName(entityClass);
        
        if (field.isAnnotationPresent(OneToMany.class)) {
            String targetEntity = getTargetEntityName(field);
            String relationship = targetEntity + " ||--o{ " + entityName + " : \"has\"";
            relationships.computeIfAbsent(entityName, k -> new ArrayList<>()).add(relationship);
            
        } else if (field.isAnnotationPresent(ManyToOne.class)) {
            String targetEntity = getTargetEntityName(field);
            String relationship = entityName + " }o--|| " + targetEntity + " : \"belongs to\"";
            relationships.computeIfAbsent(entityName, k -> new ArrayList<>()).add(relationship);
            
        } else if (field.isAnnotationPresent(OneToOne.class)) {
            String targetEntity = getTargetEntityName(field);
            String relationship = entityName + " ||--|| " + targetEntity + " : \"linked to\"";
            relationships.computeIfAbsent(entityName, k -> new ArrayList<>()).add(relationship);
            
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            String targetEntity = getTargetEntityName(field);
            String relationship = entityName + " }o--o{ " + targetEntity + " : \"associated\"";
            relationships.computeIfAbsent(entityName, k -> new ArrayList<>()).add(relationship);
        }
    }
    
    private static String getTargetEntityName(Field field) {
        Class<?> fieldType = field.getType();

        if (Collection.class.isAssignableFrom(fieldType)) {
            java.lang.reflect.ParameterizedType parameterizedType = 
                (java.lang.reflect.ParameterizedType) field.getGenericType();
            Class<?> actualType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            return getEntityName(actualType);
        }
        
        return getEntityName(fieldType);
    }
}
