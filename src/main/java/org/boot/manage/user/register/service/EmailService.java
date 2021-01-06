package org.boot.manage.user.register.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.internet.MimeMessage;

import org.boot.manage.user.register.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	private SimpleMailMessage message = new SimpleMailMessage();
	boolean success = false;

	public boolean sendMessage(String email, String subject, String body) {
		try {
			
			message.setTo(email);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	// send mime message
	public boolean sendMimeMessage(String to, String subject, String body, String emailBody) { // ,byte[] content

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(body, true);
			messageHelper.setText(emailBody, true);
			// messageHelper.addAttachment("MyTestFile.txt", new ByteArrayResource(content),"text/html");
			mailSender.send(mimeMessage);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String sendMail(UserDetails user) {

		String to = user.getEmail();
		String subject = "java email sending demo";
		String body = "Demo is successfully completed and what about you ?";
		String emailBody = getUnlockAccEmailBody(user);

		boolean sendMimeMessage = sendMimeMessage(to, subject, body, emailBody);

		if (sendMimeMessage)
			return "Email is successfully send to your account " + user.getFirstName();
		else
			return "registered successfully but still your account is not active due to technical issue,"
					+ "we will send an activation link on your registered email";
	}

	public String getUnlockAccEmailBody(UserDetails user) {
		String fileName = "MyTestFile.txt";

		Stream<String> streamLine = null;
		List<String> mailLines = null;
		String mailBody = null;
		Path path = null;
		try {
			path = Paths.get(fileName, "");
			streamLine = Files.lines(path);
			mailLines = streamLine
					.map(flow -> flow.replace("{FNAME}", user.getFirstName())
					.replace("{LNAME}", user.getLastName())
					.replace("{TEMP-PWD}", user.getPassword()).replace("{EMAIL}", user.getEmail()))
					.collect(Collectors.toList());

			mailBody = String.join("", mailLines);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (streamLine != null)
				streamLine.close();
		}

		return mailBody;
	}
}
