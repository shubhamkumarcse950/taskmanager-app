package com.taskManagement.ServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.taskManagement.Dtos.LeadDTO;
import com.taskManagement.Entitys.Lead;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

@Component
public class FileParserUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileParserUtil.class);
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public List<Lead> parseFile(MultipartFile file) throws IOException, InvalidFormatException {
		String fileName = file.getOriginalFilename();
		if (fileName == null || (!fileName.endsWith(".csv") && !fileName.endsWith(".xlsx"))) {
			throw new IllegalArgumentException("Unsupported file format. Please upload a CSV or XLSX file.");
		}

		if (fileName.endsWith(".csv")) {
			return parseCsv(file.getInputStream());
		} else {
			return parseExcel(file.getInputStream());
		}
	}

	// ====================== CSV PARSER ======================
	private List<Lead> parseCsv(InputStream inputStream) throws IOException {
		List<Lead> leads = new ArrayList<>();

		CsvParserSettings settings = new CsvParserSettings();
		settings.setHeaderExtractionEnabled(true);
		settings.setIgnoreLeadingWhitespaces(true);
		settings.setIgnoreTrailingWhitespaces(true);
		settings.setNullValue("");
		settings.setEmptyValue("");

		CsvParser parser = new CsvParser(settings);
		parser.beginParsing(inputStream);

		String[] row;
		while ((row = parser.parseNext()) != null) {
			Lead lead = new Lead();
			lead.setSource("Upload"); // ✅ always set source

			lead.setName(nullIfEmpty(safe(row, 0)));
			lead.setPhone(nullIfEmpty(safe(row, 1)));
			lead.setEmail(nullIfEmpty(safe(row, 2)));
			lead.setRequirement(nullIfEmpty(safe(row, 3)));
			lead.setLocation(nullIfEmpty(safe(row, 4)));
			lead.setStatus(nullIfEmpty(safe(row, 5)));
			lead.setCompany(nullIfEmpty(safe(row, 6)));

			String dateStr = safe(row, 7);
			lead.setDateAdded(parseDateSafe(dateStr));

			lead.setIndustry(nullIfEmpty(safe(row, 8)));

			leads.add(lead);

			if (leads.size() % 1000 == 0) {
				logger.info("Parsed {} rows from CSV so far...", leads.size());
			}
		}
		parser.stopParsing();
		logger.info("✅ Finished parsing {} leads from CSV", leads.size());
		return leads;
	}

	// ====================== EXCEL PARSER ======================
	private List<Lead> parseExcel(InputStream inputStream) throws IOException, InvalidFormatException {
		List<Lead> leads = new ArrayList<>();
		DataFormatter formatter = new DataFormatter();

		try (OPCPackage pkg = OPCPackage.open(inputStream); Workbook workbook = new XSSFWorkbook(pkg)) {

			Sheet sheet = workbook.getSheetAt(0);
			boolean skipHeader = true;

			for (Row row : sheet) {
				if (skipHeader) {
					skipHeader = false;
					continue;
				}

				Lead lead = new Lead();
				lead.setSource("Upload"); // ✅ always set source

				lead.setName(nullIfEmpty(formatter.formatCellValue(row.getCell(0))));
				lead.setPhone(nullIfEmpty(formatter.formatCellValue(row.getCell(1))));
				lead.setEmail(nullIfEmpty(formatter.formatCellValue(row.getCell(2))));
				lead.setRequirement(nullIfEmpty(formatter.formatCellValue(row.getCell(3))));
				lead.setLocation(nullIfEmpty(formatter.formatCellValue(row.getCell(4))));
				lead.setStatus(nullIfEmpty(formatter.formatCellValue(row.getCell(5))));
				lead.setCompany(nullIfEmpty(formatter.formatCellValue(row.getCell(6))));

				String dateStr = formatter.formatCellValue(row.getCell(7));
				lead.setDateAdded(parseDateSafe(dateStr));

				lead.setIndustry(nullIfEmpty(formatter.formatCellValue(row.getCell(8))));

				leads.add(lead);

				if (leads.size() % 1000 == 0) {
					logger.info("Parsed {} rows from Excel so far...", leads.size());
				}
			}
			logger.info("✅ Finished parsing {} leads from Excel", leads.size());
		}
		return leads;
	}

	// ====================== CSV TEMPLATE GENERATOR ======================
	public ByteArrayInputStream generateCsvTemplate(List<LeadDTO> templateData) {
		String[] headers = { "name", "phone", "email", "requirement", "location", "status", "company", "dateAdded",
				"industry" };
		StringBuilder csvContent = new StringBuilder();
		csvContent.append(String.join(",", headers)).append("\n");

		for (LeadDTO dto : templateData) {
			csvContent.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n", escape(dto.getName()),
					escape(dto.getPhone()), escape(dto.getEmail()), escape(dto.getRequirement()),
					escape(dto.getLocation()), escape(dto.getStatus()), escape(dto.getCompany()),
					escape(dto.getDateAdded()), escape(dto.getIndustry())));
		}

		try {
			return new ByteArrayInputStream(csvContent.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed to generate CSV template: " + e.getMessage());
		}
	}

	// ====================== HELPERS ======================
	private LocalDate parseDateSafe(String dateStr) {
		if (dateStr == null || dateStr.isBlank()) {
			return LocalDate.now();
		}
		try {
			return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
		} catch (Exception ex) {
			logger.warn("⚠️ Invalid date '{}'. Using current date instead.", dateStr);
			return LocalDate.now();
		}
	}

	private String safe(String[] row, int index) {
		return (row.length > index && row[index] != null) ? row[index].trim() : "";
	}

	private String nullIfEmpty(String value) {
		return (value == null || value.trim().isEmpty()) ? null : value.trim();
	}

	private String escape(String value) {
		if (value == null) {
			return "";
		}
		if (value.contains(",") || value.contains("\"")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}
}

//@Component
//public class FileParserUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(FileParserUtil.class);
//	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//	public List<Lead> parseFile(MultipartFile file) throws IOException, InvalidFormatException {
//		String fileName = file.getOriginalFilename();
//		if (fileName == null || (!fileName.endsWith(".csv") && !fileName.endsWith(".xlsx"))) {
//			throw new IllegalArgumentException("Unsupported file format. Please upload a CSV or XLSX file.");
//		}
//
//		if (fileName.endsWith(".csv")) {
//			return parseCsv(file.getInputStream());
//		} else {
//			return parseExcel(file.getInputStream());
//		}
//	}
//
//	private List<Lead> parseCsv(InputStream inputStream) throws IOException {
//		List<Lead> leads = new ArrayList<>();
//
//		CsvParserSettings settings = new CsvParserSettings();
//		settings.setHeaderExtractionEnabled(true);
//		settings.setIgnoreLeadingWhitespaces(true);
//		settings.setIgnoreTrailingWhitespaces(true);
//		settings.setNullValue("");
//		settings.setEmptyValue("");
//
//		CsvParser parser = new CsvParser(settings);
//		parser.beginParsing(inputStream);
//
//		String[] row;
//		while ((row = parser.parseNext()) != null) {
//			Lead lead = new Lead();
//
//			lead.setName(nullIfEmpty(safe(row, 0)));
//			lead.setPhone(nullIfEmpty(safe(row, 1)));
//			lead.setEmail(nullIfEmpty(safe(row, 2)));
//			lead.setRequirement(nullIfEmpty(safe(row, 3)));
//			lead.setLocation(nullIfEmpty(safe(row, 4)));
//			lead.setStatus(nullIfEmpty(safe(row, 5)));
//			lead.setCompany(nullIfEmpty(safe(row, 6)));
//
//			String dateStr = safe(row, 7);
//			lead.setDateAdded(dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr, DATE_FORMATTER));
//
//			lead.setIndustry(nullIfEmpty(safe(row, 8)));
//
//			leads.add(lead);
//
//			if (leads.size() % 1000 == 0) {
//				logger.info("Parsed {} rows from CSV so far...", leads.size());
//			}
//		}
//		parser.stopParsing();
//		logger.info("✅ Finished parsing {} leads from CSV", leads.size());
//		return leads;
//	}
//
//	private List<Lead> parseExcel(InputStream inputStream) throws IOException, InvalidFormatException {
//		List<Lead> leads = new ArrayList<>();
//		DataFormatter formatter = new DataFormatter();
//
//		try (OPCPackage pkg = OPCPackage.open(inputStream); Workbook workbook = new XSSFWorkbook(pkg)) {
//			Sheet sheet = workbook.getSheetAt(0);
//			boolean skipHeader = true;
//
//			for (Row row : sheet) {
//				if (skipHeader) {
//					skipHeader = false;
//					continue;
//				}
//
//				Lead lead = new Lead();
//				lead.setName(nullIfEmpty(formatter.formatCellValue(row.getCell(0))));
//				lead.setPhone(nullIfEmpty(formatter.formatCellValue(row.getCell(1))));
//				lead.setEmail(nullIfEmpty(formatter.formatCellValue(row.getCell(2))));
//				lead.setRequirement(nullIfEmpty(formatter.formatCellValue(row.getCell(3))));
//				lead.setLocation(nullIfEmpty(formatter.formatCellValue(row.getCell(4))));
//				lead.setStatus(nullIfEmpty(formatter.formatCellValue(row.getCell(5))));
//				lead.setCompany(nullIfEmpty(formatter.formatCellValue(row.getCell(6))));
//
//				String dateStr = formatter.formatCellValue(row.getCell(7));
//				lead.setDateAdded(dateStr == null || dateStr.isEmpty() ? LocalDate.now()
//						: LocalDate.parse(dateStr, DATE_FORMATTER));
//
//				String industry = formatter.formatCellValue(row.getCell(8));
//				lead.setIndustry(industry == null || industry.trim().isEmpty() ? null : industry.trim());
//
//				leads.add(lead);
//
//				if (leads.size() % 1000 == 0) {
//					logger.info("Parsed {} rows from Excel so far...", leads.size());
//				}
//			}
//			logger.info("✅ Finished parsing {} leads from Excel", leads.size());
//		}
//		return leads;
//	}
//

//
//	private String escape(String value) {
//		if (value == null) {
//			return "";
//		}
//		if (value.contains(",") || value.contains("\"")) {
//			return "\"" + value.replace("\"", "\"\"") + "\"";
//		}
//		return value;
//	}
//
//	private String safe(String[] row, int index) {
//		return (row.length > index && row[index] != null) ? row[index].trim() : "";
//	}
//
//	private String nullIfEmpty(String value) {
//		return (value == null || value.trim().isEmpty()) ? null : value.trim();
//	}
//}
