package com.example.webfluxmicroservicedemo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DemoModel {
    private Map<String, String> args;
}
