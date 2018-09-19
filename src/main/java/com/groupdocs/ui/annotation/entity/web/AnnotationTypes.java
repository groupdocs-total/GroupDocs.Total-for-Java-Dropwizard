package com.groupdocs.ui.annotation.entity.web;

import java.util.HashMap;
import java.util.Map;

/**
 * AnnotationTypes
 * Contains all annotations types
 * @author Aspose Pty Ltd
 */
public class AnnotationTypes {

    private Map<String, String> types = new HashMap<String, String>();

    public AnnotationTypes(){
        types.put("0", "text");
        types.put("1", "area");
        types.put("2", "point");
        types.put("3", "textStrikeout");
        types.put("4", "polyline");
        types.put("5", "textField");
        types.put("6", "watermark");
        types.put("7", "textReplacement");
        types.put("8", "arrow");
        types.put("9", "textRedaction");
        types.put("10", "resourcesRedaction");
        types.put("11", "textUnderline");
        types.put("12", "distance");
    }

    public String getAnnotationType(byte type){
        return types.get(Byte.toString(type));
    }
}

