package ar.edu.itba.paw.interfaces;

public interface MailService {
    void sendShareRecipeEmail(String from, String to, String recipeTitle, long recipeId);
    void sendVerificationEmail(String to, String token);
    void sendPasswordResetEmail(String to, String token);
    void sendBannedEmail(String to);
}
