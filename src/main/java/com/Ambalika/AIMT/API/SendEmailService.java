package com.Ambalika.AIMT.API;

import java.time.LocalDateTime;

/*import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void ConfirmUserMail(String name, String pass, String mailTo, String testname, LocalDateTime starttime, int duration) {
        String datetime = starttime.toString();
        String arr[] = datetime.split("T");
        
    	String subject = "Online Test Scheduled - AIMT Lucknow";
        String message = "<html><body>" +
                         "<p>Dear " + name + ",</p>" +
                         "<p>Thank you for registering for <b>" + testname + "</b> by AIMT Lucknow. Below are the details:</p>" +
                         "<ul>" +
                         "<li><b>Test Start Date:</b> "+arr[0]+ "</li>" +
                         "<li><b>Test Start Time:</b> "+arr[1] +" "+(starttime.getHour()>12?("PM"):("AM"))+"</li>" +
                         // "<li><b>Duration:</b> <b>" + duration + " Hours</b></li>" +
                         "<li><b>Test Link:</b> <a href='Portal link'</a></li>" +
                         "<li><b>User ID:</b> " + mailTo + "</li>" +
                         "<li><b>Password:</b> " + pass + "</li>" +
                         "</ul>" +
                         "<p>We appreciate your enthusiasm and commitment to participating in this test. Please ensure you join on time and complete the test within the given duration. For any queries or issues, feel free to contact us.</p>\n<b> Note:<b> Test will be active for "+duration+" Hours from start time. So please give your test accordingly." +
                         "<p>Best of luck!</p>" +
                         "<p>Warm regards,<br>AIMT Lucknow</p>" +
                         "</body></html>";

        try {
            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(message, true); // Enable HTML content
            mailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }
    }

  /*  public void SendTestId(String testname, LocalDateTime ldt, String mailTo) {
        String subject = "Test Id for Softpro Internship Test Portal";
        String message = "<html><body>" +
                         "<p>Hello Dear,</p>" +
                         "<p>Your Test is scheduled at <b>" + ldt + "</b>.</p>" +
                         "<p>Login to this portal through the URL: <a href='http://192.168.29.133:8080'>http://192.168.29.133:8080</a></p>" +
                         "<p><b>Test Name:</b> " + testname + "<br>" +
                         "<b>Test Date:</b> " + ldt + "</p>" +
                         "<p>Thank You,<br>By AIMT Test Portal Team</p>" +
                         "</body></html>";

        try {
            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(message, true); // Enable HTML content
            mailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }
    }*/
}
