package ru.marina.notes.meals;

import java.util.Objects;

public class FoodInfo {
    private String foodInfoId;
    private String mealId;
    private String product;
    private int portionWeight;
    private int calories;

    public FoodInfo() {
    }

    public FoodInfo(final String foodInfoId, final String mealId, final String product, final int portionWeight, final int calories) {
        this.foodInfoId = foodInfoId;
        this.mealId = mealId;
        this.product = product;
        this.portionWeight = portionWeight;
        this.calories = calories;
    }

    public String getFoodInfoId() {
        return foodInfoId;
    }

    public String getMealId() {
        return mealId;
    }

    public String getProduct() {
        return product;
    }

    public int getPortionWeight() {
        return portionWeight;
    }

    public int getCalories() {
        return calories;
    }

    public void setFoodInfoId(final String foodInfoId) {
        this.foodInfoId = foodInfoId;
    }

    public void setMealId(final String mealId) {
        this.mealId = mealId;
    }

    public void setProduct(final String product) {
        this.product = product;
    }

    public void setPortionWeight(final int portionWeight) {
        this.portionWeight = portionWeight;
    }

    public void setCalories(final int calories) {
        this.calories = calories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FoodInfo foodInfo = (FoodInfo) o;
        return portionWeight == foodInfo.portionWeight &&
                calories == foodInfo.calories &&
                Objects.equals(foodInfoId, foodInfo.foodInfoId) &&
                Objects.equals(mealId, foodInfo.mealId) &&
                Objects.equals(product, foodInfo.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodInfoId, mealId, product, portionWeight, calories);
    }
}
