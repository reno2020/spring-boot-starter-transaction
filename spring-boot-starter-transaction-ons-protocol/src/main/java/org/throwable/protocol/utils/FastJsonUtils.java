package org.throwable.protocol.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.throwable.ons.core.common.constants.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 23:51
 */
public abstract class FastJsonUtils {

    private static final SerializerFeature[] GENERATE_FEATURES;

    private static final Feature[] PARSE_FEATURES;

    static {
        List<SerializerFeature> generateFeatures = new ArrayList<>();
        generateFeatures.add(SerializerFeature.WriteEnumUsingName);
        generateFeatures.add(SerializerFeature.QuoteFieldNames);
        generateFeatures.add(SerializerFeature.SkipTransientField);
        generateFeatures.add(SerializerFeature.SortField);
        generateFeatures.add(SerializerFeature.MapSortField);
        GENERATE_FEATURES = generateFeatures.toArray(new SerializerFeature[generateFeatures.size()]);

        List<Feature> parseFeatures = new ArrayList<>();
        parseFeatures.add(Feature.AutoCloseSource);
        parseFeatures.add(Feature.InternFieldNames);
        parseFeatures.add(Feature.UseBigDecimal);
        parseFeatures.add(Feature.AllowUnQuotedFieldNames);
        parseFeatures.add(Feature.AllowArbitraryCommas);
        parseFeatures.add(Feature.SortFeidFastMatch);
        parseFeatures.add(Feature.IgnoreNotMatch);
        PARSE_FEATURES = parseFeatures.toArray(new Feature[parseFeatures.size()]);
    }

    public static String toJsonString(Object value) {
        return JSON.toJSONStringWithDateFormat(value, Constants.DETE_TIME_PATTERN, GENERATE_FEATURES);
    }

    public static <T> T parseFromJsonString(String value, Class<T> clazz) {
        return JSON.parseObject(value, clazz, PARSE_FEATURES);
    }
}
