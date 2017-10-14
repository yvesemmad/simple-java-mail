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

/**
 *
 * @author emmanuel
 */
public class NewEmail {
    
    Email email = new EmailBuilder()
    .from("lollypop", "lolly.pop@pretzelfun.com")
    .to("C. Cane", "candycane@candyshop.org")
    .cc("C. Bo <chocobo@candyshop.org>")
    .subject("hey")
    .text("We should meet up! ;)")
    .build();
    
    
//new Mailer().sendMail(email);
    
}
