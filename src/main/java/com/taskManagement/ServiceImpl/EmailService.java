package com.taskManagement.ServiceImpl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	// 📨 First Deal - Client Onboarding Email
	public void sendClientOnboardingEmail(String to, String clientName, String username, String password,
			String dealName, String dealValue, String salesPersonName) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject("Welcome to Kanak Drishti Infotech - Your Dashboard Credentials");

			String content = """
					<html>
					<body style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:20px;">
					    <div style="max-width:600px; margin:auto; background:white; border-radius:10px;
					                box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:20px;">
					        <h2 style="color:#2c3e50;">Welcome, %s 👋</h2>
					        <p>We are excited to onboard you as our valued client at <b>Kanak Drishti Infotech Pvt. Ltd.</b></p>

					        <h3 style="color:#34495e;">Your First Deal Details</h3>
					        <p><b>Deal Name:</b> %s</p>
					        <p><b>Deal Value:</b> ₹%s</p>
					        <p><b>Sales Person:</b> %s</p>

					        <h3 style="color:#34495e;">Your Client Dashboard Credentials</h3>
					        <p><b>Username (Email):</b> %s</p>
					        <p><b>Password:</b> %s</p>

					        <p>You can login to our
					        <a href="https://your-client-dashboard-url.com" style="color:#2980b9;">Client Dashboard</a>
					        and check all the details about your deal anytime.</p>

					        <br/>
					        <p style="color:#16a085;">Thank you so much for trusting us!</p>
					        <p>Best Regards,<br/>
					        <b>Kanak Drishti Infotech Pvt. Ltd.</b></p>
					    </div>
					</body>
					</html>
					"""
					.formatted(clientName, dealName, dealValue, salesPersonName, username, password);

			helper.setText(content, true);

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send onboarding email", e);
		}
	}

	// 📨 Additional Deal Email
	public void sendAdditionalDealEmail(String to, String clientName, String dealName, String dealValue,
			String salesPersonName) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject("New Deal Added - Kanak Drishti Infotech");

			String content = """
					<html>
					<body style="font-family: Arial, sans-serif; background-color:#f4f6f8; padding:20px;">
					    <div style="max-width:600px; margin:auto; background:white; border-radius:10px;
					                box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:20px;">
					        <h2 style="color:#2c3e50;">Hello, %s 👋</h2>
					        <p>We wanted to inform you that a <b>new deal</b> has been added to your account at
					        <b>Kanak Drishti Infotech Pvt. Ltd.</b></p>

					        <h3 style="color:#34495e;">Deal Details</h3>
					        <p><b>Deal Name:</b> %s</p>
					        <p><b>Deal Value:</b> ₹%s</p>
					        <p><b>Sales Person:</b> %s</p>

					        <p>You can check all your deals in our
					        <a href="https://your-client-dashboard-url.com" style="color:#2980b9;">Client Dashboard</a>.</p>

					        <br/>
					        <p style="color:#16a085;">We appreciate your continued trust in us.</p>
					        <p>Best Regards,<br/>
					        <b>Kanak Drishti Infotech Pvt. Ltd.</b></p>
					    </div>
					</body>
					</html>
					"""
					.formatted(clientName, dealName, dealValue, salesPersonName);

			helper.setText(content, true);

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send additional deal email", e);
		}
	}
}
