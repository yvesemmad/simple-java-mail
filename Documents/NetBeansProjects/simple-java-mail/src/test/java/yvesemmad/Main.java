/*
 * Copyright 2017 emmanuel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yvesemmad;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;
import testutil.ConfigLoaderTestHelper;

/**
 *
 * @author emmanuel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String properties[][] = {{"user","yvesdiby3dev@gmail.com"},{"password","1551992y"}};
        
        Email email = new EmailBuilder()
        .from("yvesemmad", "yvesdiby3dev@gmail.com")
        .to("Yves Emmanuel", "yvesemmanueld@gmail.com")
        .cc("D. YE <Emmanuel.DIBY@ascens-services.com>")
        .subject("hey")
        .text("We should meet up! ;)")
        .build();
        
        //new Mailer().sendMail(email);
        try {
            ConfigLoaderTestHelper.clearConfigProperties();
            GmailTLS587(email, properties);
        } catch (Exception e) {
            System.err.println("exception: "+e.getMessage());
        }
        
        

    }
    
    public static void GmailTLS25(Email email, String[][] properties) {
        
        new Mailer("smtp.gmail.com", 25, properties[0][1], properties[1][1], TransportStrategy.SMTP_TLS).sendMail(email);
        
    }
    public static void GmailTLS587(Email email, String[][] properties) {
        new Mailer("smtp.gmail.com", 587, properties[0][1], properties[1][1], TransportStrategy.SMTP_TLS).sendMail(email);
        
    }
    public static void GmailSSL465(Email email, String[][] properties) {
        new Mailer("smtp.gmail.com", 465, properties[0][1], properties[1][1], TransportStrategy.SMTP_SSL).sendMail(email);
    }
    
}
