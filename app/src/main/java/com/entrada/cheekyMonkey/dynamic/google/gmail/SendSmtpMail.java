package com.entrada.cheekyMonkey.dynamic.google.gmail;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Rahul on 24/06/2017.
 */
public class SendSmtpMail {


    public static void sendMessage(String recipient, String subject, String body){

        final String username = "cheekymonkeyentrada@gmail.com";
        final String password = "Entrada123";
        //final String recipient = "rgrahulgupta001@gmail.com";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

       /* Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });*/

        Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            new AsyncTask<Void,Void, Void>(){

                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        Transport.send(message);

                    }catch (MessagingException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }.execute();

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public static void sendMessageWithAttachment(String recipient, String subject, String data,
                                                 String attachedFilePath){

        final String username = "cheekymonkeyentrada@gmail.com";
        final String password = "Entrada123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(data);


            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String file = "/storage/sdcard/StewardPad/log/" + data;
            String fileName = "attachmentName";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            messageBodyPart.attachFile(attachedFilePath);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            new AsyncTask<Void,Void, Void>(){

                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        Transport.send(message);

                    }catch (MessagingException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }.execute();
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

