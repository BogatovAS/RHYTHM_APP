package com.andrey.rythm;

import com.andrey.rhythm.MarksHttpUrlConnection;
import com.andrey.rhythm.db.DBHelper;

import org.junit.Assert;
import org.junit.Test;

public class MarksHttpUrlConnectionTests {

    MarksHttpUrlConnection marskHttpConnection = new MarksHttpUrlConnection();

    @Test
    public void MarksHttpUrlConnection_GetMarks_Test1(){
        // Arrange
        String htmlFragment =
                "<table> " +
                    "<thead> " +
                        "<tr>" +
                        "</tr>" +
                    "</thead> " +
                    "<tbody> " +
                        "<tr> " +
                            "<td> Безопасность жизнедеятельности </td> " +
                            "<td> <span>5</span> </td> " +
                            "<td> <span>5</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>100</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>н/д</span> </td> " +
                        "</tr> " +
                    "</tbody> " +
                "</table>";

        String expectedString = "Без жиз ;5;5;0;0;100;0;н/д;\n";

        // Act
        String result = marskHttpConnection.GetMarks(htmlFragment);

        // Assert
        Assert.assertEquals(expectedString, result);
    }

    @Test
    public void MarksHttpUrlConnection_GetMarks_Test2(){
        // Arrange
        String htmlFragment =
                "<table> " +
                    "<thead> " +
                        "<tr>" +
                        "</tr>" +
                    "</thead> " +
                    "<tbody> " +
                        "<tr> " +
                            "<td> БЖД </td> " +
                            "<td> <span>5</span> </td> " +
                            "<td> <span>5</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>100</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>н/д</span> </td> " +
                        "</tr> " +
                    "</tbody> " +
                "</table>";

        String expectedString = "БЖД ;5;5;0;0;100;0;н/д;\n";

        // Act
        String result = marskHttpConnection.GetMarks(htmlFragment);

        // Assert
        Assert.assertEquals(expectedString, result);
    }

    @Test
    public void MarksHttpUrlConnection_GetMarks_Test3(){
        // Arrange
        String htmlFragment =
                "<table> " +
                    "<thead> " +
                        "<tr>" +
                        "</tr>" +
                    "</thead> " +
                "</table>" +
                "<td> Безопасность жизнедеятельности </td> " +
                "<td> <span>5</span> </td> " +
                "<td> <span>5</span> </td> " +
                "<td> <span>0</span> </td> " +
                "<td> <span>0</span> </td> " +
                "<td> <span>100</span> </td> " +
                "<td> <span>0</span> </td> " +
                "<td> <span>н/д</span> </td> ";

        String expectedString = "";

        // Act
        String result = marskHttpConnection.GetMarks(htmlFragment);

        // Assert
        Assert.assertEquals(expectedString, result);
    }

    @Test
    public void MarksHttpUrlConnection_GetMarks_Test4(){
        // Arrange
        String htmlFragment =
                "<table> " +
                    "<thead> " +
                        "<tr>" +
                        "</tr>" +
                    "</thead> " +
                    "<tbody> " +
                        "<tr> " +
                            "<td> <span>\n</span></td> " +
                            "<td> <span>5</span> </td> " +
                            "<td> <span>5</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>100</span> </td> " +
                            "<td> <span>0</span> </td> " +
                            "<td> <span>н/д</span> </td> " +
                        "</tr> " +
                    "</tbody> " +
                "</table>";

        String expectedString = "5 ;5;0;0;100;0;н/д;\n";

        // Act
        String result = marskHttpConnection.GetMarks(htmlFragment);

        // Assert
        Assert.assertEquals(expectedString, result);
    }

    @Test
    public void MarksHttpUrlConnection_GetMarks_Test5(){
        // Arrange
        String htmlFramgent =
                "<table> " +
                    "<thead> " +
                        "<tr>" +
                        "</tr>" +
                    "</thead> " +
                    "<tbody> " +
                        "<tr> " +
                            "<td> БЖД </td> " +
                        "</tr> " +
                    "</tbody> " +
                "</table>";

        String expectedString = "БЖД ;\n";

        // Act
        String result = marskHttpConnection.GetMarks(htmlFramgent);

        // Assert
        Assert.assertEquals(expectedString, result);
    }
}
