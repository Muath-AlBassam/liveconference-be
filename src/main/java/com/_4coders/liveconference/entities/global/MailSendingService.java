package com._4coders.liveconference.entities.global;

import com._4coders.liveconference.entities.account.Account;
import com._4coders.liveconference.entities.account.activation.AccountActivation;
import com._4coders.liveconference.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSendingService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;


    public void sendActivationCodeEmail(Account account, AccountActivation accountActivation) throws MessagingException {
        final Context context = new Context();
        context.setVariable("account", account);
        context.setVariable("accountCode", accountActivation);
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Account activation code");
        message.setTo(account.getEmail());
        String template = "activation_code_mail";
        final String textContent = templateEngine.process(template,
                context);
        message.setText(textContent, true);
        this.mailSender.send(mimeMessage);
    }

    public void sendCallInviteEmail(User sender, Account targetAccount, String sessionId) throws MessagingException {
        final Context context = new Context();
        context.setVariable("senderUserUserName", sender.getUserName());
        context.setVariable("sessionId", sessionId);
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Call invitation");
        message.setTo(targetAccount.getEmail());
        String template = "call_invite_mail";
        final String textContent = templateEngine.process(template,
                context);
        message.setText(textContent, true);
        this.mailSender.send(mimeMessage);
    }

}
