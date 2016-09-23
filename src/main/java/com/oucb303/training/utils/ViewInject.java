package com.oucb303.training.utils;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by TL on 2016/6/2.
 */
public class ViewInject {

    public static void inject(Activity activity)
    {
        Class<? extends Activity> aClass = activity.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field:declaredFields)
        {
            if (field.isAnnotationPresent(Inject.class))
            {
                Inject annotation = field.getAnnotation(Inject.class);
                field.setAccessible(true);
                try {
                   field.set(activity,activity.findViewById(annotation.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
