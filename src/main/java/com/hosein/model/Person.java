package com.hosein.model;

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

    /** شناسه یکتای رکورد در فایل اکسل */
    private int id;

    private String firstName;
    private String lastName;
    private String nationalCode;
    private String birthDate;
}
