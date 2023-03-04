package com.msb.framework.web.tool;

import com.msb.framework.web.tool.excel.ExportExcelAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liao
 */
@Configuration
@Import(ExportExcelAspect.class)
public class WebToolsAutoConfig {
}
