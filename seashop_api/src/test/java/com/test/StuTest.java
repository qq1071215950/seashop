package com.test;

import com.haojing.Application;
import com.haojing.entity.Stu;
import com.haojing.service.StuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StuTest {

    @Autowired
    private StuService stuService;

    @Test
    public void testsave(){
        Stu stu = stuService.getStuInfo(1212);
        System.out.println(stu.toString());
    }
}
