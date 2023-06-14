package jmaster.io.notificationservice.service;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import jmaster.io.notificationservice.model.MessageDTO;

public interface EmailService {
	void sendEmail(MessageDTO messageDTO) throws javax.mail.MessagingException ;
}

@Service
class EmailServiceImpl implements EmailService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@Override
	@Async
	public void sendEmail(MessageDTO messageDTO) throws javax.mail.MessagingException {
		try {
			logger.info("START... Sending email");
			
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
			
			//load template email with content
			Context context  = new Context();
			context.setVariable("name", messageDTO.getToName());
			context.setVariable("content", messageDTO.getContent());
			String html = templateEngine.process("welcome-email", context);
			
			
			//send email
			helper.setTo(messageDTO.getTo());
			helper.setText(html,true);
			helper.setSubject(messageDTO.getSubject());
			helper.setFrom(from);
			javaMailSender.send(message);
			
			logger.info("END... Email sent success");
		} catch (MessagingException e) {
			logger.error("Email sent with error: " + e.getMessage());
		}
	}
	
}
