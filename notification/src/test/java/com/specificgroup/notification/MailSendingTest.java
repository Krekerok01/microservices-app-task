package com.specificgroup.notification;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MailSendingTest {
    private SimpleSmtpServer server;

    @BeforeEach
    public void setUp() throws Exception {
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testSend() throws MessagingException {
        sendMessage(server.getPort(), "sender@here.com", "Test", "Test Body", "receiver@there.com");

        List<SmtpMessage> emails = server.getReceivedEmails();
        assertEquals(1, emails.size());
        SmtpMessage email = emails.get(0);
        assertEquals("Test", email.getHeaderValue("Subject"));
        assertEquals("Test Body", email.getBody());
        assertTrue(email.getHeaderNames().contains("Date"));
        assertTrue(email.getHeaderNames().contains("From"));
        assertTrue(email.getHeaderNames().contains("To"));
        assertTrue(email.getHeaderNames().contains("Subject"));
        assertTrue(email.getHeaderValues("To").contains("receiver@there.com"));
        assertEquals("receiver@there.com", email.getHeaderValue("To"));
    }

    private Properties getMailProperties(int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", "true");
        return mailProps;
    }


    private void sendMessage(int port, String from, String subject, String body, String to) throws MessagingException {
        Properties mailProps = getMailProperties(port);
        Session session = Session.getInstance(mailProps, null);

        MimeMessage msg = createMessage(session, from, to, subject, body);

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setJavaMailProperties(mailProps);

        javaMailSender.send(msg);
    }

    private MimeMessage createMessage(
            Session session, String from, String to, String subject, String body) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(body);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        return msg;
    }
}
