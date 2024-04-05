package ch.cern.todo.request;

public record TaskCategoryRequest(String categoryName, String categoryDescription) {

    @Override
    public String categoryName() {
        return categoryName;
    }

    @Override
    public String categoryDescription() {
        return categoryDescription;
    }

}
