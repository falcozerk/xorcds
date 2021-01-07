package com.falcozerk.xorcds;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public @Data
class Album {
    Artist artist;
    String name;
    String path;
    Map<String, Track> trackMap = new HashMap<>();

}
