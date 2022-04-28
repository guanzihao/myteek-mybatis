package com.myteek.mybatis.generator.element;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

public class GenericXmlElement extends XmlElement {

    public GenericXmlElement(String name) {
        super(name);
    }

    /**
     * generic xml constructor
     * @param xmlElement xml element
     */
    public GenericXmlElement(XmlElement xmlElement) {
        super(xmlElement.getName());
        putElement(xmlElement.getElements());
        putAttrs(xmlElement.getAttributes());
    }

    /**
     * put elements
     * @param elementList element list
     */
    public void putElement(List<VisitableElement> elementList) {
        for (VisitableElement element : elementList) {
            addElement(element);
        }
    }

    /**
     * put attrs
     * @param attributes attributes
     */
    public void putAttrs(List<Attribute> attributes) {
        for (Attribute attr : attributes) {
            addAttribute(attr);
        }
    }

}
