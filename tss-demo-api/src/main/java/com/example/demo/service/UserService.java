package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.security.CheckUserRole;
import com.example.demo.util.ConvertExcelToPdf;
import com.example.demo.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	//@Autowired
	//@Qualifier("jodConverter")
	//private DocumentConverter documentConverter;
	
	@Value("${templates.file.path}")
    private String templateFilePath;

	private final UserMapper userMapper;

	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public User getUser(String id) {
		return userMapper.getUser(id);
	}
	
	public User getCurrentUser() {
		String id = CheckUserRole.getUserId();
		
		User user = getUser(id);
		
		return user;
	}

	public List<User> searchUser(String name) {
		List<User> users = null;
		if (name == null || name.isEmpty()) {
			users = userMapper.getAllUsers();
		} else {
			users = userMapper.searchUserByName(name);
		}
		return CheckUserRole.filterUser(getCurrentUser(), users);
	}

	@Transactional
	public void updateUser(String id, User user) {
		CheckUserRole.checkUpdatePermission(getCurrentUser(), user);
		
		log.info("id=" + id + ", user=" + user);
		user.setId(id);
		int count = userMapper.updateUser(user);
		if (count == 0) {
			throw new OptimisticLockingFailureException("更新失敗。他のユーザから更新されました。");
		}
	}

	@Transactional
	public void insertUser(User user) throws DuplicateKeyException {
		log.info("user=" + user);
		userMapper.insertUser(user);
	}

	@Transactional
	public void deleteUser(String id, User user) {
		log.info("id=" + id + ", user=" + user);
		user.setId(id);
		int count = userMapper.deleteUser(user);
		if (count == 0) {
			throw new OptimisticLockingFailureException("更新失敗。他のユーザから更新されました。");
		}
	}

	public ByteArrayOutputStream users2PDF(String name) throws IOException, OfficeException {
		List<User> users = searchUser(name);

		ClassPathResource resource = new ClassPathResource(templateFilePath + "/user-list.xlsx");
		InputStream stream = resource.getInputStream();
		
		File tempExcelFile = File.createTempFile("user-list", ".xlsx");
		FileUtils.copyInputStreamToFile(stream, tempExcelFile);
		
		stream.close();
		//Files.copy(templateFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

		try {
			FileInputStream fis = new FileInputStream(tempExcelFile);
			Workbook workbook = new XSSFWorkbook(fis);

			// Get the Sheet1
			Sheet sheet = workbook.getSheet("Sheet1");

			// Get the first row (index 0)
			Row rowSrc = sheet.getRow(2);

			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);

				Row row = sheet.getRow(2 + i);
				if (row == null) {
					row = sheet.createRow(2 + i);
					copyRowStyle(workbook, rowSrc, row, 1, 8);
				}
				row.getCell(1).setCellValue(user.getId());
				row.getCell(2).setCellValue(user.getName());
				row.getCell(3).setCellValue(user.getDept());
				row.getCell(4).setCellValue(user.getTitle());
				row.getCell(5).setCellValue(user.getInsert_id());
				row.getCell(6).setCellValue(StringUtil.formatExcelDateTime(user.getInsert_time()));
				row.getCell(7).setCellValue(user.getUpdate_id());
				row.getCell(8).setCellValue(StringUtil.formatExcelDateTime(user.getUpdate_time()));
			}
			
			fis.close();

			FileOutputStream fos = new FileOutputStream(tempExcelFile);
			workbook.write(fos);
			
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		File tempPdfFile = File.createTempFile("user-list", ".pdf");

		// Start an office manager to handle the conversion
		//        OfficeManager officeManager = LocalOfficeManager.install();
		//        officeManager.start();

		try {
			// Convert the Excel file to PDF
			//documentConverter.convert(tempExcelFile).to(tempPdfFile).execute();
			ConvertExcelToPdf converter = new ConvertExcelToPdf();
			converter.convert(tempExcelFile, tempPdfFile);

			// Read the converted PDF into a byte array
			FileInputStream pdfInputStream = new FileInputStream(tempPdfFile);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int byteRead;
			while ((byteRead = pdfInputStream.read()) != -1) {
				byteArrayOutputStream.write(byteRead);
			}

			pdfInputStream.close();

			return byteArrayOutputStream;
			
		} catch (OfficeException e) {
			e.printStackTrace();
			throw e;

		} finally {
			// Stop the office manager
			//            OfficeUtils.stopQuietly(officeManager);

			// Delete the temporary files
			tempExcelFile.delete();
			tempPdfFile.delete();
		}
	}

	private void copyRowStyle(Workbook book, Row src, Row target, int col1, int col2) {
		for (int i = col1; i <= col2; i++) {
			Cell c1 = src.getCell(i);
			Cell c2 = target.getCell(i);
			if (c2 == null) {
				c2 = target.createCell(i);
			}

			CellStyle s1 = c1.getCellStyle();
			CellStyle s2 = book.createCellStyle();
			s2.cloneStyleFrom(s1);
			c2.setCellStyle(s2);

		}
	}

}
