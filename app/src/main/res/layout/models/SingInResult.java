package layout.models;

import androidx.annotation.Nullable;

public class SingInResult {
    @Nullable
    private User success;

    @Nullable
    private String errorMessage;

    public SingInResult(@Nullable String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public SingInResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
   public User getSuccess() {
        return success;
    }
    @Nullable
   public String getErrorMessage() {
        return errorMessage;
    }
}
