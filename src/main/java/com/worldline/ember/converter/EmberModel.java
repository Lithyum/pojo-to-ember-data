package com.worldline.ember.converter;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EmberModel {

    private static final String DATE_FORMAT_SPECIFIER = "%DATE%";
    private static final String FILE_HEADER = "// Auto-generated by EmberDataModelGenerator ( " + DATE_FORMAT_SPECIFIER + " )";

    private String name;
    private String superClass;
    private List<EmberAttribute> attributes;
    private Boolean writeFileHeader = Boolean.TRUE;

    public EmberModel(String name, String superClass,
                      List<EmberAttribute> emberAttributes, Boolean writeFileHeader) {
        this.name = name;
        this.superClass = superClass;
        this.attributes = emberAttributes;
        this.writeFileHeader = writeFileHeader;
    }

    public void write(Writer writer) throws IOException {
        if (writeFileHeader) {
            writer.append(FILE_HEADER.replace(DATE_FORMAT_SPECIFIER, new Date().toString()));
            writer.append(System.lineSeparator());
        }
        writer.append(name).append(" = ").append(superClass).append(".extend({");
        writer.append(System.lineSeparator());

        Iterator<EmberAttribute> iter = attributes.iterator();
        while (iter.hasNext()) {
            writer.append('\t');
            EmberAttribute attr = iter.next();
            attr.write(writer);

            if (iter.hasNext()) {
                writer.append(',');
            }
            writer.append(System.lineSeparator());
        }

        writer.append("});");
        writer.append(System.lineSeparator());
        writer.close();
    }

    public String getName() {
        return name;
    }

    public String getSuperClass() {
        return superClass;
    }

    public List<EmberAttribute> getAttributes() {
        return attributes;
    }

    public Boolean getWriteFileHeader() {
        return writeFileHeader;
    }
}