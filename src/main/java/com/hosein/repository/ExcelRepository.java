package com.hosein.repository;

import com.hosein.model.Person;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;


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
                        header.createCell(i).setCellValue(HEADERS[i]);
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

    /**
     * ذخیره رکورد جدید یا به‌روزرسانی رکورد موجود.
     * اگر id برابر صفر باشد، رکورد جدید در نظر گرفته می‌شود و شناسه جدید تولید می‌شود.
     */
    public synchronized List<Person> findAll() {
        List<Person> people = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                Cell cellId = row.getCell(0);
                if (cellId == null || formatter.formatCellValue(cellId).isBlank()) {
                    continue;
                }
                Person person = new Person();
                person.setId((int) Double.parseDouble(formatter.formatCellValue(cellId)));
                person.setFirstName(formatter.formatCellValue(row.getCell(1)));
                person.setLastName(formatter.formatCellValue(row.getCell(2)));
                person.setNationalCode(formatter.formatCellValue(row.getCell(3)));
                person.setBirthDate(formatter.formatCellValue(row.getCell(4)));
                people.add(person);
            }
        } catch (IOException e) {
            throw new RuntimeException("خطا در خواندن فایل اکسل", e);
        }
        return people;
    }

    /**
     * ذخیره رکورد جدید یا به‌روزرسانی رکورد موجود.
     * اگر id برابر صفر باشد، رکورد جدید در نظر گرفته می‌شود و شناسه جدید تولید می‌شود.
     */
    public synchronized Person save(Person person) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            if (person.getId() == 0) {
                person.setId(nextId(sheet));
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                writeRow(row, person);
            } else {
                Row existingRow = findRowById(sheet, person.getId());
                if (existingRow != null) {
                    writeRow(existingRow, person);
                } else {
                    Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                    writeRow(row, person);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            throw new RuntimeException("خطا در ذخیره‌سازی رکورد در فایل اکسل", e);
        }
        return person;
    }

    // ---------- متدهای کمکی داخلی ----------

    private Row findRowById(Sheet sheet, int id) {
        return null;
    }

    private void writeRow(Row row, Person person) {
        getOrCreateCell(row, 0).setCellValue(person.getId());
        getOrCreateCell(row, 1).setCellValue(person.getFirstName());
        getOrCreateCell(row, 2).setCellValue(person.getLastName());
        // کد ملی به صورت متن ذخیره می‌شود تا صفرهای ابتدایی از بین نروند
        getOrCreateCell(row, 3).setCellValue(person.getNationalCode());
        getOrCreateCell(row, 4).setCellValue(person.getBirthDate());
    }


    private Cell getOrCreateCell(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell == null) {
            cell = row.createCell(i);
        }
        return cell;
    }

    private int nextId(Sheet sheet) {
        int max = 0;
        DataFormatter formatter = new DataFormatter();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            Cell cellId = row.getCell(0);
            if (cellId == null || formatter.formatCellValue(cellId).isBlank()) continue;
            int val = (int) Double.parseDouble(formatter.formatCellValue(cellId));
            if (val > max) {
                max = val;
            }
        }
        return max + 1;
    }
}
