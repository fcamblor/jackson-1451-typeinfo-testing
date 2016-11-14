package com.fasterxml.jackson.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by fcamblor on 14/11/16.
 */
public class TypeInfoSerializationTest {
    @JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    public static class A {
        public String a;
    }
    public static class B extends A {
        public String b;
    }

    @Test
    public void should_polymorphic_iterables_return_typeinfo_class_in_instances() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.EAGER_DESERIALIZER_FETCH)
                .disable(SerializationFeature.EAGER_SERIALIZER_FETCH);

        ParameterizedType valueType = newParameterizedType(Iterable.class, A.class);
        ObjectWriter writer = mapper.writerWithType(TypeFactory.defaultInstance().constructType(valueType));

        String result = writer.writeValueAsString(createPolymorphicList());

        assertThat(result, is(equalTo("[{\"@class\":\".TypeInfoSerializationTest$A\",\"a\":\"a1\"},{\"@class\":\".TypeInfoSerializationTest$B\",\"a\":\"a2\",\"b\":\"b\"}]")));
    }

    private static ParameterizedType newParameterizedType(final Class<?> rawType, final Type... arguments) {
        return new ParameterizedType() {
            public Type[] getActualTypeArguments() {
                return arguments;
            }
            public Type getRawType() {
                return rawType;
            }
            public Type getOwnerType() {
                return rawType.getEnclosingClass();
            }
        };
    }


    private static Iterable<A> createPolymorphicList(){
        List<A> l = new ArrayList<A>();
        A a = new A();
        a.a = "a1";
        l.add(a);

        B b = new B();
        b.b = "b";
        b.a = "a2";
        l.add(b);

        return l;

    }
}
