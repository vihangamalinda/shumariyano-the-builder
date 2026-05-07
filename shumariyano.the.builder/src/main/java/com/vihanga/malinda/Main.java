package com.vihanga.malinda;

import com.vihanga.malinda.svmf.configuration.AppConfiguration;
import com.vihanga.malinda.svmf.window.Window;

public class Main {
    public static void main(String[] args) {
        AppConfiguration appConfiguration = new AppConfiguration();
        Window window = appConfiguration.getWindow();
        window.run();
    }
}