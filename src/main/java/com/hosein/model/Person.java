package com.hosein.model;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * مدل داده‌ای هر سطر از فایل اکسل.
 * هر شی از این کلاس معادل یک سطر(یک رکورد) در فایل اکسل است.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    /**
     * شناسه یکتای رکورد در فایل اکسل
     */
    private int id;

    @NotBlank(message = "نام الزامی است")
    @Pattern(regexp = "^[\\u0600-\\u06FF\\s]+$",
            message = "نام باید فقط شامل حروف فارسی باشد")
    private String firstName;


    @NotBlank(message = "نام خانوادگی الزامی است")
    @Pattern(regexp = "^[\\u0600-\\u06FF\\s]+&",
            message = "نام خانوادگی باید فقط شامل حروف فارسی باشد")
    private String lastName;


    @NotBlank(message = "کد ملی الزامی است")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "کد ملی باید دقیقا 10 رقم و فقط شامل عدد باشد")
    private String nationalCode;


    @NotBlank(message = "تاریخ تولد الزامی است")
    @Pattern(regexp = "^[0-9]{4}/[0-9]{2}/[0-9]{2}$",
            message = "تاریخ تولد باید به فرمت ۱۴۰۰/۰۱/۰۱ باشد")
    private String birthDate;
}
