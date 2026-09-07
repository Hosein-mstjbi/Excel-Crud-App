package com.hosein.repository;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * لایه ذخیره‌سازی (Repository) که مستقیماً روی فایل اکسل کار می‌کند.
 * تمام عملیات خواندن و نوشتن روی فایل به صورت synchronized انجام می‌شود
 * تا از تداخل درخواست‌های همزمان جلوگیری شود.
 */
@Repository
public class ExcelRepository {

    private static final String FILE_PATH = "data" + File.separator + "people.xlsx";
    private static final String SHEET_NAME = "people";
    private static final String[] HEADERS = {"ردیف", "نام", "نام خانوادگی", "کد ملی", "تاریخ تولد"};

    public ExcelRepository() {
        initFileIfNotExists();
    }

    /**
     * در صورت نبود فایل اکسل، فایل جدید با سرستون‌های مناسب ایجاد می‌شود.
     */
    private void initFileIfNotExists() {
        try {
            File file = new File(FILE_PATH);

            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                try (Workbook workbook = new XSSFWorkbook()) {
                    Sheet sheet = workbook.createSheet(SHEET_NAME);
                    Row header = sheet.createRow(0);
                    for (int i = 0; i < HEADERS.length; i++) {
                        header.createCell(1).setCellValue(HEADERS[i]);
                    }
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        workbook.write(fos);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("خطا در ایجاد فایل اکسل اولیه", e);
        }
    }
}
