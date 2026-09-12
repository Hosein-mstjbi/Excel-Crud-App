# 📊 Excel CRUD App — مدیریت رکوردهای فایل اکسل با Spring Boot

برنامه‌ای وب‌محور که به‌جای پایگاه‌داده، مستقیماً روی یک فایل اکسل (`.xlsx`) کار می‌کند:
محتویات فایل را در قالب جدول نمایش می‌دهد و امکان **افزودن، ویرایش و حذف** رکوردها را
با اعتبارسنجی کامل داده‌ها فراهم می‌کند.

---

## ✨ ویژگی‌ها

- نمایش محتویات فایل اکسل در یک جدول وب
- افزودن، ویرایش و حذف رکوردها (CRUD کامل)
- اعتبارسنجی داده‌ها:
  - نام و نام‌خانوادگی: فقط حروف فارسی
  - کد ملی: دقیقاً ۱۰ رقم
  - تاریخ تولد: فرمت `1400/01/01`
- پذیرش خودکار ارقام فارسی/عربی و تبدیل آن‌ها به ارقام انگلیسی پیش از اعتبارسنجی
- جلوگیری از ثبت کد ملی تکراری (قانون Business)
- رابط کاربری راست‌به‌چپ (RTL) با Thymeleaf

---

## 🛠 تکنولوژی‌ها

| بخش | تکنولوژی |
|---|---|
| زبان برنامه‌نویسی | Java 17 |
| فریم‌ورک | Spring Boot 3 |
| موتور قالب | Thymeleaf |
| اعتبارسنجی | Jakarta Bean Validation |
| کار با اکسل | Apache POI |
| ابزار ساخت | Maven |

---

## 📂 ساختار پروژه

```
excel-crud-app/
├── src/main/java/com/example/excelapp/
│   ├── ExcelAppApplication.java     # نقطه ورود برنامه
│   ├── model/Person.java            # مدل هر سطر اکسل + قوانین اعتبارسنجی
│   ├── repository/ExcelRepository.java  # خواندن/نوشتن فایل اکسل با Apache POI
│   ├── service/PersonService.java   # قوانین Business (کد ملی یکتا)
│   ├── controller/PersonController.java # مسیرهای HTTP برای CRUD
│   ├── controller/HomeController.java
│   └── util/PersianDigitUtil.java   # تبدیل ارقام فارسی/عربی به انگلیسی
├── src/main/resources/
│   ├── templates/index.html         # نمایش جدول رکوردها
│   ├── templates/form.html          # فرم افزودن/ویرایش
│   ├── static/css/style.css
│   └── application.properties
├── data/                            # محل ساخته‌شدن فایل people.xlsx (در .gitignore)
└── pom.xml
```

---

## 🚀 نحوه اجرا

پیش‌نیاز: **JDK 17** و **Maven**

```bash
git clone https://github.com/<username>/excel-crud-app.git
cd excel-crud-app
mvn spring-boot:run
```

سپس در مرورگر به آدرس زیر بروید:

```
http://localhost:8080
```

فایل اکسل به‌صورت خودکار در مسیر `data/people.xlsx` ساخته می‌شود.

---

## ✅ قوانین اعتبارسنجی

| فیلد | قانون | مثال معتبر |
|---|---|---|
| نام / نام‌خانوادگی | فقط حروف فارسی | `علی`, `احمدی` |
| کد ملی | دقیقاً ۱۰ رقم، یکتا | `0012345678` |
| تاریخ تولد | فرمت `YYYY/MM/DD` | `1400/01/01` |

---

## 🗺 مراحل توسعه پروژه

این پروژه با رویکرد لایه‌به‌لایه (Model → Repository → Service → Controller → UI) توسعه داده شده است:

1. تحلیل مسئله و پیدا کردن موجودیت‌ها
2. طراحی Model و مشخص کردن قوانین صحت‌سنجی
3. طراحی Repository برای خواندن/نوشتن فایل اکسل
4. طراحی Service و پیاده‌سازی قوانین Business
5. طراحی Controller و تعریف مسیرهای HTTP
6. طراحی رابط کاربری (UI) با Thymeleaf

---

## 📌 توسعه‌های آتی

- [ ] اضافه کردن صفحه‌بندی (Pagination) برای فایل‌های اکسل با تعداد رکورد بالا
- [ ] خروجی گرفتن از رکوردها به فرمت‌های دیگر (CSV / PDF)
- [ ] افزودن تست‌های واحد (Unit Test) برای لایه Service و Repository

---
