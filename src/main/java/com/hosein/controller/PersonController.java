package com.hosein.controller;

import com.hosein.model.Person;
import com.hosein.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * کنترلر مربوط به نمایش جدول و انجام عملیات CRUD روی رکورد های فایل اکسل.
 */
@Controller
@RequestMapping("people")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * نمایش جدول تمام رکوردها
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("people", personService.findAll());
        return "index";
    }

    /**
     * نمایش فرم افزودن رکورد جدید
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("isEdit", false);
        return "form";
    }

    /**
     * نمایش فرم ویرایش یک رکورد موجود
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Person person = personService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("رکورد با این شناسه یافت نشد"));
        model.addAttribute("person", person);
        model.addAttribute("isEdit", true);
        return "form";
    }

    /**
     * ثبت رکورد جدید یا ذخیره تغییرات رکورد ویرایش‌شده
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("person") Person person,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", person.getId() != 0);
            return "form";
        }

        boolean isNew = person.getId() == 0;
        try {
            personService.save(person);

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", person.getId() != 0);
            return "form";
        }

        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "رکورد جدید با موفقیت ثبت شد" : "تغییرات با موفقیت ذخیره شد");
        return "redirect:/people";
    }

    /**
     * حذف یک رکورد
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        personService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "رکورد با موفقیت حذف شد");
        return "redirect:/people";
    }
}
