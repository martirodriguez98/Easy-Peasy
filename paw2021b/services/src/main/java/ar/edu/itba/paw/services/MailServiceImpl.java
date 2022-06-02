package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private String appProtocol;

    @Autowired
    private String appHost;

    @Autowired
    private String appWebContext;

    @Autowired
    private int appPort;

    @Autowired
    private URL appBaseUrl;


    @Autowired
    Environment environment;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    private Locale locale;

    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
    private static final int VERIFICATION = 0;
    private static final int PASSWORD_RESET = 1;
    private static final int SHARE = 2;
    private static final int BANNED = 3;

    private void setupVerificationEmail(String to, String token){

        locale = LocaleContextHolder.getLocale();
        Map<String, Object> attrs = new HashMap<>();
        String url = "";
        try {
            url = new URL(appProtocol, appHost, appPort, appWebContext +"verification?token="+token).toString();
        }catch (MalformedURLException e){
            LOGGER.warn("Failed to form URL");
        }
        attrs.put("to", to);
        attrs.put("url", url);
        try{
            sendMail("verification", messageSource.getMessage("verification.subject", new Object[]{}, locale), attrs, locale);
        }catch(MessagingException e){
            LOGGER.warn("Error sending verification email");
        }
        LOGGER.info("Verification Email sent to {}", to);


    }

    private void setupPasswordResetEmail(String to, String token) {

        locale = LocaleContextHolder.getLocale();
        Map<String, Object> attrs = new HashMap<>();
        String url = "";
        try {
            url = new URL(appProtocol, appHost, appPort, appWebContext +"passwordReset?token="+token).toString();
        }catch (MalformedURLException e){
            LOGGER.warn("Failed to form URL");
        }
        attrs.put("to", to);
        attrs.put("url", url);
        try{
            sendMail("passwordReset", messageSource.getMessage("reset.subject", new Object[]{}, locale), attrs, locale);
        }catch(MessagingException e){
            LOGGER.warn("Error sending password reset email");
        }
        LOGGER.info("Password reset Email sent to {}", to);


    }

    private void setupBannedEmail(String to) {

        locale = LocaleContextHolder.getLocale();
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("to", to);
        try{
            sendMail("banned", messageSource.getMessage("banned.subject", new Object[]{}, locale), attrs, locale);
        }catch(MessagingException e){
            LOGGER.warn("Error sending banned email");
        }
        LOGGER.info("Banned email sent to {}", to);


    }

    private void setupShareRecipeEmail(String from, String to, String recipeTitle, long recipeId){

        locale = LocaleContextHolder.getLocale();
        String url = "";
        try {
            url = new URL(appProtocol, appHost, appPort, appWebContext + "recipes/" + recipeId).toString();
        }catch (MalformedURLException e){
            LOGGER.warn("Failed to form URL");
        }
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("to", to);
        attrs.put("from", from);
        attrs.put("title", recipeTitle);
        attrs.put("url", url);


        try{
            sendMail("share", messageSource.getMessage("share.subject", new Object[]{}, locale), attrs, locale);
        }catch(MessagingException e){
            LOGGER.warn("Error sending share email");
        }
        LOGGER.info("Shared recipe from {} to {}", from, to);


    }

    @Async
    @Override
    public void sendShareRecipeEmail(String from, String to, String recipeTitle, long recipeId){
        setupEmailTemplate(SHARE, from, to, recipeTitle, recipeId);
    }

    @Async
    @Override
    public void sendPasswordResetEmail(String to, String token){
        setupEmailTemplate(PASSWORD_RESET, to, token, null, 0);
    }

    @Async
    @Override
    public void sendVerificationEmail(String to, String token){
        setupEmailTemplate(VERIFICATION, to, token, null, 0);
    }

    @Async
    @Override
    public void sendBannedEmail(String to){
        setupEmailTemplate(BANNED, to, null, null, 0);
    }
    private void setupEmailTemplate(int type, String att1, String att2, String att3, long att4){
        switch(type){
            case VERIFICATION: setupVerificationEmail(att1, att2);break;
            case PASSWORD_RESET: setupPasswordResetEmail(att1, att2); break;
            case SHARE:setupShareRecipeEmail (att1, att2, att3, att4); break;
            case BANNED:setupBannedEmail(att1); break;
        }
    }

    @Async
    protected void sendMail(String template, String subject, Map<String, Object> variables, final Locale locale) throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariables(variables);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(subject);
        message.setFrom("easypeasywebrecipes@gmail.com");
        message.setTo((String) variables.get("to"));

        // Create the HTML body using Thymeleaf
        final String htmlContent = htmlTemplateEngine.process(template, ctx);

        message.setText(htmlContent, true /* isHtml */);

        // Send email
        mailSender.send(mimeMessage);
        LOGGER.info("Email sent with subject {}", subject);

    }

}
