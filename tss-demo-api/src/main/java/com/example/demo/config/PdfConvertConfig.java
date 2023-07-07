package com.example.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfConvertConfig {
//	@Value("${jodconverter.local.office-home}")
//	private String officeHome;
//	
//	@Value("${jodconverter.local.portNumbers}")
//	private int[] ports;
//	
//	@Value("${jodconverter.local.maxTasksPerProcess}")
//	private int tasks;
//
//    @Bean(
//            initMethod = "start",
//            destroyMethod = "stop"
//    )
//    OfficeManager officeManager(){
//        //String os = System.getProperty("os.name").toLowerCase();
//        return LocalOfficeManager.builder()
//                .officeHome(officeHome)
//                .portNumbers(ports)
//                .maxTasksPerProcess(tasks)
//                .build();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean({OfficeManager.class})
//    DocumentConverter jodConverter(OfficeManager officeManager) {
//        return LocalConverter.make(officeManager);
//        //return null;
//    }
}
