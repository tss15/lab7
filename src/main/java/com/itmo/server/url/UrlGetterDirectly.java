package com.itmo.server.url;

import com.itmo.server.url.UrlGetter;

public class UrlGetterDirectly implements UrlGetter {
    @Override
    public String getUrl() {
        return "jdbc:postgresql://pg:5432/studs";
    }
}
