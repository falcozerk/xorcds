package com.falcozerk.xorcds;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public @Data
class Artist {
    String name;
    String path;
    Map<String, Album> albumMap = new HashMap<>();
    ;
}
