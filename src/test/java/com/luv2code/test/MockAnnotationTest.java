package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

//    @Mock
    @MockBean
    private ApplicationDao applicationDao;

//    @InjectMocks
    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Foreman");
        studentOne.setEmailAddress("ericforman@gmail.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When and Verify")
    @Test
    public void assertEqualsTestAddGrades(){
        when(applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults())).thenReturn(100.0);
        assertEquals(100,applicationService.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults()));
        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
        verify(applicationDao,times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @DisplayName("Find Gpa")
    @Test
    public void assertEqualsTestFindGpa(){
        when(applicationDao.findGradePointAverage(
                studentGrades.getMathGradeResults())).thenReturn(88.31);
        assertEquals(88.31,applicationService.findGradePointAverage(
                studentGrades.getMathGradeResults()));
        verify(applicationDao).findGradePointAverage(studentGrades.getMathGradeResults());
        verify(applicationDao,times(1)).findGradePointAverage(studentGrades.getMathGradeResults());
    }

    @DisplayName("Not null")
    @Test
    public void assertEqualsTestNotNull(){
        when(applicationDao.checkNull(
                studentGrades.getMathGradeResults())).thenReturn(true);
        assertNotNull(applicationService.checkNull(
                studentGrades.getMathGradeResults()));
        verify(applicationDao).checkNull(studentGrades.getMathGradeResults());
        verify(applicationDao,times(1)).checkNull(studentGrades.getMathGradeResults());
    }

    @DisplayName("Throw runtime exception")
    @Test
    public void throwRuntimeException(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class, ()->{
            applicationService.checkNull(nullStudent);
        });

        verify(applicationDao,times(1)).checkNull(nullStudent);
    }

    @DisplayName("Mltiple stubbing")
    @Test
    public void stubbingConsecutiveCalls(){
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent))
                .thenReturn(new RuntimeException())
                .thenReturn("Do not throw exception second time");

        assertThrows(RuntimeException.class, ()->{
            applicationService.checkNull(nullStudent);
        });

        assertEquals("Do not throw exception second time", applicationDao.checkNull(nullStudent));
        verify(applicationDao,times(2)).checkNull(nullStudent);

    }
}
