package com.sivalabs.devzone;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "devzone")
public interface ApplicationProperties {
    boolean postDataInitEnabled();

    String postsInitDataFile();
}
