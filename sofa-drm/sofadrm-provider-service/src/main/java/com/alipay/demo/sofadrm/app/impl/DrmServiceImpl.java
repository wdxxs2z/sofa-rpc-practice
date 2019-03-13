package com.alipay.demo.sofadrm.app.impl;

import com.alipay.demo.sofadrm.api.DrmService;

public class DrmServiceImpl implements DrmService {

    @Override
    public String testMessage(String message) {
        return "test: " + message;
    }
}
