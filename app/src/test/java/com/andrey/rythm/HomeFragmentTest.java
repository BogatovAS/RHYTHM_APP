package com.andrey.rythm;

import com.andrey.rhythm.HomeFragment;

import org.junit.Assert;
import org.junit.Test;

public class HomeFragmentTest {
    HomeFragment homeFragment = new HomeFragment();

    @Test
    public void test1_correct_marks_all_fives(){
        // Arrange
        float tk1 = 5;
        float pk1 = 5;
        float tk2 = 5;
        float pk2 = 5;
        int needMark = 4;

        float expectedRating = 200;

        // Act
        float actualRating = homeFragment.calculateRating(tk1, pk1, tk2, pk2, needMark);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - actualRating) < 0.001);
    }

    @Test
    public void test2_correct_marks(){
        // Arrange
        float tk1 = 3;
        float pk1 = 3;
        float tk2 = 4;
        float pk2 = 5;
        int needMark = 5;

        float expectedRating = 155;

        // Act
        float actualRating = homeFragment.calculateRating(tk1, pk1, tk2, pk2, needMark);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - actualRating) < 0.001);
    }

    @Test
    public void test3_not_filled_marks(){
        // Arrange
        float tk1 = 0;
        float pk1 = 0;
        float tk2 = 0;
        float pk2 = 0;
        int needMark = 0;

        float expectedRating = -1;

        // Act
        float actualRating = homeFragment.calculateRating(tk1, pk1, tk2, pk2, needMark);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - actualRating) < 0.001);
    }

    @Test
    public void test4_incorrect_marks(){
        // Arrange
        float tk1 = 6;
        float pk1 = 3;
        float tk2 = 4;
        float pk2 = 2;
        int needMark = 5;

        float expectedRating = -1;

        // Act
        float actualRating = homeFragment.calculateRating(tk1, pk1, tk2, pk2, needMark);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - actualRating) < 0.001);
    }

    @Test
    public void calculateNeedMark_test1(){
        // Arrange
        int wish = 5;
        float rating = 200;

        float expectedRating = 3.916F;

        // Act
        float needMark = homeFragment.calculateNeedMark(rating, wish);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - needMark) < 0.001);
    }
    @Test
    public void calculateNeedMark_test2(){
        // Arrange
        int wish = 4;
        float rating = 200;

        float expectedRating = 2.5F;

        // Act
        float needMark = homeFragment.calculateNeedMark(rating, wish);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - needMark) < 0.001);
    }

    @Test
    public void calculateNeedMark_test3(){
        // Arrange
        int wish = 3;
        float rating = 200;

        float expectedRating = 0.833F;

        // Act
        float needMark = homeFragment.calculateNeedMark(rating, wish);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - needMark) < 0.001);
    }

    @Test
    public void calculateNeedMark_test4(){
        // Arrange
        int wish = 4;
        float rating = 100;

        float expectedRating = 4.166F;

        // Act
        float needMark = homeFragment.calculateNeedMark(rating, wish);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - needMark) < 0.001);
    }

    @Test
    public void calculateNeedMark_test5(){
        // Arrange
        int wish = 2;
        float rating = 200;

        float expectedRating = 0;

        // Act
        float needMark = homeFragment.calculateNeedMark(rating, wish);

        // Assert
        Assert.assertTrue(Math.abs(expectedRating - needMark) < 0.001);
    }

}