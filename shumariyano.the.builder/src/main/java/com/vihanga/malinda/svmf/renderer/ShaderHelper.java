package com.vihanga.malinda.svmf.renderer;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.vihanga.malinda.svmf.renderer.ShaderConstant.*;

public class ShaderHelper {
    @NotNull
    public static Map<String, String> getShaderSourceByTypeMap(String source) {
        String[] splitSource = source.split(TEST_PATTERN);

        // Find the first pattern after #type "pattern
        int index = source.indexOf("#type") + 6;
        int endOfLine = source.indexOf("\r\n",
                                       index);
        String firstPattern = source.substring(index,
                                               endOfLine).trim();

        // Find the Second pattern after #type # "pattern
        index = source.indexOf("#type",
                               endOfLine) + 6;
        endOfLine = source.indexOf("\r\n",
                                   index);
        String secondPattern = source.substring(index,
                                                endOfLine).trim();

        validateShaderType(firstPattern);
        validateShaderType(secondPattern);

        Map<String, String> shaderSourceByKey = new HashMap<>();
        shaderSourceByKey.put(firstPattern,
                              splitSource[1]);
        shaderSourceByKey.put(secondPattern,
                              splitSource[2]);
        return shaderSourceByKey;
    }

    private static void validateShaderType(String shaderType) {
        boolean isInValid = !shaderType.equals(VERTEX_SHADER) && !shaderType.equals(FRAGMENT_SHADER);
        if (isInValid) {
            throw new RuntimeException("Unexpected token. '" + shaderType + "'");
        }
    }

}
