package com.vihanga.malinda.svmf.renderer;

public class ShaderConstant {
    public static final String VERTEX_SHADER = "vertex";
    public static final String FRAGMENT_SHADER = "fragment";

    public final static String TYPE_IDENTIFIER_PATTERN ="(#type)()+([a-zA-Z])+";
    public final static String TEST_PATTERN= "#type\\s+(vertex|fragment)";
}
