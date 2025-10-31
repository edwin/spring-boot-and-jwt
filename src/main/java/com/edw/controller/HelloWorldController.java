package com.edw.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * <pre>
 *  com.edw.controller.HelloWorldController
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 31 Oct 2025 14:36
 */
@RestController
public class HelloWorldController {

    @GetMapping("/")
    public ResponseEntity<HashMap> helloWorld() {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("message", "Hello World!");
        }});
    }

}
