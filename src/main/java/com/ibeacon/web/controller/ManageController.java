package com.ibeacon.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibeacon.hibernate.Page;
import com.ibeacon.hibernate.PageUtil;
import com.ibeacon.hibernate.PropertyFilter;
import com.ibeacon.model.beacon.Ibeacon;
import com.ibeacon.service.beacon.IbeaconService;


@Controller
@RequestMapping("/manage/")
public class ManageController {

    @Autowired
    IbeaconService ibeaconService;

    @RequestMapping("ibeacon_manage")
    public String companyManage(HttpServletRequest request, Model model,
                                @ModelAttribute("page") Page<Ibeacon> page) {
        System.out.println();
        List<PropertyFilter> filters = PropertyFilter
                .buildFromHttpRequest(request);
        PageUtil.initPage(page, "createTime", Page.DESC, 10);
        ibeaconService.findPage(Ibeacon.class, page, filters);
        model.addAttribute("page", page);
        System.out.println(page);
        return "show";
    }

}
