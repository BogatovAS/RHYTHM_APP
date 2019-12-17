package com.andrey.rythm;

import com.andrey.rhythm.Marks;
import com.andrey.rhythm.db.DBHelper;
import com.andrey.rhythm.models.SubjectMarks;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MarksTests {
    Marks marksFragment = new Marks();

    @Mock
    DBHelper db = Mockito.mock(DBHelper.class);

    @Test
    public void Marks_GetMarks_Returns_One_SubjectMarks_Object() {
        // Arrange
        String testMarks = "15.10.2019\nБЖД:5;5;5;5;";

        SubjectMarks subjectMarks = new SubjectMarks();
        subjectMarks.UpdateDate = "15.10.2019";
        subjectMarks.Marks = new String[] {"5","5","5","5"};
        subjectMarks.Subject = "БЖД";

        ArrayList<SubjectMarks> expectedMarks = new ArrayList<>();
        expectedMarks.add(subjectMarks);

        when(db.addSubjectRecord(any(SubjectMarks.class))).thenReturn(true);

        // Act
        ArrayList<SubjectMarks> marks = this.marksFragment.GetMarks(testMarks, db);

        // Assert
        Assert.assertEquals(expectedMarks.size(), marks.size());
        for(int i=0 ;i< marks.size();i++)
        {
            Assert.assertEquals(expectedMarks.get(i).Subject, marks.get(i).Subject);
            Assert.assertEquals(expectedMarks.get(i).UpdateDate, marks.get(i).UpdateDate);
            for(int j = 0; j < marks.get(i).Marks.length; j++)
            {
               Assert.assertEquals(expectedMarks.get(i).Marks[j], marks.get(i).Marks[j]);
            }
        }
    }

    @Test
    public void Marks_GetMarks_When_HttpRequest_Returns_Empty_String()  {
        // Arrange
        String testMarks = "";
        ArrayList<SubjectMarks> expectedMarks = new ArrayList<>();

        when(db.addSubjectRecord(any(SubjectMarks.class))).thenReturn(true);

        // Act
        ArrayList<SubjectMarks> marks = this.marksFragment.GetMarks(testMarks, db);

        // Assert
        Assert.assertEquals(expectedMarks.size(), marks.size());
        for(int i=0 ;i< marks.size();i++)
        {
            Assert.assertEquals(expectedMarks.get(i).Subject, marks.get(i).Subject);
            Assert.assertEquals(expectedMarks.get(i).UpdateDate, marks.get(i).UpdateDate);
            for(int j = 0; j < marks.get(i).Marks.length; j++)
            {
                Assert.assertEquals(expectedMarks.get(i).Marks[j], marks.get(i).Marks[j]);
            }
        }
    }

    @Test
    public void Marks_GetMarks_Returns_Two_SubjectMarks_Objects()  {
        // Arrange
        String testMarks = "15.10.2019\nБЖД:5;5;5;5;\nТестирование:4;4;4;4;";

        SubjectMarks subjectMarks1 = new SubjectMarks();
        subjectMarks1.UpdateDate = "15.10.2019";
        subjectMarks1.Marks = new String[] {"5","5","5","5"};
        subjectMarks1.Subject = "БЖД";

        SubjectMarks subjectMarks2 = new SubjectMarks();
        subjectMarks2.UpdateDate = "15.10.2019";
        subjectMarks2.Marks = new String[] {"4","4","4","4"};
        subjectMarks2.Subject = "Тестирование";

        ArrayList<SubjectMarks> expectedMarks = new ArrayList<>();
        expectedMarks.add(subjectMarks1);
        expectedMarks.add(subjectMarks2);

        when(db.addSubjectRecord(any(SubjectMarks.class))).thenReturn(true);

        // Act
        ArrayList<SubjectMarks> marks = this.marksFragment.GetMarks(testMarks, db);

        // Assert
        Assert.assertEquals(expectedMarks.size(), marks.size());
        for(int i=0 ;i< marks.size();i++)
        {
            Assert.assertEquals(expectedMarks.get(i).Subject, marks.get(i).Subject);
            Assert.assertEquals(expectedMarks.get(i).UpdateDate, marks.get(i).UpdateDate);
            for(int j = 0; j < marks.get(i).Marks.length; j++)
            {
                Assert.assertEquals(expectedMarks.get(i).Marks[j], marks.get(i).Marks[j]);
            }
        }
    }

    @Test
    public void Marks_GetMarks_Returns_Empty_SubjectMarks_Object_When_Error_While_Adding_To_DB() {
        // Arrange
        String testMarks = "15.10.2019\nБЖД:5;5;5;5;";

        SubjectMarks subjectMarks = new SubjectMarks();
        subjectMarks.UpdateDate = "15.10.2019";
        subjectMarks.Marks = new String[] {"5","5","5","5"};
        subjectMarks.Subject = "БЖД";

        ArrayList<SubjectMarks> expectedMarks = new ArrayList<>();
        expectedMarks.add(subjectMarks);

        when(db.addSubjectRecord(any(SubjectMarks.class))).thenReturn(false);

        // Act
        ArrayList<SubjectMarks> marks = this.marksFragment.GetMarks(testMarks, db);

        // Assert
        Assert.assertEquals(0, marks.size());
    }
}
