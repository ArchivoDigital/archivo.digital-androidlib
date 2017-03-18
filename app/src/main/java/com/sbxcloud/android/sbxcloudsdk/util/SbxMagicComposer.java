package com.sbxcloud.android.sbxcloudsdk.util;

import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxModelException;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxParamField;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Created by lgguzman on 20/02/17.
 */


public class SbxMagicComposer {

    public static Object getSbxModel(JSONObject jsonObject,  Class<?> clazz, int level)throws  Exception{
        level++;
        //accept only two level
        if (level==3)
            return null;
        if(!SbxDataValidator.hasKeyAnnotation(clazz)){
            throw new SbxModelException("SbxKey is required");
        }
        Constructor<?> ctor = clazz.getConstructor();
        Object o = ctor.newInstance();
        final Field[] variables = clazz.getDeclaredFields();

        for (final Field variable : variables) {
            boolean isAccessible=variable.isAccessible();
            variable.setAccessible(true);
            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {

                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            variable.set(o,jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "int":{
                            variable.set(o,jsonObject.optInt(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "long":{
                            variable.set(o,jsonObject.optLong(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "double":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "float":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "class java.util.Date":{
                            variable.set(o,
                                    SbxDataValidator.getDate(
                                    jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation))));
                            break;
                        }
                        case "boolean":{
                            variable.set(o,jsonObject.optBoolean(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        default:{

                            Object obj= jsonObject.get(SbxDataValidator.getAnnotationName(variable,annotation));
                            if(obj instanceof String){
                               Object reference=variable.getType().getConstructor().newInstance();
                                final Field[] variables2 = variable.getType().getDeclaredFields();
                                for (final Field variable2 : variables2) {

                                    final Annotation annotation2 = variable2.getAnnotation(SbxKey.class);

                                    if (annotation2 != null && annotation2 instanceof SbxKey) {
                                        try {
                                            boolean isAccessible2 = variable2.isAccessible();
                                            variable2.setAccessible(true);
                                            variable2.set(reference, obj);
                                            variable2.setAccessible(isAccessible2);
                                            break;
                                        } catch (IllegalArgumentException | IllegalAccessException e) {
                                            throw new SbxAuthException(e);
                                        }
                                    }
                                }
                                variable.set(o,reference);
                            }else {
                                variable.set(o,
                                        getSbxModel(
                                                jsonObject.optJSONObject(SbxDataValidator.getAnnotationName(variable, annotation))
                                                , variable.getType(), level)
                                );
                            }
                            break;
                        }


                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            final Annotation annotationK = variable.getAnnotation(SbxKey.class);
            if (annotationK != null && annotationK instanceof SbxKey) {
                try {

                variable.set(o,jsonObject.optString("_KEY"));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            variable.setAccessible(isAccessible);

        }


        return o;
    }

    public static void getSbxModel(JSONObject jsonObject,  Class<?> clazz, Object o, int level)throws  Exception{
        level++;
        //accept only two level
        if (level==3)
            return ;
        if(!SbxDataValidator.hasKeyAnnotation(clazz)){
            throw new SbxModelException("SbxKey is required");
        }
        final Field[] variables = clazz.getDeclaredFields();

        for (final Field variable : variables) {
            boolean isAccessible=variable.isAccessible();
            variable.setAccessible(true);
            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {

                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            variable.set(o,jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "int":{
                            variable.set(o,jsonObject.optInt(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "long":{
                            variable.set(o,jsonObject.optLong(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "double":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "float":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "class java.util.Date":{
                            variable.set(o,
                                    SbxDataValidator.getDate(
                                            jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation))));
                            break;
                        }
                        case "boolean":{
                            variable.set(o,jsonObject.optBoolean(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        default:{

                            Object obj= jsonObject.get(SbxDataValidator.getAnnotationName(variable,annotation));
                            if(obj instanceof String){
                                Object reference=variable.getType().getConstructor().newInstance();
                                final Field[] variables2 = variable.getType().getDeclaredFields();
                                for (final Field variable2 : variables2) {

                                    final Annotation annotation2 = variable2.getAnnotation(SbxKey.class);

                                    if (annotation2 != null && annotation2 instanceof SbxKey) {
                                        try {
                                            boolean isAccessible2 = variable2.isAccessible();
                                            variable2.setAccessible(true);
                                            variable2.set(reference, obj);
                                            variable2.setAccessible(isAccessible2);
                                            break;
                                        } catch (IllegalArgumentException | IllegalAccessException e) {
                                            throw new SbxAuthException(e);
                                        }
                                    }
                                }
                                variable.set(o,reference);
                            }else {
                                variable.set(o,
                                        getSbxModel(
                                                jsonObject.optJSONObject(SbxDataValidator.getAnnotationName(variable, annotation))
                                                , variable.getType(), level)
                                );
                            }
                            break;
                        }


                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            final Annotation annotationK = variable.getAnnotation(SbxKey.class);
            if (annotationK != null && annotationK instanceof SbxKey) {
                try {

                    variable.set(o,jsonObject.optString("_KEY"));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            variable.setAccessible(isAccessible);

        }


        return ;
    }

    public static void getSbxModel(JSONObject jsonObject,  Class<?> clazz, Object o, int level, JSONObject fetches)throws  Exception{
        level++;
        //accept only two level
        if (level==3)
            return ;
        if(!SbxDataValidator.hasKeyAnnotation(clazz)){
            throw new SbxModelException("SbxKey is required");
        }
        final Field[] variables = clazz.getDeclaredFields();

        for (final Field variable : variables) {
            boolean isAccessible=variable.isAccessible();
            variable.setAccessible(true);
            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {

                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            variable.set(o,jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "int":{
                            variable.set(o,jsonObject.optInt(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "long":{
                            variable.set(o,jsonObject.optLong(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "double":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "float":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "class java.util.Date":{
                            variable.set(o,
                                    SbxDataValidator.getDate(
                                            jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation))));
                            break;
                        }
                        case "boolean":{
                            variable.set(o,jsonObject.optBoolean(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        default:{

                            Object obj= jsonObject.get(SbxDataValidator.getAnnotationName(variable,annotation));
                            if(obj instanceof String){

                                Object reference=null;
                                try{
                                    reference = SbxMagicComposer.getSbxModel(fetches.getJSONObject(SbxDataValidator
                                                    .getAnnotationModelNameFromVariable(variable)).getJSONObject((String)obj),
                                            variable.getType(),level) ;
                                }catch (Exception ex){}

                                if(reference==null) {
                                    reference=variable.getType().getConstructor().newInstance();
                                    final Field[] variables2 = variable.getType().getDeclaredFields();
                                    for (final Field variable2 : variables2) {

                                        final Annotation annotation2 = variable2.getAnnotation(SbxKey.class);

                                        if (annotation2 != null && annotation2 instanceof SbxKey) {
                                            try {
                                                boolean isAccessible2 = variable2.isAccessible();
                                                variable2.setAccessible(true);
                                                variable2.set(reference, obj);
                                                variable2.setAccessible(isAccessible2);
                                                break;
                                            } catch (IllegalArgumentException | IllegalAccessException e) {
                                                throw new SbxAuthException(e);
                                            }
                                        }
                                    }

                                }
                                variable.set(o,reference);
                            }else {
                                variable.set(o,
                                        getSbxModel(
                                                jsonObject.optJSONObject(SbxDataValidator.getAnnotationName(variable, annotation))
                                                , variable.getType(), level)
                                );
                            }
                            break;
                        }


                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            final Annotation annotationK = variable.getAnnotation(SbxKey.class);
            if (annotationK != null && annotationK instanceof SbxKey) {
                try {

                    variable.set(o,jsonObject.optString("_KEY"));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            variable.setAccessible(isAccessible);

        }


        return ;
    }

    public static Object getSbxModel(JSONObject jsonObject,  Class<?> clazz, int level, JSONObject fetches)throws  Exception{
        level++;
        //accept only two level
        if (level==3)
            return null;
        if(!SbxDataValidator.hasKeyAnnotation(clazz)){
            throw new SbxModelException("SbxKey is required");
        }
        Constructor<?> ctor = clazz.getConstructor();
        Object o = ctor.newInstance();
        final Field[] variables = clazz.getDeclaredFields();

        for (final Field variable : variables) {
            boolean isAccessible=variable.isAccessible();
            variable.setAccessible(true);
            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {

                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            variable.set(o,jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "int":{
                            variable.set(o,jsonObject.optInt(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "long":{
                            variable.set(o,jsonObject.optLong(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "double":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "float":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "class java.util.Date":{
                            variable.set(o,
                                    SbxDataValidator.getDate(
                                            jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation))));
                            break;
                        }
                        case "boolean":{
                            variable.set(o,jsonObject.optBoolean(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        default:{

                            Object obj= jsonObject.get(SbxDataValidator.getAnnotationName(variable,annotation));
                            if(obj instanceof String){

                                Object reference=null;
                                try{
                                    reference = SbxMagicComposer.getSbxModel(fetches.getJSONObject(SbxDataValidator
                                            .getAnnotationModelNameFromVariable(variable)).getJSONObject((String)obj),
                                            variable.getType(),level) ;
                                }catch (Exception ex){}

                                if(reference==null) {
                                    variable.getType().getConstructor().newInstance();
                                    final Field[] variables2 = variable.getType().getDeclaredFields();
                                    for (final Field variable2 : variables2) {

                                        final Annotation annotation2 = variable2.getAnnotation(SbxKey.class);

                                        if (annotation2 != null && annotation2 instanceof SbxKey) {
                                            try {
                                                boolean isAccessible2 = variable2.isAccessible();
                                                variable2.setAccessible(true);
                                                variable2.set(reference, obj);
                                                variable2.setAccessible(isAccessible2);
                                                break;
                                            } catch (IllegalArgumentException | IllegalAccessException e) {
                                                throw new SbxAuthException(e);
                                            }
                                        }
                                    }
                                }


                                variable.set(o,reference);


                            }else {
                                variable.set(o,
                                        getSbxModel(
                                                jsonObject.optJSONObject(SbxDataValidator.getAnnotationName(variable, annotation))
                                                , variable.getType(), level)
                                );
                            }
                            break;
                        }


                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            final Annotation annotationK = variable.getAnnotation(SbxKey.class);
            if (annotationK != null && annotationK instanceof SbxKey) {
                try {

                    variable.set(o,jsonObject.optString("_KEY"));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
            variable.setAccessible(isAccessible);

        }


        return o;
    }
}


