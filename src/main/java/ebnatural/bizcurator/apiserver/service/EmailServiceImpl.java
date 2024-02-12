package ebnatural.bizcurator.apiserver.service;

import java.util.Map;
import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import ebnatural.bizcurator.apiserver.domain.CertificationNumber;
import ebnatural.bizcurator.apiserver.repository.CertificationNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final CertificationNumberRepository certificationNumberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    public String certificationNumber;
    @Value("${AdminMail.id}")
    String AdminMail;

    private MimeMessage createSetPwdMessage(String to) throws Exception {
        String setNewPwdLink = "somewhere";
        setNewCertificationNumber(to);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 서비스");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 에비네츄럴 이메일 인증 서비스입니다. </h1>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>비밀번호 재설정 링크입니다. 아래 링크를 통해 비밀번호를 재설정해주세요.(유효시간 : 1시간)</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "<strong>";
        msgg += setNewPwdLink + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("ebnatural522@gmail.com"));//보내는 사람

        return message;
    }



    private MimeMessage createCertificationNumberMessage(String to) throws Exception {
        setNewCertificationNumber(to);

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 서비스");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 에비네츄럴 이메일 인증 서비스입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>아래 인증번호를 확인해주세요.(유효시간 : 1시간)</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE: <strong>";
        msgg += certificationNumber + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("ebnatural522@gmail.com"));//보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Override
    public Map<String, Object> sendSetNewPwdMessage(String to) throws Exception {
        MimeMessage message = createSetPwdMessage(to);
        try {//예외처리
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return Map.of("certification_code", certificationNumber);
    }

    @Override
    public String sendCertificationNumberMessage(String to) throws Exception {
        MimeMessage message = createCertificationNumberMessage(to);
        try {//예외처리
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return certificationNumber;
    }
    private void setNewCertificationNumber(String to) {
        certificationNumber = createKey();
        certificationNumberRepository.findByUsername(to).ifPresent(certificationNumberRepository::delete);
        certificationNumberRepository.save(CertificationNumber.of(to, certificationNumber, passwordEncoder));
    }
}