package site.lanmushan.slashdocstarter.util;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Set;

public class ParameterTypeDeterminer {
    private ParameterTypeDeterminer() {
        throw new UnsupportedOperationException();
    }

    public static String determineScalarParameterType(Set<MediaType> consumes, HttpMethod method) {
        String parameterType = "query";

        if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED)
                && method == HttpMethod.POST) {
            parameterType = "form";
        } else if (consumes.contains(MediaType.MULTIPART_FORM_DATA)
                && method == HttpMethod.POST) {
            parameterType = "formData";
        }

        return parameterType;
    }
}
