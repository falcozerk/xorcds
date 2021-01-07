package com.falcozerk.xorcds;

import lombok.Data;

public @Data
class Track {
    Album album;
    String name;
    String path;
    Integer size;
}
