package site.lanmushan.slashdocdemo.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.lanmushan.slashdocdemo.entity.QueryInfo;
import site.lanmushan.slashdocdemo.entity.SysTbApp;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author dy
 */
@RequestMapping(name = "测试控制器", value = "/demo2")
@RestController
@Slf4j
public class Demo2Controller {
    @GetMapping(name = "简单测试", value = "/test1")
    public SysTbApp test1(String name) {
        return null;
    }

    @PostMapping(name = "简单测试", value = "/PostMapping")
    public SysTbApp PostMapping(@RequestBody SysTbApp sysTbApp) {
        return null;
    }

    @PostMapping(name = "Validated分组测试SaveGroup", value = "/PostMappingSaveGroup")

    public void PostMappingSaveGroup(@RequestBody @Validated(SaveGroup.class)
                                             SysTbApp sysTbApp, @RequestParam String test) {
        return;
    }

    @PostMapping(name = "Validated分组测试UpdateGroup", value = "/PostMappingUpdateGroup")
    public void PostMappingUpdateGroup(@RequestBody @Validated(UpdateGroup.class)
                                               SysTbApp sysTbApp, @RequestParam String test) {
        return;
    }

    @PostMapping(name = "Valid测试", value = "/PostMappingValid")
    public void PostMapping3(@RequestBody @Valid SysTbApp sysTbApp, @RequestParam String test) {
        return;
    }

    @GetMapping(name = "隐藏测试", value = "/queryInfo")
    public void PostMapping3(@NotNull QueryInfo queryInfo) {
        return;
    }
}
