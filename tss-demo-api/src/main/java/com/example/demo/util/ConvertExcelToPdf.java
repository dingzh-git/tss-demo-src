package com.example.demo.util;

import java.io.File;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Value;

public class ConvertExcelToPdf {
	@Value("${jodconverter.local.office-home}")
	private String officeHome;
	
	@Value("${jodconverter.local.portNumbers}")
	private int[] ports;
	
	@Value("${jodconverter.local.maxTasksPerProcess}")
	private int tasks;

	public void convert(File excel, File pdf) throws OfficeException {
		LocalOfficeManager officeManager = LocalOfficeManager.builder().build();
		try {
			officeManager.start();
			
			DocumentConverter converter = LocalConverter.builder()
                    .officeManager(officeManager)
                    .build();
			
			converter.convert(excel).to(pdf).execute();
			System.out.println("Conversion completed successfully!");
			
		} catch (OfficeException e) {
			e.printStackTrace();
			throw e;
		} finally {
            // 停止 OfficeManager
			officeManager.stop();
        }
	}
}
