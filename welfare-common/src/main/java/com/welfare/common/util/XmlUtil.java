package com.welfare.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtil {

    public static Map<String, String> getSimpleMapFromDocument(String content) throws DocumentException {
        content = stripNonValidXMLCharacters(content);
        org.dom4j.Document document = DocumentHelper.parseText(content);

        // 得到xml根元素
        Element root = document.getRootElement();

        return getElementParameters(root.elements());
    }

    public static Map<String, String> getElementParameters(List<Element> elements) {
        Map<String, String> parameters = new HashMap<String, String>();

        for (Element e : elements) {
            if (e.elements().size() > 0) {
                parameters.putAll(getElementParameters(e.elements()));
            } else {
                parameters.put(e.getName(), e.getText());
            }
        }

        return parameters;
    }

    /**
     * This method ensures that the output String has only
     * valid XML unicode characters as specified by the
     * XML 1.0 standard. For reference, please see
     * <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
     * standard</a>. This method will return an empty
     * String if the input is null or empty.
     *
     * @param in The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }
}
