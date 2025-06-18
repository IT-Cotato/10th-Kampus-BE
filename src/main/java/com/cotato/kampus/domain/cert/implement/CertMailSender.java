package com.cotato.kampus.domain.cert.implement;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CertMailSender {

	private final JavaMailSender emailSender;

	public void sendVerificationMail(String email, String code) {
		try {
			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			helper.setFrom("kampus.contact@gmail.com", "KAMPUS");
			helper.setTo(email);
			helper.setSubject("🎓 KAMPUS 대학 인증 코드입니다");
			helper.setText(createHtmlContent(code), true); // HTML 사용

			emailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private String createHtmlContent(String code) {
		return String.format("""
        <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;">
            <h2 style="color: #333;">🎓 KAMPUS 대학 인증</h2>
            <p>안녕하세요! 대학 인증을 위한 코드를 보내드립니다.</p>
            
            <div style="background: #f0f8ff; border: 2px solid #4169e1; border-radius: 8px; 
                        padding: 20px; text-align: center; margin: 20px 0;">
                <h3 style="margin: 0; color: #4169e1;">인증 코드</h3>
                <div style="font-size: 32px; font-weight: bold; color: #4169e1; 
                           letter-spacing: 4px; margin-top: 10px;">%s</div>
            </div>
            
            <p style="color: #666; font-size: 14px;">
                ⏰ 이 코드는 10분 후 만료됩니다.<br>
                🔒 보안을 위해 타인과 공유하지 마세요.
            </p>
            
            <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
            <p style="color: #999; font-size: 12px; text-align: center;">
                KAMPUS - 대학생 커뮤니티
            </p>
        </div>
        """, code);
	}
}
